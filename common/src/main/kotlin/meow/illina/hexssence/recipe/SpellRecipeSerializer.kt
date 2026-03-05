package meow.illina.hexssence.recipe

import com.google.gson.JsonObject
import meow.illina.hexssence.recipe.utils.ChancedResult
import meow.illina.hexssence.recipe.utils.IngredientCounted
import net.minecraft.core.NonNullList
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.GsonHelper
import net.minecraft.world.item.crafting.RecipeSerializer

abstract class SpellRecipeSerializer<T : SpellRecipe>(
    private val factory: SpellRecipeBuilder.SpellRecipeFactory<T>,
) : RecipeSerializer<T> {
    override fun fromJson(recipeId: ResourceLocation, json: JsonObject): T {
        val builder = SpellRecipeBuilder(factory, recipeId)
        val ingredients = NonNullList.create<IngredientCounted>()
        val results = NonNullList.create<ChancedResult>()

        for (je in GsonHelper.getAsJsonArray(json, "ingredients"))
            ingredients.add(IngredientCounted.fromJson(je))
        for (je in GsonHelper.getAsJsonArray(json, "results"))
            results.add(ChancedResult.fromJson(je))

        builder.withIngredients(ingredients)
            .withResults(results)

        if (GsonHelper.isValidNode(json, "mediaCost"))
            builder.withCost(GsonHelper.getAsLong(json, "mediaCost"))

        return builder.build()
    }

    override fun fromNetwork(recipeId: ResourceLocation, buf: FriendlyByteBuf): T {
        val ingredients = NonNullList.create<IngredientCounted>()
        val results = NonNullList.create<ChancedResult>()

        var size = buf.readInt()
        for (@Suppress("UNUSED_PARAMETER" )i in 0 .. size)
            ingredients.add(IngredientCounted.fromNetwork(buf))
        size = buf.readInt()
        for (@Suppress("UNUSED_PARAMETER") i in 0 .. size)
            results.add(ChancedResult.fromNetwork(buf))

        return SpellRecipeBuilder(factory, recipeId).withIngredients(ingredients)
            .withResults(results)
            .withCost(buf.readLong())
            .build()
    }

    override fun toNetwork(buf: FriendlyByteBuf, recipe: T) {
        val ingredients = recipe.params.ingredients
        val results = recipe.params.results

        buf.writeInt(ingredients.size)
        ingredients.forEach { ingredient -> ingredient.toNetwork(buf) }

        buf.writeInt(results.size)
        results.forEach { result -> result.toNetwork(buf) }

        buf.writeLong(recipe.params.mediaCost)
    }
}