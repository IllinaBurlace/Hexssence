package meow.illina.hexssence.recipe.extraction

import meow.illina.hexssence.recipe.SpellRecipeBuilder
import meow.illina.hexssence.recipe.SpellRecipeSerializer

object ExtractionRecipeSerializer : SpellRecipeSerializer<ExtractionRecipe>(
    SpellRecipeBuilder.SpellRecipeFactory {params -> ExtractionRecipe(params)}
)