@file:JvmName("HexssenceAbstractionsImpl")

package meow.illina.hexssence.fabric

import meow.illina.hexssence.registry.HexssenceRegistrar
import net.minecraft.core.Registry

fun <T : Any> initRegistry(registrar: HexssenceRegistrar<T>) {
    val registry = registrar.registry
    registrar.init { id, value -> Registry.register(registry, id, value) }
}
