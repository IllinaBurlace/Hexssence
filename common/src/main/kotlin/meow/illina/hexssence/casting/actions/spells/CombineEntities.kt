package meow.illina.hexssence.casting.actions.spells

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import meow.illina.hexssence.recipe.combination.CombinationRecipe
import meow.illina.hexssence.recipe.container.BaseContainer
import net.minecraft.core.NonNullList
import net.minecraft.network.chat.Component
import net.minecraft.world.Containers
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.phys.Vec3

object CombineEntities : SpellAction {
    override val argc = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val targets = args.getList(0, argc)
        val outPos = args.getVec3(1, argc)

        env.assertVecInRange(outPos)

        targets.forEach { target ->
            if (target !is EntityIota || target.entity !is ItemEntity)
                throw MishapInvalidIota(args[0], 1, Component.translatable("hexssence.mishaps.combine_entities.valid_list"))
            env.assertEntityInRange(target.entity)
        }

        val coll = NonNullList.create<ItemEntity>()
        coll.addAll(targets.map { (it as EntityIota).entity as ItemEntity })

        val recipe = env.world.recipeManager
            .getRecipeFor(
                CombinationRecipe.Type,
                BaseContainer.of(coll.map { i -> i.item }),
                env.world
            ).orElseThrow {
                MishapInvalidIota(
                    args[0],
                    1,
                    Component.translatable("hexssence.combine_entities.valid_list")
                ) }

        return SpellAction.Result(
            Spell(coll, recipe, outPos),
            recipe.params.mediaCost,
            listOf(ParticleSpray.cloud(outPos, 1.0))
        )
    }

    private data class Spell (
        val coll: NonNullList<ItemEntity>,
        val recipe: CombinationRecipe,
        val outPos: Vec3
    ) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            coll.forEach { e -> e.makeFakeItem() }
            recipe.rollResults(1).forEach { res ->
                Containers.dropItemStack(
                    env.world,
                    outPos.x,
                    outPos.y,
                    outPos.z,
                    res
                )
            }
        }
    }
}