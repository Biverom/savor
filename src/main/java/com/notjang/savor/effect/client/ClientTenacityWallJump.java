package com.notjang.savor.effect.client;

import com.notjang.savor.effect.TenacityWallJump;
import com.notjang.savor.network.NMResetFallDistance;
import com.notjang.savor.network.PacketHandler;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashSet;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public class ClientTenacityWallJump {
    public static boolean isOnWall;
    private static int ticksKeyDown;
    private static boolean prevJumping = false;

    private static Set<Direction> walls = new HashSet<>();

    public static void tickPlayer(LocalPlayer player, boolean hasEffect) {
        double stickingZ;
        double stickingX;
        if (player.isOnGround() || player.getAbilities().flying || player.isInWater() || !hasEffect) {

            isOnWall = false;
            stickingX = Double.NaN;
            stickingZ = Double.NaN;
            prevJumping = true;

            return;
        }

        walls = TenacityWallJump.getWalls(player, isOnWall);
        ticksKeyDown = player.input.jumping ? ticksKeyDown + 1 : 0;

        if (walls.isEmpty()) {
            return;
        }

        isOnWall = true;
        stickingX = player.position().x;
        stickingZ = player.position().z;

        boolean jumpingPressed = player.input.jumping && (!prevJumping);
        prevJumping = player.input.jumping;

        if (jumpingPressed) {

            player.fallDistance = 0.0f;
            //PacketHandler.INSTANCE.sendToServer(new MessageWallJump());

            wallJump(player);
            return;
        }

        player.setPos(stickingX, player.position().y, stickingZ);

        double motionY = player.getDeltaMovement().y;
        if (motionY < -0.1) {
            motionY = -0.1;
        }

        if (player.fallDistance > 2) {
            player.fallDistance = 0;
            PacketHandler.INSTANCE.sendToServer(new NMResetFallDistance());
        }

        player.setDeltaMovement(0.0, motionY, 0.0);
    }

    private static void wallJump(LocalPlayer pl) {
        Vec3 speed = pl.getDeltaMovement();
        double speedX = speed.x;
        double speedY = speed.y;
        double speedZ = speed.z;

        int jumpBoostLevel = 0;
        MobEffectInstance jumpBoostEffect = pl.getEffect(MobEffects.JUMP);
        if (jumpBoostEffect != null) jumpBoostLevel = jumpBoostEffect.getAmplifier() + 1;

        speedY += (float) 0.75 + (jumpBoostLevel * 0.125f);

        for (Direction direction : walls) {
            switch (direction) {
                case WEST -> speedX += 0.25f;
                case EAST -> speedX -= 0.25f;
                case NORTH -> speedZ += 0.25f;
                case SOUTH -> speedZ -= 0.25f;
            }
        }

        pl.setDeltaMovement(speedX, speedY, speedZ);
    }
}
