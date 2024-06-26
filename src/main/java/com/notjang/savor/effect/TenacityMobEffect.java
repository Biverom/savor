package com.notjang.savor.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class TenacityMobEffect extends MobEffect {

	public TenacityMobEffect() {
		super(MobEffectCategory.NEUTRAL, 0xF69928);
	}

	@Override
	public void applyEffectTick(@NotNull LivingEntity entity, int p_19468_) {
		//if (!(entity instanceof Player)) TenacityWallJump.tickEntity(entity, true);
		super.applyEffectTick(entity, p_19468_);
	}
}
