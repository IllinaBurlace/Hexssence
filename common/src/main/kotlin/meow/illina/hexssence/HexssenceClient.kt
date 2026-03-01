package meow.illina.hexssence

import meow.illina.hexssence.config.HexssenceClientConfig
import me.shedaniel.autoconfig.AutoConfig
import net.minecraft.client.gui.screens.Screen

object HexssenceClient {
    fun init() {
        HexssenceClientConfig.init()
    }

    fun getConfigScreen(parent: Screen): Screen {
        return AutoConfig.getConfigScreen(HexssenceClientConfig.GlobalConfig::class.java, parent).get()
    }
}
