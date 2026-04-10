package meow.illina.hexssence.casting.actions.spells

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import meow.illina.hexssence.casting.iota.RecipeIota
import meow.illina.hexssence.recipe.combination.CombinationRecipe
import ram.talia.moreiotas.api.casting.iota.ItemTypeIota

object Decay : ConstMediaAction {
    override val argc = 1
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val iota = args[0]
        if (iota !is RecipeIota)
            throw MishapInvalidIota.ofType(iota, 0, "Recipe")

        val recipe = env.world.recipeManager
            .byKey(iota.recipe)
            .orElseThrow { MishapInvalidIota.ofType(iota, 0, "Recipe") }
        as CombinationRecipe

        val coll = mutableListOf<ListIota>()

        recipe.params.ingredients.forEach { ing ->
            val item = ing.ingredient.items[0]
            coll.add(ListIota(
                listOf(
                    ItemTypeIota(item.item),
                    DoubleIota(ing.count.toDouble())
                )
            ))
        }

        return listOf(ListIota(coll as List<Iota>))
    }
}