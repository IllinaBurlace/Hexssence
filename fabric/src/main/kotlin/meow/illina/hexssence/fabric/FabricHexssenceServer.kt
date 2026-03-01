package meow.illina.hexssence.fabric

import meow.illina.hexssence.Hexssence
import net.fabricmc.api.DedicatedServerModInitializer

object FabricHexssenceServer : DedicatedServerModInitializer {
    override fun onInitializeServer() {
        Hexssence.initServer()
    }
}
