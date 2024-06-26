package com.notjang.savor.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class AcidPiercingMobEffect extends MobEffect {

	public AcidPiercingMobEffect() {
		super(MobEffectCategory.NEUTRAL, 0xCFF254);
	}


	public static float calculateArmorPiercingDamage(LivingEntity attacker, LivingEntity target, float damage) {
		float armorValue = target.getArmorValue();
		float damageReduction = armorValue * 0.04f; // Each point of armor typically reduces damage by 4%
		float ignoredReduction = damageReduction * 0.5f;
		return damage + ignoredReduction * damage;
	}
}
