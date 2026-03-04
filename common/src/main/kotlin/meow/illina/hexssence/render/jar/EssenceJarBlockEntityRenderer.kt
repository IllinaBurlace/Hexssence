package meow.illina.hexssence.render.jar

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import meow.illina.hexssence.blocks.jar.EssenceJarBlockEntity
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import kotlin.math.sin

class EssenceJarBlockEntityRenderer(ctx: BlockEntityRendererProvider.Context) : BlockEntityRenderer<EssenceJarBlockEntity> {
    val itemRenderer: ItemRenderer = Minecraft.getInstance().itemRenderer
    override fun render(
        blockEntity: EssenceJarBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        val stack = ItemStack(blockEntity.storedItem, blockEntity.count)

        poseStack.pushPose()


        val offset = sin((blockEntity.level!!.gameTime + partialTick) / 8.0) / 8.0
        poseStack.translate(0.5, 0.25 + offset, 0.5)
        poseStack.mulPose(Axis.YP.rotationDegrees((blockEntity.level!!.gameTime + partialTick) * 4))
        itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, packedLight, packedOverlay, poseStack, buffer, blockEntity.level!!, 0)

        poseStack.popPose()
    }
}