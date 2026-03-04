package meow.illina.hexssence.blocks.jar

import meow.illina.hexssence.api.HexssenceTags
import meow.illina.hexssence.registry.HexssenceBlockEntities
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
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

    override fun newBlockEntity(pos: BlockPos, state: BlockState) = EssenceJarBlockEntity(pos, state)

    // Debugger shouts if I don't add these :/
    @Deprecated("Overriding is fine but you shouldn't call this directly.")
    override fun getShape(state: BlockState, view: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape =
        Shapes.join(box(3.0, 0.0, 3.0, 13.0, 12.0, 13.0),
            box(4.0, 12.0, 4.0, 12.0, 15.0, 12.0),
            BooleanOp.OR)

    @Deprecated("Overriding is fine but you shouldn't call this directly.")
    override fun getRenderShape(state: BlockState): RenderShape = RenderShape.MODEL

    @Deprecated("Overriding is fine but you shouldn't call this directly.")
    override fun attack(state: BlockState, level: Level, pos: BlockPos, player: Player) {
        if ((!player.mainHandItem.isEmpty && !player.mainHandItem.`is`(HexssenceTags.Items.VALID_ESSENCE))
            || player.inventory.freeSlot == -1)
            return

        val bE: EssenceJarBlockEntity = level.getBlockEntity(pos, HexssenceBlockEntities.ESSENCE_JAR.value).orElseThrow()
        val count: Int = if (player.isShiftKeyDown) 64 else 1

        player.inventory.add(bE.extract(count))
    }

    @Deprecated("Overriding is fine but you shouldn't call this directly.")
    override fun use(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hit: BlockHitResult
    ): InteractionResult {
        val bE: EssenceJarBlockEntity = level.getBlockEntity(pos, HexssenceBlockEntities.ESSENCE_JAR.value)
            .orElseThrow { InvalidObjectException("EssenceJarBlockEntity not found") }

        return bE.insert(player, hand)
    }

    override fun <T : BlockEntity?> getTicker(
        level: Level,
        state: BlockState,
        blockEntityType: BlockEntityType<T?>
    ): BlockEntityTicker<T?> {
        return BlockEntityTicker { _, _, _, bE -> (bE as EssenceJarBlockEntity).tick() }
    }

    override fun appendHoverText(stack: ItemStack, level: BlockGetter?, tooltip: MutableList<Component?>, flag: TooltipFlag) {
        val nbt: CompoundTag = BlockItem.getBlockEntityData(stack) ?: return
        if (nbt.getString("stored") == "minecraft:air") return
        val out = Component.translatable("hexssence.tooltip.essence_jar",
            Component.translatable(ResourceLocation(nbt.getString("stored")).toLanguageKey("item")),
            nbt.getInt("count")
        ).withStyle(ChatFormatting.LIGHT_PURPLE)

        tooltip.add(out)

        super.appendHoverText(stack, level, tooltip, flag)
    }
}