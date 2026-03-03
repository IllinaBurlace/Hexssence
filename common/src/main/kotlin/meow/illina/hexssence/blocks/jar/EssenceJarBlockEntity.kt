package meow.illina.hexssence.blocks.jar

import at.petrak.hexcasting.api.block.HexBlockEntity
import meow.illina.hexssence.registry.HexssenceBlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import kotlin.math.min

class EssenceJarBlockEntity(pos: BlockPos, state: BlockState) :
    HexBlockEntity(HexssenceBlockEntities.ESSENCE_JAR.value, pos, state)
{
    var storedItem: Item = Items.AIR
    var count: Int = 0
    var lastClickTime: Int = 0

    fun insert(stack: ItemStack): Boolean {
        if (storedItem != Items.AIR && stack.item != storedItem) return false
        storedItem = stack.item
        count += stack.count
        stack.copyAndClear()
        setChanged()
        return true
    }

    fun extract(count: Int): ItemStack {
        // practically never going to desync but its barely more characters so might as well check
        if (this.count == 0 || storedItem == Items.AIR) return ItemStack.EMPTY
        val out = min(count, this.count)
        val outStack = ItemStack(storedItem, out)
        this.count -= out
        setChanged()
        return outStack
    }

    override fun saveModData(tag: CompoundTag?) {
        tag!!.putString("stored", BuiltInRegistries.ITEM.getKey(storedItem).toString())
        tag.putInt("count", count)
    }

    override fun loadModData(tag: CompoundTag?) {
        storedItem = BuiltInRegistries.ITEM.get(ResourceLocation(tag!!.getString("stored")))
        count = tag.getInt("count")
    }

    override fun getUpdateTag(): CompoundTag = saveWithoutMetadata()

    fun tick() {
        if (lastClickTime < 10) lastClickTime++
    }
}