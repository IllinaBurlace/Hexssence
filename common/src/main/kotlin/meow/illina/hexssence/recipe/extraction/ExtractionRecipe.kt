package meow.illina.hexssence.recipe.extraction

import meow.illina.hexssence.blocks.container.BaseContainer
import meow.illina.hexssence.recipe.SpellRecipe
import meow.illina.hexssence.recipe.SpellRecipeParams
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

class ExtractionRecipe(params: SpellRecipeParams) : SpellRecipe(
    params = params,
    Type,
    ExtractionRecipeSerializer
) {
    object Type : RecipeType<ExtractionRecipe>

    override fun matches(container: BaseContainer, level: Level): Boolean {
        if (container.isEmpty) return false
        return params.ingredients[0]
            .testWithCount(container.getItem(0))
    }
}