package com.notjang.savor.init;

import com.notjang.savor.SavorMod;
import com.notjang.savor.effect.AbsoluteMobEffect;
import com.notjang.savor.effect.AcidPiercingMobEffect;
import com.notjang.savor.effect.CarbonationMobEffect;
import com.notjang.savor.effect.TenacityMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MobEffectInit {
	public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, SavorMod.MOD_ID);

	public static final RegistryObject<MobEffect> TENACITY = MOB_EFFECTS.register("tenacity", TenacityMobEffect::new);
	public static final RegistryObject<MobEffect> ABSOLUTE = MOB_EFFECTS.register("absolute", AbsoluteMobEffect::new);
	public static final RegistryObject<MobEffect> ACID_PIERCING = MOB_EFFECTS.register("acid_piercing", AcidPiercingMobEffect::new);
	public static final RegistryObject<MobEffect> CARBONATION = MOB_EFFECTS.register("carbonation", CarbonationMobEffect::new);

}
