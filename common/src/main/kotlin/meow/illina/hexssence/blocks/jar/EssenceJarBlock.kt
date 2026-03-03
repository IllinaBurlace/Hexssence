package meow.illina.hexssence.blocks.jar

import meow.illina.hexssence.registry.HexssenceBlockEntities
import meow.illina.hexssence.registry.HexssenceItems
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
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
import org.apache.commons.lang3.CharSetUtils.count

class EssenceJarBlock : Block(
    Properties
        .of()
        .noOcclusion()
        .sound(SoundType.GLASS)
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
        val bE: EssenceJarBlockEntity = level.getBlockEntity(pos, HexssenceBlockEntities.ESSENCE_JAR.value).orElseThrow()
        if (bE.lastClickTime < 10) {
            var success = false
            for (i in player.inventory.containerSize -1 downTo 0) {
                val stack = player.inventory.getItem(i)
                success = success || bE.insert(stack)
            }
            return if (success) InteractionResult.SUCCESS else InteractionResult.FAIL
        }
        val tryHand = player.getItemInHand(hand)
        if (!validEssences.contains(tryHand.item)) return InteractionResult.FAIL
        val success = bE.insert(tryHand)

        return if (hand == InteractionHand.MAIN_HAND) InteractionResult.SUCCESS else if (success) InteractionResult.PASS else InteractionResult.FAIL
    }

    override fun <T : BlockEntity?> getTicker(
        level: Level,
        state: BlockState,
        blockEntityType: BlockEntityType<T?>
    ): BlockEntityTicker<T?> {
        return BlockEntityTicker { _, _, _, blockEntityType -> (blockEntityType as EssenceJarBlockEntity).tick() }
    }

    val validEssences: List<Item> = listOf(
        HexssenceItems.ESSENCE_HEX.value,
        HexssenceItems.ESSENCE_EARTH.value,
        HexssenceItems.ESSENCE_AIR.value,
        HexssenceItems.ESSENCE_WATER.value,
        HexssenceItems.ESSENCE_FIRE.value,
        HexssenceItems.ESSENCE_METAL.value,
        HexssenceItems.ESSENCE_PRECIOUS.value,
        HexssenceItems.ESSENCE_LIFE.value,
        HexssenceItems.ESSENCE_END.value
    )
}