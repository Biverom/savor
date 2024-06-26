package com.notjang.savor.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.notjang.savor.entity.animations.ModAnimationDefinitions;
import com.notjang.savor.entity.custom.CrackerEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class CrackerModel<T extends Entity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	//public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "crackermodel"), "main");
	private final ModelPart cracker;
	public final ModelPart body;
	public final ModelPart head;
	private final ModelPart rightLeg;
	private final ModelPart leftLeg;

	public CrackerModel(ModelPart root) {
		this.cracker = root.getChild("cracker");
		this.body = cracker.getChild("body");
		this.head = body.getChild("head");
		this.rightLeg = cracker.getChild("leg_right");
		this.leftLeg = cracker.getChild("leg_left");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition cracker = partdefinition.addOrReplaceChild("cracker", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition leg_left = cracker.addOrReplaceChild("leg_left", CubeListBuilder.create().texOffs(24, 42).mirror().addBox(-1.0F, 0.0F, -2.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, -4.0F, 0.0F));

		PartDefinition body = cracker.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 34).addBox(-4.0F, -10.0F, -3.0F, 8.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition top_jaw = head.addOrReplaceChild("top_jaw", CubeListBuilder.create().texOffs(0, 0).addBox(-12.0F, -8.0F, -10.0F, 24.0F, 7.0F, 11.0F, new CubeDeformation(0.0F))
				.texOffs(0, -3).mirror().addBox(0.0F, -13.0F, -8.0F, 0.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -14.0F, 5.0F));

		PartDefinition mohawk_green_r1 = top_jaw.addOrReplaceChild("mohawk_green_r1", CubeListBuilder.create().texOffs(0, 21).mirror().addBox(0.0F, -5.0F, 0.0F, 0.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -8.0F, -2.0F, -0.5236F, 0.0F, 0.0F));

		PartDefinition mohawk_blue_r1 = top_jaw.addOrReplaceChild("mohawk_blue_r1", CubeListBuilder.create().texOffs(0, 3).mirror().addBox(0.0F, -5.0F, 0.0F, 0.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -8.0F, -5.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition bottom_jaw = head.addOrReplaceChild("bottom_jaw", CubeListBuilder.create().texOffs(0, 18).addBox(-12.0F, -15.0F, -5.0F, 24.0F, 5.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leg_right = cracker.addOrReplaceChild("leg_right", CubeListBuilder.create().texOffs(24, 42).addBox(-3.0F, -4.0F, -2.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);

		this.animate(((CrackerEntity)entity).idleAnimationState, ModAnimationDefinitions.CRACKER_IDLE, ageInTicks);
		this.animate(((CrackerEntity)entity).walkAnimationState, ModAnimationDefinitions.CRACKER_WALK,
				ageInTicks * (((CrackerEntity) entity).isBaby() ? 2f : 1f));
		this.animate(((CrackerEntity)entity).crunchAnimationState, ModAnimationDefinitions.CRACKER_CRUNCH, ageInTicks);

		if (this.riding) {
			this.rightLeg.xRot = -1.4137167F;
			this.rightLeg.yRot = ((float) Math.PI / 10F);
			this.rightLeg.zRot = 0.07853982F;
			this.leftLeg.xRot = -1.4137167F;
			this.leftLeg.yRot = (-(float) Math.PI / 10F);
			this.leftLeg.zRot = -0.07853982F;
		}
	}

	private void applyHeadRotation(float pNetHeadYaw, float pHeadPitch, float pAgeInTicks) {
		pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30.0F);
		//pHeadPitch = Mth.clamp(pHeadPitch, -25.0F, 45.0F);

		this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
		//this.head.xRot = pHeadPitch * ((float)Math.PI / 180F);
	}

	@Override
	public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		cracker.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public @NotNull ModelPart root() {
		return cracker;
	}
}