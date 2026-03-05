package meow.illina.hexssence

import net.minecraft.resources.ResourceLocation
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import meow.illina.hexssence.config.HexssenceServerConfig
import meow.illina.hexssence.networking.HexssenceNetworking
import meow.illina.hexssence.registry.HexssenceActions
import meow.illina.hexssence.registry.HexssenceBlockEntities
import meow.illina.hexssence.registry.HexssenceBlocks
import meow.illina.hexssence.registry.HexssenceCreativeTabs
import meow.illina.hexssence.registry.HexssenceItems
import meow.illina.hexssence.registry.HexssenceRecipeSerializers
import meow.illina.hexssence.registry.HexssenceRecipeTypes

object Hexssence {
    const val MODID = "hexssence"

    @JvmField
    val LOGGER: Logger = LogManager.getLogger(MODID)

    @JvmStatic
    fun id(path: String) = ResourceLocation(MODID, path)

    fun init() {
        HexssenceServerConfig.init()
        initRegistries(
            HexssenceBlocks,
            HexssenceBlockEntities,
            HexssenceItems,
            HexssenceActions,
            HexssenceCreativeTabs,
            HexssenceRecipeTypes,
            HexssenceRecipeSerializers,
        )
        HexssenceNetworking.init()
    }

    fun initServer() {
        HexssenceServerConfig.initServer()
    }
}
