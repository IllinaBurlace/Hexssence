package meow.illina.hexssence.recipe

import at.petrak.hexcasting.api.misc.MediaConstants
import meow.illina.hexssence.recipe.utils.ChancedResult
import meow.illina.hexssence.recipe.utils.IngredientCounted
import net.minecraft.core.NonNullList
import net.minecraft.resources.ResourceLocation

class SpellRecipeParams(
    val id: ResourceLocation,
) {
    var ingredients: NonNullList<IngredientCounted> = NonNullList.create()
    var results: NonNullList<ChancedResult> = NonNullList.create()
    var mediaCost = MediaConstants.SHARD_UNIT
}