package meow.illina.hexssence.fabric

import meow.illina.hexssence.HexssenceClient
import net.fabricmc.api.ClientModInitializer

object FabricHexssenceClient : ClientModInitializer {
    override fun onInitializeClient() {
        HexssenceClient.init()
    }
}
