@file:JvmName("HexssenceAbstractions")

package meow.illina.hexssence

import dev.architectury.injectables.annotations.ExpectPlatform
import meow.illina.hexssence.registry.HexssenceRegistrar

fun initRegistries(vararg registries: HexssenceRegistrar<*>) {
    for (registry in registries) {
        Hexssence.LOGGER.info("register running on $registry")
        initRegistry(registry)
    }
}

@ExpectPlatform
fun <T : Any> initRegistry(registrar: HexssenceRegistrar<T>) {
    throw AssertionError()
}
