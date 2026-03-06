package meow.illina.hexssence.registry

import meow.illina.hexssence.recipe.combination.CombinationRecipe
import meow.illina.hexssence.recipe.extraction.ExtractionRecipe
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.crafting.RecipeType

object HexssenceRecipeTypes : HexssenceRegistrar<RecipeType<*>>(
    Registries.RECIPE_TYPE,
    { BuiltInRegistries.RECIPE_TYPE }
){
    val EXTRACTION = register("essence_extraction") {
        ExtractionRecipe.Type
    }
    val COMBINATION = register("essence_combination") {
        CombinationRecipe.Type
    }
}
