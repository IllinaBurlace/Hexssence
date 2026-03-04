package meow.illina.hexssence.datagen.tags

import meow.illina.hexssence.api.HexssenceTags
import meow.illina.hexssence.registry.HexssenceItems
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.world.item.Item
import java.util.concurrent.CompletableFuture

class HexssenceItemTags(output: PackOutput, provider: CompletableFuture<HolderLookup.Provider>)
    : TagsProvider<Item>(output, Registries.ITEM, provider)
{
    override fun addTags(provider: HolderLookup.Provider) {
        tag(HexssenceTags.Items.VALID_ESSENCE).add(
            HexssenceItems.ESSENCE_HEX.key,
            HexssenceItems.ESSENCE_EARTH.key,
            HexssenceItems.ESSENCE_AIR.key,
            HexssenceItems.ESSENCE_WATER.key,
            HexssenceItems.ESSENCE_FIRE.key,
            HexssenceItems.ESSENCE_METAL.key,
            HexssenceItems.ESSENCE_PRECIOUS.key,
            HexssenceItems.ESSENCE_LIFE.key,
            HexssenceItems.ESSENCE_END.key,
        )
    }
}