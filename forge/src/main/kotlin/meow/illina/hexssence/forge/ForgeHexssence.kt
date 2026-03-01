package meow.illina.hexssence.forge

import dev.architectury.platform.forge.EventBuses
import meow.illina.hexssence.Hexssence
import meow.illina.hexssence.forge.datagen.ForgeHexssenceDatagen
import net.minecraftforge.fml.common.Mod
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(Hexssence.MODID)
class ForgeHexssence {
    init {
        MOD_BUS.apply {
            EventBuses.registerModEventBus(Hexssence.MODID, this)
            addListener(ForgeHexssenceClient::init)
            addListener(ForgeHexssenceDatagen::init)
            addListener(ForgeHexssenceServer::init)
        }
        Hexssence.init()
    }
}
