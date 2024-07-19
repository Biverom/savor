package com.notjang.savor.init;

import com.notjang.savor.SavorMod;
import com.notjang.savor.block.entity.JawbreakerCoreEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SavorMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<JawbreakerCoreEntity>> JAWBREAKER_CORE_ENTITY =
            BLOCK_ENTITIES.register("jawbreaker_core_entity", () ->
                    BlockEntityType.Builder.of(JawbreakerCoreEntity::new,
                            BlockInit.JAWBREAKER_CORE.get()).build(null));
}
