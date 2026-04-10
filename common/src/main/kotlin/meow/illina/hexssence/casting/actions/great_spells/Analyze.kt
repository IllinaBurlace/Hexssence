package meow.illina.hexssence.casting.actions.great_spells

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.getItemEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import meow.illina.hexssence.casting.iota.RecipeIota
import meow.illina.hexssence.recipe.combination.CombinationRecipe
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.item.ItemEntity
import kotlin.collections.plus

object Analyze : SpellAction {
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val target = args.getItemEntity(0, argc)
        val recipe = env.world.recipeManager.getAllRecipesFor(CombinationRecipe.Type).find {
            it.params.results.find { res ->
                res.item.item == target.item.item
            } != null
        }
        if (recipe == null)
            throw MishapInvalidIota(args[0], 0, Component.translatable("hexssence.mishaps.analyze.valid_item"))

        return SpellAction.Result(
            Spell(recipe.id!!, target),
            MediaConstants.CRYSTAL_UNIT * 5,
            listOf()
        )
    }

    private data class Spell(
        val recipe: ResourceLocation,
        val target: ItemEntity
    ) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {}
        override fun cast(env: CastingEnvironment, image: CastingImage): CastingImage {
            target.makeFakeItem()
            return image.copy(stack = image.stack.plus(RecipeIota(recipe)))
        }
    }
}