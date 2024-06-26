package com.notjang.savor.effect;

import com.notjang.savor.init.ParticleInit;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class CarbonationMobEffect extends MobEffect {

	public CarbonationMobEffect() {
		super(MobEffectCategory.NEUTRAL, 0xC15721);
	}

	public final Random rand = new Random();

	public static <T extends Entity> void pushEntityGroup(@NotNull Entity sourceEntity, Class<T> targetClass, double forceMult) {
		for (Entity targetEntity : sourceEntity.level.getEntitiesOfClass(targetClass, sourceEntity.getBoundingBox().inflate(5))) {
			pushEntity(sourceEntity, targetEntity, forceMult);
		}
	}

	public static <T extends Entity> void pushEntity(@NotNull Entity sourceEntity, @NotNull Entity targetEntity, double forceMult) {
		if (targetEntity != sourceEntity) {
			double distance = sourceEntity.distanceTo(targetEntity);

			if (distance > 5) return;

			double force = (5 - distance) / 5 * forceMult;

			pushEntityConstant(sourceEntity, targetEntity, force, false);
		}
	}

	public static <T extends Entity> void pushEntityConstant(@NotNull Entity sourceEntity, @NotNull Entity targetEntity, double force, Boolean ignoreVelocity) {
		if (targetEntity != sourceEntity) {
			Vec3 direction = targetEntity.position().subtract(sourceEntity.position()).normalize();

			double pushX = direction.x * force;
			double pushY = direction.y * force;
			double pushZ = direction.z * force;

			Vec3 delta = new Vec3(pushX, pushY, pushZ).add(ignoreVelocity ? Vec3.ZERO : targetEntity.getDeltaMovement());
			targetEntity.setDeltaMovement(delta);
		}
	}

	@Override
	public void applyEffectTick(@NotNull LivingEntity sourceEntity, int p_19468_) {
		for(int i = 0; i < 4; ++i) {
			double d0 = sourceEntity.getX() + (rand.nextDouble() * 2.0D - 1.0D) * (double)sourceEntity.getBbWidth() * 0.5D;
			double d1 = sourceEntity.getY() + 0.05D + rand.nextDouble();
			double d2 = sourceEntity.getZ() + (rand.nextDouble() * 2.0D - 1.0D) * (double)sourceEntity.getBbWidth() * 0.5D;
			double d3 = (rand.nextDouble() * 2.0D - 1.0D) * 0.3D;
			double d4 = (rand.nextDouble() * 2.0D - 1.0D) * 0.3D;
			double d5 = (rand.nextDouble() * 2.0D - 1.0D) * 0.3D;
			sourceEntity.level.addParticle(ParticleInit.NETHER_COLA_BUBBLE.get(), d0, d1 + 1.0D, d2, d3, d4, d5);
		}
		super.applyEffectTick(sourceEntity, p_19468_);
	}

	@Override
	public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
		return true;
	}
}
