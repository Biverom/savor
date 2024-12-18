package com.notjang.savor.block.entity;

import com.notjang.savor.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class JawbreakerCoreEntity extends BlockEntity {
    public JawbreakerCoreEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.JAWBREAKER_CORE_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public AABB getRenderBoundingBox() {
        // Create an effectively infinite bounding box
        return new AABB(
                Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY
        );
    }

    public void tick() {
        assert level != null;
        if (level.getGameTime() % 80L == 0L) {
            level.playSound((Player)null, getBlockPos(), SoundEvents.BEACON_AMBIENT, SoundSource.BLOCKS, 2.0F, 1.0F);
        }
    }
}
