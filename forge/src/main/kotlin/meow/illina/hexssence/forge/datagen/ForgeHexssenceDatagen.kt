package meow.illina.hexssence.forge.datagen

import at.petrak.hexcasting.forge.datagen.TagsProviderEFHSetter
import meow.illina.hexssence.datagen.tags.HexssenceActionTags
import meow.illina.hexssence.datagen.tags.HexssenceItemTags
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.minecraftforge.data.event.GatherDataEvent

object ForgeHexssenceDatagen {
    fun init(event: GatherDataEvent) {
        event.apply {
            // common datagen
            if (System.getProperty("hexssence.common-datagen") == "true") {
                addProvider(includeServer()) { HexssenceItemTags(it, lookupProvider) }
            }

            // Forge-only datagen
            if (System.getProperty("hexssence.forge-datagen") == "true") {
                addVanillaProvider(includeServer()) { HexssenceActionTags(it, lookupProvider) }
            }
        }
    }
}

private fun <T : DataProvider> GatherDataEvent.addProvider(run: Boolean, factory: (PackOutput) -> T) =
    generator.addProvider(run, DataProvider.Factory { factory(it) })

private fun <T : DataProvider> GatherDataEvent.addVanillaProvider(run: Boolean, factory: (PackOutput) -> T) =
    addProvider(run) { packOutput ->
        factory(packOutput).also {
            (it as TagsProviderEFHSetter).setEFH(existingFileHelper)
        }
    }
