package meow.illina.hexssence

import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry
import meow.illina.hexssence.config.HexssenceClientConfig
import me.shedaniel.autoconfig.AutoConfig
import meow.illina.hexssence.registry.HexssenceBlockEntities.ESSENCE_JAR
import meow.illina.hexssence.render.jar.EssenceJarBlockEntityRenderer
import net.minecraft.client.gui.screens.Screen

object HexssenceClient {
    fun init() {
        HexssenceClientConfig.init()
        BlockEntityRendererRegistry.register(ESSENCE_JAR.value, ::EssenceJarBlockEntityRenderer)
    }

    fun getConfigScreen(parent: Screen): Screen {
        return AutoConfig.getConfigScreen(HexssenceClientConfig.GlobalConfig::class.java, parent).get()
    }
}
