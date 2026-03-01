package meow.illina.hexssence.fabric

import meow.illina.hexssence.Hexssence
import net.fabricmc.api.ModInitializer

object FabricHexssence : ModInitializer {
    override fun onInitialize() {
        Hexssence.init()
    }
}
