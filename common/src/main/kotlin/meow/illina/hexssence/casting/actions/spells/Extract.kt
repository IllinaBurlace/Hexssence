package meow.illina.hexssence.casting.actions.spells

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getItemEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import meow.illina.hexssence.recipe.container.BaseContainer
import meow.illina.hexssence.recipe.extraction.ExtractionRecipe
import net.minecraft.network.chat.Component
import net.minecraft.world.Containers
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack

object Extract : SpellAction {
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val target = args.getItemEntity(0, argc)
        env.assertEntityInRange(target)

        val recipe = env.world.recipeManager
            .getRecipeFor(
                ExtractionRecipe.Type,
                BaseContainer.of(target.item),
                env.world
            ).orElseThrow { MishapBadEntity(
                target,
                Component.translatable("hexssence.mishaps.extraction.valid_target")
            ) }

        return SpellAction.Result(
            Spell(target, recipe),
            recipe.params.mediaCost,
            listOf(ParticleSpray.cloud(target.position(), 1.0))
        )
    }

    private data class Spell(
        val item: ItemEntity,
        val recipe: ExtractionRecipe
    ) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            val pos = item.position()
            val count = item.item.count
            val cost = recipe.params.ingredients[0].count

            var rem = ItemStack.EMPTY
            if (count % cost != 0) rem = ItemStack(item.item.item, count % cost)
            item.makeFakeItem()

            recipe.rollResults(count / cost).forEach { res ->
                Containers.dropItemStack(
                    env.world,
                    pos.x,
                    pos.y,
                    pos.z,
                    res
                )
            }

            Containers.dropItemStack(
                env.world,
                pos.x,
                pos.y,
                pos.z,
                rem
            )
        }
    }
}