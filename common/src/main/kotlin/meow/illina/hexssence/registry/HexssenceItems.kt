package meow.illina.hexssence.registry

import meow.illina.hexssence.registry.HexssenceCreativeTabs.HEXSSENCE
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike

object HexssenceItems : HexssenceRegistrar<Item>(
    Registries.ITEM,
    { BuiltInRegistries.ITEM }
) {
    val props: Item.Properties = Item.Properties().`arch$tab`(HEXSSENCE.key)
    @JvmField
    val ESSENCE_HEX = item("essence_hex") {
        Item(props)
    }
    @JvmField
    val ESSENCE_EARTH = item("essence_earth") {
        Item(props)
    }
    @JvmField
    val ESSENCE_AIR = item("essence_air") {
        Item(props)
    }
    @JvmField
    val ESSENCE_WATER = item("essence_water") {
        Item(props)
    }
    @JvmField
    val ESSENCE_FIRE = item("essence_fire") {
        Item(props)
    }
    @JvmField
    val ESSENCE_METAL = item("essence_metal") {
        Item(props)
    }
    @JvmField
    val ESSENCE_PRECIOUS = item("essence_precious") {
        Item(props)
    }
    @JvmField
    val ESSENCE_LIFE = item("essence_life") {
        Item(props)
    }
    @JvmField
    val ESSENCE_END = item("essence_end") {
        Item(props)
    }


    private fun <V : Item> item(name: String, builder: () -> V) = ItemEntry(register(name, builder))

    class ItemEntry<V : Item>(entry: Entry<V>) : Entry<V>(entry), ItemLike {
        override fun asItem() = value
    }
}
