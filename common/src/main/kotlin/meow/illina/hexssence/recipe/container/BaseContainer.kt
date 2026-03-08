package meow.illina.hexssence.recipe.container

import net.minecraft.core.NonNullList
import net.minecraft.world.Container
import net.minecraft.world.ContainerHelper
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

class BaseContainer : Container {
    val stacks: NonNullList<ItemStack> = NonNullList.create()

    override fun getContainerSize() = stacks.size
    override fun isEmpty() = stacks.all { it.isEmpty }
    override fun getItem(slot: Int) = stacks[slot]
    override fun removeItem(slot: Int, amount: Int): ItemStack = ContainerHelper.removeItem(stacks, slot, amount).also {
        if (!it.isEmpty) setChanged()
    }
    override fun removeItemNoUpdate(slot: Int): ItemStack = ContainerHelper.takeItem(stacks, slot)
    override fun setItem(slot: Int, stack: ItemStack) {
        stacks[slot] = stack
    }
    override fun clearContent() {
        stacks.replaceAll { ItemStack.EMPTY }
    }
    override fun stillValid(player: Player) = false
    override fun setChanged() {}

    companion object {
        fun of(vararg items: ItemStack): BaseContainer {
            val container = BaseContainer()
            container.stacks.addAll(items)
            return container
        }
        fun of(stacks: List<ItemStack>): BaseContainer {
            val container = BaseContainer()
            container.stacks.addAll(stacks)
            return container
        }
    }
}
