package meow.illina.hexssence.casting.iota

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.Recipe

// Ripped from https://github.com/miyucomics/hexcellular/blob/main/src/main/java/miyucomics/hexcellular/PropertyIota.kt

class RecipeIota(recipe: ResourceLocation) : Iota(TYPE, recipe) {
    override fun isTruthy() = true
    override fun toleratesOther(that: Iota) = typesMatch(this, that) && this.recipe == (that as RecipeIota).recipe
    val recipe = payload as ResourceLocation

    override fun serialize(): Tag {
        val nbt = CompoundTag()
        nbt.putString("recipe", recipe.toString())
        return nbt
    }

    companion object {
        const val COLOR = 0x6a4ad4
        @JvmField
        val TYPE: IotaType<RecipeIota> = object : IotaType<RecipeIota>() {
            override fun deserialize(tag: Tag, world: ServerLevel) = RecipeIota(ResourceLocation((tag as CompoundTag).getString("recipe")))
            override fun display(tag: Tag): Component = Component.literal(
                (tag as CompoundTag).getString("recipe").split(':', '/').last())
                .withStyle(Style.EMPTY.withColor(COLOR))
            override fun color(): Int = COLOR
        }
    }
}

fun List<Iota>.getRecipe(idx: Int, argc: Int, world: ServerLevel): Recipe<*> {
    val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
    if (x is RecipeIota) {
        return world.recipeManager.byKey(x.recipe).orElseThrow { MishapInvalidIota(x, argc - idx + 1, Component.translatable("hexssence.mishaps.recipe.valid")) }
    }
    throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "recipe")
}
