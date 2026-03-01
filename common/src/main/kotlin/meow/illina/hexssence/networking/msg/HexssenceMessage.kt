package meow.illina.hexssence.networking.msg

import dev.architectury.networking.NetworkChannel
import dev.architectury.networking.NetworkManager.PacketContext
import meow.illina.hexssence.Hexssence
import meow.illina.hexssence.networking.HexssenceNetworking
import meow.illina.hexssence.networking.handler.applyOnClient
import meow.illina.hexssence.networking.handler.applyOnServer
import net.fabricmc.api.EnvType
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.server.level.ServerPlayer
import java.util.function.Supplier

sealed interface HexssenceMessage

sealed interface HexssenceMessageC2S : HexssenceMessage {
    fun sendToServer() {
        HexssenceNetworking.CHANNEL.sendToServer(this)
    }
}

sealed interface HexssenceMessageS2C : HexssenceMessage {
    fun sendToPlayer(player: ServerPlayer) {
        HexssenceNetworking.CHANNEL.sendToPlayer(player, this)
    }

    fun sendToPlayers(players: Iterable<ServerPlayer>) {
        HexssenceNetworking.CHANNEL.sendToPlayers(players, this)
    }
}

sealed interface HexssenceMessageCompanion<T : HexssenceMessage> {
    val type: Class<T>

    fun decode(buf: FriendlyByteBuf): T

    fun T.encode(buf: FriendlyByteBuf)

    fun apply(msg: T, supplier: Supplier<PacketContext>) {
        val ctx = supplier.get()
        when (ctx.env) {
            EnvType.SERVER, null -> {
                Hexssence.LOGGER.debug("Server received packet from {}: {}", ctx.player.name.string, this)
                when (msg) {
                    is HexssenceMessageC2S -> msg.applyOnServer(ctx)
                    else -> Hexssence.LOGGER.warn("Message not handled on server: {}", msg::class)
                }
            }
            EnvType.CLIENT -> {
                Hexssence.LOGGER.debug("Client received packet: {}", this)
                when (msg) {
                    is HexssenceMessageS2C -> msg.applyOnClient(ctx)
                    else -> Hexssence.LOGGER.warn("Message not handled on client: {}", msg::class)
                }
            }
        }
    }

    fun register(channel: NetworkChannel) {
        channel.register(type, { msg, buf -> msg.encode(buf) }, ::decode, ::apply)
    }
}
