package meow.illina.hexssence.casting.actions.spells

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import meow.illina.hexssence.blocks.jar.EssenceJarBlockEntity
import meow.illina.hexssence.recipe.combination.CombinationRecipe
import meow.illina.hexssence.recipe.container.BaseContainer
import net.minecraft.core.BlockPos
import net.minecraft.core.NonNullList
import net.minecraft.network.chat.Component
import net.minecraft.world.Containers
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.minecraft.world.phys.Vec3
import kotlin.math.floor
import kotlin.math.sign

object CombineVecs : SpellAction{
    override val argc = 3
    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val vecs = args.getList(0, argc)
        val counts = args.getList(1, argc)
        val outPos = args.getVec3(2, argc)

        counts.forEach { iota ->
            if (iota !is DoubleIota || iota.double.sign == -1.0) MishapInvalidIota(args[1], 1, Component.translatable("hexssence.mishaps.combine_vecs.valid_recipe"))
        }

        val jars = NonNullList.create<EssenceJarBlockEntity>()
        jars.addAll(vecs.mapIndexed { index, vec ->
            if (vec !is Vec3Iota)
                throw MishapInvalidIota(args[0], 2, Component.translatable("hexssence.mishaps.combine_vecs.valid_list"))
            env.assertVecInRange(vec.vec3)
            val v = vec.vec3
            val blockPos = BlockPos(floor(v.x).toInt(), floor(v.y).toInt(), floor(v.z).toInt())
            val bE = env.world.getBlockEntity(blockPos)
            if (bE is EssenceJarBlockEntity && bE.count >= (counts.toList()[index] as DoubleIota).double.toInt())
                return@mapIndexed bE
            else
                throw MishapInvalidIota(args[0], 2, Component.translatable("hexssence.mishaps.combine_vecs.valid_list"))
        })

        val ratio = NonNullList.create<ItemStack>()
        ratio.addAll(jars.mapIndexed { index, jar ->
            ItemStack(jar.storedItem, (counts.toList()[index] as DoubleIota).double.toInt())
        })

        val recipe = env.world.recipeManager
            .getRecipeFor(
                CombinationRecipe.Type,
                BaseContainer.of(ratio),
                env.world
            ).orElseThrow { MishapInvalidIota(args[1], 1, Component.translatable("hexssence.mishaps.combine_vecs.valid_recipe")) }

        return SpellAction.Result(
            Spell(jars, recipe, outPos),
            recipe.params.mediaCost,
            listOf(ParticleSpray.cloud(outPos, 1.0))
        )
    }
    private data class Spell(
        val jars: NonNullList<EssenceJarBlockEntity>,
        val recipe: CombinationRecipe,
        val outPos: Vec3,
    ) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            jars.forEachIndexed {index, jar ->
                jar.count -= recipe.params.ingredients[index].count
                // will literally never be less because above check would fail otherwise
                if (jar.count == 0) jar.storedItem = Items.AIR
                jar.setChanged()
                env.world.sendBlockUpdated(jar.blockPos, jar.blockState, jar.blockState, Block.UPDATE_ALL)
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