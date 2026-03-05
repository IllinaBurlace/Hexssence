package meow.illina.hexssence.recipe

import meow.illina.hexssence.recipe.utils.ChancedResult
import meow.illina.hexssence.recipe.utils.IngredientCounted
import net.minecraft.core.NonNullList
import net.minecraft.resources.ResourceLocation

class SpellRecipeBuilder<T : SpellRecipe>(
    val factory: SpellRecipeFactory<T>,
    val id: ResourceLocation,
) {
    val params = SpellRecipeParams(id)

    fun withIngredients(ingredients: NonNullList<IngredientCounted>) : SpellRecipeBuilder<T> {
        params.ingredients = ingredients
        return this
    }
    fun withResults(results: NonNullList<ChancedResult>) : SpellRecipeBuilder<T> {
        params.results = results
        return this
    }
    fun withCost(cost: Long) : SpellRecipeBuilder<T> {
        params.mediaCost = cost
        return this
    }

    fun build() : T {
        return factory.create(params)
    }

    fun interface SpellRecipeFactory<T : SpellRecipe> {
        fun create(params: SpellRecipeParams): T
    }

}