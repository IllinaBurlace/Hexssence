package meow.illina.hexssence.blocks.jar

import at.petrak.hexcasting.api.block.HexBlockEntity
import meow.illina.hexssence.api.HexssenceTags
import meow.illina.hexssence.datagen.tags.HexssenceItemTags
import meow.illina.hexssence.registry.HexssenceBlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.state.BlockState
import kotlin.math.min

class EssenceJarBlockEntity(pos: BlockPos, state: BlockState) :
    HexBlockEntity(HexssenceBlockEntities.ESSENCE_JAR.value, pos, state)
{
    var storedItem: Item = Items.AIR
    var count: Int = 0
    var lastClickTime: Int = 0

    fun insert(player: Player, hand: InteractionHand): InteractionResult {
        if (hand == InteractionHand.MAIN_HAND && lastClickTime < 10) return allStacks(player)
        lastClickTime = 0

        val stack = player.getItemInHand(hand)
        if ((storedItem != Items.AIR && !stack.`is`(storedItem))
            || stack == ItemStack.EMPTY
            || !stack.`is`(HexssenceTags.Items.VALID_ESSENCE)
        ) return InteractionResult.FAIL


        storedItem = stack.item
        count += stack.copyAndClear().count
        setChanged()

        return if (hand == InteractionHand.MAIN_HAND) InteractionResult.SUCCESS else InteractionResult.PASS
    }

    fun allStacks(player: Player): InteractionResult {
        lastClickTime = 0
        val inventory = player.inventory
        var success = false
        for (slot in 0 .. inventory.containerSize) {
            val stack = inventory.getItem(slot)
            if (stack.isEmpty || !stack.`is`(storedItem)) continue
            count += stack.copyAndClear().count
            success = true
        }
        setChanged()
        return if (success) InteractionResult.SUCCESS else InteractionResult.FAIL
    }

    fun extract(count: Int): ItemStack {
        // practically never going to desync but its barely more characters so might as well check
        if (this.count == 0 || storedItem == Items.AIR) return ItemStack.EMPTY

        val out = min(count, this.count)
        val outStack = ItemStack(storedItem, out)

        this.count -= out
        if (this.count == 0) storedItem = Items.AIR
        setChanged()

        return outStack
    }

    override fun saveModData(tag: CompoundTag?) {
        tag!!.putString("stored", BuiltInRegistries.ITEM.getKey(storedItem).toString())
        tag.putInt("count", count)
        setChanged()
    }

    override fun loadModData(tag: CompoundTag?) {
        storedItem = BuiltInRegistries.ITEM.get(ResourceLocation(tag!!.getString("stored")))
        count = tag.getInt("count")
        setChanged()
    }

    override fun getUpdateTag(): CompoundTag = saveWithoutMetadata()

    fun tick() {
        if (lastClickTime < 10) lastClickTime++
    }
}