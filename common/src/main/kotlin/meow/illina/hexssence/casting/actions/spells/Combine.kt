package meow.illina.hexssence.casting.actions.spells

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import meow.illina.hexssence.blocks.jar.EssenceJarBlockEntity
import meow.illina.hexssence.casting.iota.getRecipe
import meow.illina.hexssence.recipe.combination.CombinationRecipe
import net.minecraft.core.BlockPos
import net.minecraft.core.NonNullList
import net.minecraft.network.chat.Component
import net.minecraft.world.Containers
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.Vec3
import kotlin.math.floor

object Combine : SpellAction{
    override val argc = 3
    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val vecList = args.getList(0, argc)
        val recipe = args.getRecipe(1, argc, env.world) as? CombinationRecipe
            ?: throw MishapInvalidIota(args[0], 2, Component.translatable("hexssence.mishaps.combine.valid_recipe"))
        val outPos = args.getVec3(2, argc)

        val badList = MishapInvalidIota(args[0], 2, Component.translatable("hexssence.mishaps.combine.valid_list"))

        val jars = NonNullList.create<Pair<EssenceJarBlockEntity, Int>>()
        vecList.forEach { iota ->
            if (iota !is Vec3Iota)
                throw badList
            val vec = iota.vec3
            env.assertVecInRange(vec)

            val pos = BlockPos(
                floor(vec.x).toInt(),
                floor(vec.y).toInt(),
                floor(vec.z).toInt(),
            )
            val bE = env.world.getBlockEntity(pos)
            bE as? EssenceJarBlockEntity
                ?: throw badList

            var idx = -1
            recipe.params.ingredients.forEachIndexed { index, counted ->
                if (counted.testWithCount(ItemStack(bE.storedItem, bE.count)))
                    idx = index
            }
            if (idx == -1)
                throw badList

            jars.add(Pair(bE, idx))
        }

        return SpellAction.Result(
            Spell(jars, recipe, outPos),
            recipe.params.mediaCost,
            listOf(ParticleSpray.cloud(outPos, 1.0)),
        )

    }
    private data class Spell(
        val jars: NonNullList<Pair<EssenceJarBlockEntity, Int>>,
        val recipe: CombinationRecipe,
        val outPos: Vec3,
    ) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            jars.forEach { pair ->
                pair.first.count -= recipe.params.ingredients[pair.second].count
            }

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