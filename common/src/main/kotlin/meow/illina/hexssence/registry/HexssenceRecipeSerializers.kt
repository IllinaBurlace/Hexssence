package meow.illina.hexssence.registry

import meow.illina.hexssence.recipe.combination.CombinationRecipeSerializer
import meow.illina.hexssence.recipe.extraction.ExtractionRecipeSerializer
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.crafting.RecipeSerializer

object HexssenceRecipeSerializers : HexssenceRegistrar<RecipeSerializer<*>>(
    Registries.RECIPE_SERIALIZER,
    { BuiltInRegistries.RECIPE_SERIALIZER }
){
    val EXTRACTION = register("essence_extraction") {
        ExtractionRecipeSerializer
    }
    val COMBINATION = register("essence_combination") {
        CombinationRecipeSerializer
    }
}