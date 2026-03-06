package meow.illina.hexssence.recipe.combination

import meow.illina.hexssence.recipe.SpellRecipeBuilder
import meow.illina.hexssence.recipe.SpellRecipeSerializer

object CombinationRecipeSerializer : SpellRecipeSerializer<CombinationRecipe>(
    SpellRecipeBuilder.SpellRecipeFactory { params -> CombinationRecipe(params)}
)