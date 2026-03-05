package meow.illina.hexssence.recipe.utils

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.minecraft.core.registries.Registries
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.tags.TagKey
import net.minecraft.util.GsonHelper
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient

class IngredientCounted {
    var ingredient: Ingredient? = null
    var tag: TagKey<Item>? = null
    var count = 1

    constructor(ingredient: Ingredient) {
        this.ingredient = ingredient
    }
    constructor(ingredient: Ingredient, count: Int) {
        this.ingredient = ingredient
        this.count = count
    }
    constructor(tag: TagKey<Item>) {
        this.tag = tag
    }
    constructor(tag: TagKey<Item>, count: Int) {
        this.tag = tag
        this.count = count
    }

    fun test(item: Item): Boolean {
        return(test(ItemStack(item)))
    }

    fun test(item: ItemStack): Boolean {
        if (tag == null) return ingredient!!.test(item)
        return item.`is`(tag!!)
    }

    fun testWithCount(item: ItemStack): Boolean {
        return test(item) && item.count >= count
    }

    fun toNetwork(buf: FriendlyByteBuf) {
        toNetwork(buf, this)
    }

    companion object {
        fun fromJson(json: JsonObject): IngredientCounted {
            val count = GsonHelper.getAsInt(json, "count", 1)
            return IngredientCounted(Ingredient.fromJson(json), count)
        }

        fun fromJson(json: JsonElement): IngredientCounted {
            return fromJson(json.asJsonObject)
        }

        fun fromNetwork(buf: FriendlyByteBuf): IngredientCounted {
            if (!buf.readBoolean()) {
                return IngredientCounted(Ingredient.fromNetwork(buf), buf.readInt())
            }
            return IngredientCounted(
                TagKey.create(Registries.ITEM, buf.readResourceLocation()),
                buf.readInt()
            )
        }

        fun toNetwork(buf: FriendlyByteBuf, ingredient: IngredientCounted) {
            buf.writeBoolean(ingredient.tag == null)
            if (ingredient.tag == null)
                ingredient.ingredient!!.toNetwork(buf)
            else {
                val tag = ingredient.tag!!
                buf.writeResourceLocation(tag.location)
            }
            buf.writeInt(ingredient.count)
        }
    }
}