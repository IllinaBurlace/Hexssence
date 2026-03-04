package meow.illina.hexssence.blocks.jar

import meow.illina.hexssence.registry.HexssenceBlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.BooleanOp
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import java.io.InvalidObjectException

class EssenceJarBlock : Block(
    Properties
        .of()
        .noOcclusion()
        .sound(SoundType.GLASS)
        .strength(0.5f, 0.3f)
),  EntityBlock {

    override fun getShape(state: BlockState, view: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape =
        Shapes.join(box(3.0, 0.0, 3.0, 13.0, 12.0, 13.0),
            box(4.0, 12.0, 4.0, 12.0, 15.0, 12.0),
            BooleanOp.OR)

    override fun newBlockEntity(pos: BlockPos, state: BlockState) = EssenceJarBlockEntity(pos, state)

    override fun getRenderShape(state: BlockState): RenderShape = RenderShape.MODEL

    override fun attack(state: BlockState, level: Level, pos: BlockPos, player: Player) {
        if (player.inventory.freeSlot == -1) return

        val bE: EssenceJarBlockEntity = level.getBlockEntity(pos, HexssenceBlockEntities.ESSENCE_JAR.value).orElseThrow()
        val count: Int = if (player.isShiftKeyDown) 64 else 1

        player.inventory.add(bE.extract(count))
    }

    override fun use(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hit: BlockHitResult
    ): InteractionResult {
        val bE: EssenceJarBlockEntity = level.getBlockEntity(pos, HexssenceBlockEntities.ESSENCE_JAR.value).orElseThrow { InvalidObjectException("EssenceJarBlockEntity not found") }

        return bE.insert(player, hand)
    }

    override fun <T : BlockEntity?> getTicker(
        level: Level,
        state: BlockState,
        blockEntityType: BlockEntityType<T?>
    ): BlockEntityTicker<T?> {
        return BlockEntityTicker { _, _, _, blockEntityType -> (blockEntityType as EssenceJarBlockEntity).tick() }
    }

}