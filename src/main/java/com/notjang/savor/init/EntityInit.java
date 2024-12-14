package com.notjang.savor.init;

import com.notjang.savor.SavorMod;
import com.notjang.savor.entity.CrackerEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityInit {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SavorMod.MOD_ID);

    public static final RegistryObject<EntityType<CrackerEntity>> CRACKER =
            ENTITIES.register("cracker", () -> EntityType.Builder.of(CrackerEntity::new, MobCategory.CREATURE)
                    .sized(0.6f, 1.4f).build("cracker"));
}
