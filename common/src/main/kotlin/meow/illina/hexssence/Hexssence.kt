package meow.illina.hexssence

import net.minecraft.resources.ResourceLocation
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import meow.illina.hexssence.config.HexssenceServerConfig
import meow.illina.hexssence.networking.HexssenceNetworking
import meow.illina.hexssence.registry.HexssenceActions

object Hexssence {
    const val MODID = "hexssence"

    @JvmField
    val LOGGER: Logger = LogManager.getLogger(MODID)

    @JvmStatic
    fun id(path: String) = ResourceLocation(MODID, path)

    fun init() {
        HexssenceServerConfig.init()
        initRegistries(
            HexssenceActions,
        )
        HexssenceNetworking.init()
    }

    fun initServer() {
        HexssenceServerConfig.initServer()
    }
}
