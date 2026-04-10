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
import meow.illina.hexssence.recipe.utils.IngredientCounted
import net.minecraft.core.BlockPos
import net.minecraft.core.NonNullList
import net.minecraft.network.chat.Component
import net.minecraft.world.Containers
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.phys.Vec3
import kotlin.math.floor

object Combine : SpellAction{
    override val argc = 3
    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val vecList = args.getList(0, argc)
        val recipe = args.getRecipe(1, argc, env.world) as? CombinationRecipe
            ?: throw MishapInvalidIota(args[1], 1, Component.translatable("hexssence.mishaps.combine.valid_recipe"))
        val outPos = args.getVec3(2, argc)

        val badList = MishapInvalidIota(args[0], 2, Component.translatable("hexssence.mishaps.combine.valid_list"))

        val jars = NonNullList.create<EssenceJarBlockEntity>()
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
            jars.add(bE)
        }

        val input = NonNullList.create<Pair<EssenceJarBlockEntity, IngredientCounted>>()
        val used = NonNullList.create<IngredientCounted>()

        jars.forEach { jar ->
            var ing = IngredientCounted.EMPTY
            recipe.params.ingredients.forEach { ingredient ->
                ing = pass(ing, ingredient, jar, used)
            }
            if (ing != IngredientCounted.EMPTY) {
                used.add(ing)
                input.add(Pair(jar, ing))
            }
        }

        if (input.size != recipe.params.ingredients.size) {
            throw badList
        }
        input.forEach { pair ->
            if (pair.first.count < pair.second.count) {
                throw MishapInvalidIota(args[0], 2, Component.translatable("hexssence.mishaps.combine.not_enough_items"))
            }
        }

        return SpellAction.Result(
            Spell(input, recipe, outPos),
            recipe.params.mediaCost,
            listOf(ParticleSpray.cloud(outPos, 1.0)),
        )

    }
    private data class Spell(
        val jars: NonNullList<Pair<EssenceJarBlockEntity, IngredientCounted>>,
        val recipe: CombinationRecipe,
        val outPos: Vec3,
    ) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            jars.forEach { pair ->
                pair.first.extract(pair.second.count)
                env.world.sendBlockUpdated(
                    pair.first.blockPos,
                    pair.first.blockState,
                    pair.first.blockState,
                    Block.UPDATE_ALL
                )
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

    fun pass(
        ing: IngredientCounted,
        test: IngredientCounted,
        jar: EssenceJarBlockEntity,
        used: NonNullList<IngredientCounted>
    ) : IngredientCounted {
        if (ing != IngredientCounted.EMPTY)
            return ing
        if (used.contains(test))
            return ing
        if (test.test(ItemStack(jar.storedItem)))
            return test
        return ing
    }
}