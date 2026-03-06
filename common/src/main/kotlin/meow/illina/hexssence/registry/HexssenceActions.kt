package meow.illina.hexssence.registry

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.HexRegistries
import at.petrak.hexcasting.common.lib.hex.HexActions
import meow.illina.hexssence.casting.actions.spells.CombineEntities
import meow.illina.hexssence.casting.actions.spells.CombineVecs
import meow.illina.hexssence.casting.actions.spells.Extract

object HexssenceActions : HexssenceRegistrar<ActionRegistryEntry>(
    HexRegistries.ACTION,
    { HexActions.REGISTRY },
) {
    val EXTRACT = make("extract_essence", HexDir.EAST, "qaqqaeawaeaeawqwa", Extract)

    val COMBINE_ENTITIES = make("combine_entities", HexDir.NORTH_WEST, "edewdwdwwdwd", CombineEntities)
    val COMBINE_JARS = make("combine_jars", HexDir.EAST, "awddwdewedqeqde", CombineVecs)

    private fun make(name: String, startDir: HexDir, signature: String, action: Action) =
        make(name, startDir, signature) { action }

    private fun make(name: String, startDir: HexDir, signature: String, getAction: () -> Action) = register(name) {
        ActionRegistryEntry(HexPattern.fromAngles(signature, startDir), getAction())
    }
}
