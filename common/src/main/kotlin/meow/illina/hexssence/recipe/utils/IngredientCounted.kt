package meow.illina.hexssence.recipe.utils

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.util.GsonHelper
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient

class IngredientCounted(val ingredient: Ingredient, val count: Int) {
    fun test(item: ItemStack): Boolean {
        return ingredient.test(item)
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
            return IngredientCounted(Ingredient.fromNetwork(buf), buf.readInt())
        }

        fun toNetwork(buf: FriendlyByteBuf, ingredient: IngredientCounted) {
            ingredient.ingredient.toNetwork(buf)
            buf.writeInt(ingredient.count)
        }
    }
}