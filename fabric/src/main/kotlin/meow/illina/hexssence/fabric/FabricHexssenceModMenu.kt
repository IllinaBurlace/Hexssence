package meow.illina.hexssence.fabric

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import meow.illina.hexssence.HexssenceClient

object FabricHexssenceModMenu : ModMenuApi {
    override fun getModConfigScreenFactory() = ConfigScreenFactory(HexssenceClient::getConfigScreen)
}
