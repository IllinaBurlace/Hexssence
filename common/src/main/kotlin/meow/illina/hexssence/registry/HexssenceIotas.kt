package meow.illina.hexssence.registry

import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.common.lib.HexRegistries
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import meow.illina.hexssence.casting.iota.RecipeIota

object HexssenceIotas : HexssenceRegistrar<IotaType<*>>(
    HexRegistries.IOTA_TYPE,
    { HexIotaTypes.REGISTRY }
) {
    val RECIPE_IOTA = register("recipe") { RecipeIota.TYPE }
}