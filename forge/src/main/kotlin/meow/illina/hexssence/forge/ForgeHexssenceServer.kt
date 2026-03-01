package meow.illina.hexssence.forge

import meow.illina.hexssence.Hexssence
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent

object ForgeHexssenceServer {
    @Suppress("UNUSED_PARAMETER")
    fun init(event: FMLDedicatedServerSetupEvent) {
        Hexssence.initServer()
    }
}
