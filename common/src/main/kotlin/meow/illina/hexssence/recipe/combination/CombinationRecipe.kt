package meow.illina.hexssence.recipe.combination

import meow.illina.hexssence.recipe.container.BaseContainer
import meow.illina.hexssence.recipe.SpellRecipe
import meow.illina.hexssence.recipe.SpellRecipeParams
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

class CombinationRecipe(params: SpellRecipeParams) : SpellRecipe(
    params = params,
    Type,
    CombinationRecipeSerializer
) {
    object Type: RecipeType<CombinationRecipe>

    override fun matches(container: BaseContainer, level: Level): Boolean {
        if (container.isEmpty) return false
        var success = true
        params.ingredients.forEach { ingredient ->
            var has = false
            container.stacks.forEach { stack ->
                has = has
                        || (ingredient.test(stack) && ingredient.count == stack.count)
            }
            success = success && has
        }
        return success
    }
}