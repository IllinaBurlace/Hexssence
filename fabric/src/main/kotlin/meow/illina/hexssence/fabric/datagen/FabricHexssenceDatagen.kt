package meow.illina.hexssence.fabric.datagen

import meow.illina.hexssence.datagen.tags.HexssenceActionTags
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

object FabricHexssenceDatagen : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(gen: FabricDataGenerator) {
        val pack = gen.createPack()

        pack.addProvider(::HexssenceActionTags)
    }
}
