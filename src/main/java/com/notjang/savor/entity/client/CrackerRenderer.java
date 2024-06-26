package com.notjang.savor.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.notjang.savor.SavorMod;
import com.notjang.savor.entity.custom.CrackerEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class CrackerRenderer extends MobRenderer<CrackerEntity, CrackerModel<CrackerEntity>> {
    public CrackerRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new CrackerModel<>(p_174304_.bakeLayer(ModModelLayers.CRACKER_LAYER)), 0.5f);
        this.addLayer(new CrackerHeldItemLayer(this, p_174304_.getItemInHandRenderer()));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull CrackerEntity p_114482_) {
        return new ResourceLocation(SavorMod.MOD_ID, "textures/entity/cracker1.png");
    }

    @Override
    public void render(CrackerEntity p_115455_, float p_115456_, float p_115457_, @NotNull PoseStack p_115458_, @NotNull MultiBufferSource p_115459_, int p_115460_) {
        if (p_115455_.isBaby()) {
            p_115458_.scale(0.5f, 0.5f, 0.5f);
        }

        super.render(p_115455_, p_115456_, p_115457_, p_115458_, p_115459_, p_115460_);
    }
}
