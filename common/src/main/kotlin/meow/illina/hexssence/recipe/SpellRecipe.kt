package meow.illina.hexssence.recipe

import meow.illina.hexssence.recipe.container.BaseContainer
import meow.illina.hexssence.recipe.utils.ChancedResult
import net.minecraft.core.RegistryAccess
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

abstract class SpellRecipe(
    val params: SpellRecipeParams,
    val recipeType: RecipeType<*>,
    val recipeSerializer: RecipeSerializer<*>,
) : Recipe<BaseContainer> {
    fun rollResults(times: Int): List<ItemStack> {
        return rollResults(params.results, times)
    }

    companion object {
        fun rollResults(rollable: List<ChancedResult>, times: Int): List<ItemStack> {
            val results = mutableListOf<ItemStack>()
            rollable.forEach {res ->
                val stack = res.rollOutput(times)
                if (!stack.isEmpty) results.add(stack)
            }
            return results
        }
    }

    // busywork

    override fun assemble(container: BaseContainer, registryAccess: RegistryAccess): ItemStack {
        return getResultItem(registryAccess)
    }

    override fun canCraftInDimensions(width: Int, height: Int): Boolean {
        return true
    }

    override fun getResultItem(registryAccess: RegistryAccess): ItemStack {
        return if (params.results.isEmpty()) ItemStack.EMPTY
            else params.results[0].item
    }

    override fun isSpecial(): Boolean {
        return true
    }

    override fun getGroup(): String {
        return "spell_recipes"
    }

    override fun getId(): ResourceLocation? {
        return params.id
    }

    override fun getSerializer(): RecipeSerializer<*>? {
        return recipeSerializer
    }

    override fun getType(): RecipeType<*>? {
        return recipeType
    }
}
