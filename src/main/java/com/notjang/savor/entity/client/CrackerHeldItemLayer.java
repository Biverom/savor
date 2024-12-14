package com.notjang.savor.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.notjang.savor.entity.CrackerEntity;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class CrackerHeldItemLayer extends RenderLayer<CrackerEntity, CrackerModel<CrackerEntity>> {
    private final ItemInHandRenderer itemInHandRenderer;

    public CrackerHeldItemLayer(RenderLayerParent<CrackerEntity, CrackerModel<CrackerEntity>> p_234862_, ItemInHandRenderer p_234863_) {
        super(p_234862_);
        this.itemInHandRenderer = p_234863_;
    }

    public void render(@NotNull PoseStack p_117280_, @NotNull MultiBufferSource p_117281_, int p_117282_, CrackerEntity p_117283_, float p_117284_, float p_117285_, float p_117286_, float p_117287_, float p_117288_, float p_117289_) {
        ItemStack itemstack = p_117283_.getItemBySlot(EquipmentSlot.MAINHAND);
        if (!itemstack.isEmpty()) {
            p_117280_.pushPose();
            p_117280_.scale(1.5f, 1.5f, 1.5f);
            p_117280_.translate(0.0, 1.0, -0.05);
            this.getParentModel().body.translateAndRotate(p_117280_);
            p_117280_.mulPose(Quaternion.fromXYZ(0.0f, this.getParentModel().head.yRot, 0.0f));
            p_117280_.translate(0.0, -0.575, 0);
            p_117280_.mulPose(Quaternion.fromXYZ((float) Math.PI, (float) Math.PI, 0.0f));
            this.itemInHandRenderer.renderItem(p_117283_, itemstack, ItemTransforms.TransformType.GROUND, false, p_117280_, p_117281_, p_117282_);
            p_117280_.popPose();
        }
    }
}