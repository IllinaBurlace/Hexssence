package meow.illina.hexssence.fabric

import meow.illina.hexssence.HexssenceClient
import meow.illina.hexssence.registry.HexssenceBlocks
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.minecraft.client.renderer.RenderType

object FabricHexssenceClient : ClientModInitializer {
    override fun onInitializeClient() {
        HexssenceClient.init()
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(),
            HexssenceBlocks.ESSENCE_JAR.value
        )
    }
}
