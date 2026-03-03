package meow.illina.hexssence.registry

import at.petrak.hexcasting.xplat.IXplatAbstractions
import meow.illina.hexssence.blocks.jar.EssenceJarBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

object HexssenceBlockEntities : HexssenceRegistrar<BlockEntityType<*>>(
    Registries.BLOCK_ENTITY_TYPE,
    { BuiltInRegistries.BLOCK_ENTITY_TYPE },
) {
    @JvmField
    val ESSENCE_JAR = register("essence_jar", ::EssenceJarBlockEntity) {
        arrayOf(
            HexssenceBlocks.ESSENCE_JAR.value,
        )
    }

    private fun <T : BlockEntity> register(
        name: String,
        func: (BlockPos, BlockState) -> T,
        blocks: () -> Array<Block>,
    ) = register(name) { IXplatAbstractions.INSTANCE.createBlockEntityType(func, *blocks()) }
}