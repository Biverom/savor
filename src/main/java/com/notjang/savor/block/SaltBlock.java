package com.notjang.savor.block;

import com.notjang.savor.init.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public class SaltBlock extends Block {

    private static final Direction[] DIRECTIONS = new Direction[] {Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};

    public SaltBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void randomTick(@NotNull BlockState pState, @NotNull ServerLevel pLevel, @NotNull BlockPos pPos, RandomSource pRandom) {
        if (pRandom.nextInt(3) == 0) {
            Direction direction = DIRECTIONS[pRandom.nextInt(DIRECTIONS.length)];
            BlockPos waterPos = pPos.relative(Direction.UP);
            BlockPos magmaPos = waterPos.relative(direction);
            BlockState waterBlockstate = pLevel.getBlockState(waterPos);
            BlockState magmaBlockstate = pLevel.getBlockState(magmaPos);
            if (waterBlockstate.is(Blocks.WATER) && magmaBlockstate.is(Blocks.MAGMA_BLOCK)) {
                pLevel.setBlockAndUpdate(waterPos, BlockInit.SALT_BLOCK.get().defaultBlockState());
            }
        }
    }
}
