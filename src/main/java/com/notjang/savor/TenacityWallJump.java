package com.notjang.savor;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Set;

public class TenacityWallJump {

    public static boolean collidesWithBlock(Level level, AABB box) {
        return !level.noCollision(box);
    }

    public static Set<Direction> getWalls(LivingEntity entity, boolean expandCheck) {

        Vec3 pos = entity.position();
        AABB box = new AABB(pos.x - 0.001, pos.y, pos.z - 0.001, pos.x + 0.001, pos.y + entity.getEyeHeight(), pos.z + 0.001);

        double dist = (entity.getBbWidth() / 2) + (expandCheck ? 0.1 : 0.06);
        AABB[] axes = {box.expandTowards(0, 0, dist), box.expandTowards(-dist, 0, 0), box.expandTowards(0, 0, -dist), box.expandTowards(dist, 0, 0)};

        int i = 0;
        Direction direction;
        Set<Direction> currentWalls = new HashSet<>();
        for (AABB axis : axes) {
            direction = Direction.from2DDataValue(i++);
            if (collidesWithBlock(entity.level, axis)) {
                currentWalls.add(direction);
                entity.horizontalCollision = true;
            }
        }
        return currentWalls;
    }

    public static void tickEntity(LivingEntity entity, boolean hasEffect) {
        if (entity.isOnGround() || entity.isInWater() || !hasEffect) {
            return;
        }

        Set<Direction> entityWalls = getWalls(entity, true);

        if (entityWalls.isEmpty()) {
            return;
        }

        double motionY = entity.getDeltaMovement().y;
        if (motionY < -0.1) {
            motionY = -0.1;
        }

        if (entity.fallDistance > 2) {
            entity.fallDistance = 0;
        }

        entity.setDeltaMovement(entity.getDeltaMovement().x, motionY, entity.getDeltaMovement().z);
    }
}
