package com.notjang.savor.init;

import com.notjang.savor.SavorMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundEffectInit {
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
			DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SavorMod.MOD_ID);

	public static final RegistryObject<SoundEvent> EFFECT_ABSOLUTE_EVADE = registerSoundEvent("effect.absolute.evade");
	public static final RegistryObject<SoundEvent> EFFECT_ABSOLUTE_EVADE_LAST = registerSoundEvent("effect.absolute.evade_last");


	private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
		return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(SavorMod.MOD_ID, name)));
	}

}
