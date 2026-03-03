package meow.illina.hexssence.registry

import meow.illina.hexssence.blocks.jar.EssenceJarBlock
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block

object HexssenceBlocks : HexssenceRegistrar<Block>(
    Registries.BLOCK,
    { BuiltInRegistries.BLOCK }
) {
    @JvmField
    val ESSENCE_JAR = block("essence_jar", HexssenceItems.props) {
        EssenceJarBlock()
    }

    private fun <V : Block> block(name: String, props: Item.Properties, builder:() -> V) =
        block(name, builder) { BlockItem(it, props) }

    private fun <B: Block, I: Item> block(
        name: String,
        blockBuilder: () -> B,
        itemBuilder: (B) -> I,
    ): BlockItemEntry<B, I> {
        val blockEntry = register(name, blockBuilder)
        val itemEntry = HexssenceItems.register(name) { itemBuilder(blockEntry.value) }
        return BlockItemEntry(blockEntry, itemEntry)
    }

    class BlockItemEntry<B: Block, I: Item>(
        blockEntry: Entry<B>,
        val itemEntry: HexssenceRegistrar<Item>.Entry<I>,
    ) : Entry<B>(blockEntry), ItemLike {
        val block by ::value
        val item by itemEntry::value
        val itemKey by itemEntry::key

        override fun asItem() = item
    }
}