package meow.illina.hexssence.networking

import dev.architectury.networking.NetworkChannel
import meow.illina.hexssence.Hexssence
import meow.illina.hexssence.networking.msg.HexssenceMessageCompanion

object HexssenceNetworking {
    val CHANNEL: NetworkChannel = NetworkChannel.create(Hexssence.id("networking_channel"))

    fun init() {
        for (subclass in HexssenceMessageCompanion::class.sealedSubclasses) {
            subclass.objectInstance?.register(CHANNEL)
        }
    }
}
