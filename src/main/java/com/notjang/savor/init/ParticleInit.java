package com.notjang.savor.init;

import com.notjang.savor.SavorMod;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ParticleInit {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, SavorMod.MOD_ID);

    public static final RegistryObject<SimpleParticleType> NETHER_COLA_BUBBLE =
            PARTICLE_TYPES.register("nether_cola_bubble", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> NETHER_COLA_BUBBLE_POP =
            PARTICLE_TYPES.register("nether_cola_bubble_pop", () -> new SimpleParticleType(true));
}
