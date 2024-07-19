package com.notjang.savor.block.entity;

import com.notjang.savor.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class JawbreakerCoreEntity extends BlockEntity {
    public JawbreakerCoreEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.JAWBREAKER_CORE_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public AABB getRenderBoundingBox() {
        return super.getRenderBoundingBox().inflate(10);
    }
}
