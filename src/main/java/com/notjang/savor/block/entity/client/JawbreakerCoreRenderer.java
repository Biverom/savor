package com.notjang.savor.block.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.notjang.savor.render.client.SavorRenderTypes;
import com.notjang.savor.block.entity.JawbreakerCoreEntity;
import com.notjang.savor.render.client.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class JawbreakerCoreRenderer implements BlockEntityRenderer<JawbreakerCoreEntity> {

    public JawbreakerCoreRenderer(BlockEntityRendererProvider.Context context) {

    }
    @Override
    public void render(@NotNull JawbreakerCoreEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, @NotNull MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {

        VertexConsumer builder1 = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(SavorRenderTypes.jawbreakerShieldOutline());
        pPoseStack.pushPose();
        pPoseStack.translate(1f, 1f, 1f);
        RenderUtils.renderShieldOutline(pPoseStack, builder1, pBlockEntity.getLevel(), pBlockEntity.getBlockPos(), 9f);
        pPoseStack.popPose();

        VertexConsumer builder2 = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(SavorRenderTypes.jawbreakerShield());
        pPoseStack.pushPose();
        pPoseStack.translate(0.5f, 0.5f, 0.5f);
        RenderUtils.renderCube(pPoseStack, builder2, 9.005f, Color.WHITE, 1.0f, false);
        RenderUtils.renderCube(pPoseStack, builder2, -8.995f, Color.WHITE, 0.5f, false);
        pPoseStack.popPose();
    }

    @Override
    public int getViewDistance() {
        return 32;
    }
}
