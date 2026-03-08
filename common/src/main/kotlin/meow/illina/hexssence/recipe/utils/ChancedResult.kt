package meow.illina.hexssence.recipe.utils

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.GsonHelper
import net.minecraft.world.item.ItemStack
import java.util.Random

class ChancedResult(val item: ItemStack, val chance: Float) {
    val rand: Random = Random()

    fun rollOutput(times: Int): ItemStack {
        var count = item.count * times
        for (@Suppress("UNUSED_PARAMETER") roll in 0..times) {
            if (rand.nextFloat() > chance)
                count -= item.count
        }
        if (count == 0)
            return ItemStack.EMPTY
        return ItemStack(item.item, count)
    }

    fun toJson(): JsonElement {
        val json = JsonObject()
        val id = BuiltInRegistries.ITEM.getId(item.item)

        json.addProperty("item", id.toString())
        val count = item.count
        if (count != 1)
            json.addProperty("count", count)
        if (chance != 1f)
            json.addProperty("chance", chance)
        return json
    }

    fun toNetwork(buf: FriendlyByteBuf) {
        buf.writeItem(item)
        buf.writeFloat(chance)
    }

    companion object {
        fun fromJson(je: JsonElement): ChancedResult {
            val json = je.asJsonObject
            val id = GsonHelper.getAsString(json, "item")
            val count = GsonHelper.getAsInt(json, "count", 1)
            val chance = GsonHelper.getAsFloat(json, "chance", 1.0f)
            val stack = ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation(id)), count)

            return ChancedResult(stack, chance)
        }

        fun fromNetwork(buf: FriendlyByteBuf): ChancedResult {
            return ChancedResult(buf.readItem(), buf.readFloat())
        }

        var empty: ChancedResult = ChancedResult(ItemStack.EMPTY, 1f)
    }
}