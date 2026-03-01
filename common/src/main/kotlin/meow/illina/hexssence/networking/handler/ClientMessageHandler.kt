package meow.illina.hexssence.networking.handler

import dev.architectury.networking.NetworkManager.PacketContext
import meow.illina.hexssence.config.HexssenceServerConfig
import meow.illina.hexssence.networking.msg.*

fun HexssenceMessageS2C.applyOnClient(ctx: PacketContext) = ctx.queue {
    when (this) {
        is MsgSyncConfigS2C -> {
            HexssenceServerConfig.onSyncConfig(serverConfig)
        }

        // add more client-side message handlers here
    }
}
