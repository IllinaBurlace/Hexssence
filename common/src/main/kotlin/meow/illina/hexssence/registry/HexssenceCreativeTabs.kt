package meow.illina.hexssence.registry

import dev.architectury.registry.CreativeTabRegistry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack

object HexssenceCreativeTabs : HexssenceRegistrar<CreativeModeTab>(
    Registries.CREATIVE_MODE_TAB,
    { BuiltInRegistries.CREATIVE_MODE_TAB }
) {
    val HEXSSENCE = make("hexssence") {
        icon { ItemStack(HexssenceItems.ESSENCE_HEX) }
        displayItems { _, output ->

        }
    }

    @Suppress("SameParameterValue")
    private fun make(name: String, action: CreativeModeTab.Builder.() -> Unit) = register(name) {
        CreativeTabRegistry.create { builder ->
            builder.title(Component.translatable("itemGroup.$name"))
            action.invoke(builder)
        }
    }
}