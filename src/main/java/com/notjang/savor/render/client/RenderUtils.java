package com.notjang.savor.render.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderUtils {

    public static void renderShieldOutline(PoseStack stack, VertexConsumer builder, Level level, BlockPos center, float side) {
        float a = side / 2f;
        int intSide = Math.round(side);

        renderShieldOutlineSide(stack, builder, level, Vec3.ZERO.add(-a+0.5f, -a, -a), center, Direction.Axis.X, intSide, -1);
        renderShieldOutlineSide(stack, builder, level, Vec3.ZERO.add(-a, -a+0.5f, -a), center, Direction.Axis.Y, intSide, -1);
        renderShieldOutlineSide(stack, builder, level, Vec3.ZERO.add(-a, -a, -a+0.5f), center, Direction.Axis.Z, intSide, -1);
        renderShieldOutlineSide(stack, builder, level, Vec3.ZERO.add(a-0.5f, -a, -a), center, Direction.Axis.X, intSide, 1);
        renderShieldOutlineSide(stack, builder, level, Vec3.ZERO.add(-a, a-0.5f, -a), center, Direction.Axis.Y, intSide, 1);
        renderShieldOutlineSide(stack, builder, level, Vec3.ZERO.add(-a, -a, a-0.5f), center, Direction.Axis.Z, intSide, 1);
    }

    private static void renderShieldOutlineSide(PoseStack stack, VertexConsumer builder, Level level, Vec3 corner, BlockPos center, Direction.Axis axisW, int size, int flipUV) {
        Direction.Axis axisU = null;
        Direction.Axis axisV = null;

        for (int i = 0, j = 0; i < Direction.Axis.values().length; i++) {
            if (Direction.Axis.values()[i] != axisW) {
                if (j == 0) {
                    axisU = Direction.Axis.values()[i];
                } else {
                    axisV = Direction.Axis.values()[i];
                }
                j++;
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                assert axisU != null;
                assert axisV != null;
                Vec3 pos = corner.add(axisToVec3(axisU, i)).add(axisToVec3(axisV, j));
                BlockPos pb = center.offset(Vec3Floor(pos));
                if (level.getBlockState(pb).isSolidRender(level, pb)) {
                    if (i != size - 1)
                        renderShieldOutlinePart(stack, builder, level, pos, pb, axisU, axisV, axisW, 1, 1, flipUV);
                    if (i != 0)
                        renderShieldOutlinePart(stack, builder, level, pos, pb, axisU, axisV, axisW, -1, -1, flipUV);
                    if (j != size - 1)
                        renderShieldOutlinePart(stack, builder, level, pos, pb, axisV, axisU, axisW, 1, -1, flipUV);
                    if (j != 0)
                        renderShieldOutlinePart(stack, builder, level, pos, pb, axisV, axisU, axisW, -1, 1, flipUV);
                }
            }
        }
    }

    private static Vec3i Vec3Floor(Vec3 vec) {
        return new Vec3i((int) Math.round(vec.x), (int) Math.round(vec.y), (int) Math.round(vec.z));
    }

    private static void renderShieldOutlinePart(PoseStack stack, VertexConsumer builder, Level level, Vec3 p, BlockPos bp, Direction.Axis translateAxis, Direction.Axis scaleAxis, Direction.Axis widthAxis, int amount, int flip, int flipUV) {
        Matrix4f last = stack.last().pose();

        BlockPos nextPos = bp.relative(translateAxis, amount);
        if (!level.getBlockState(nextPos).isSolidRender(level, nextPos)) {
            Vec3 pc = p.add(axisToVec3(translateAxis, 0.501f*amount));

            if (widthAxis.isVertical()) flip *= -1;

            Vec3 p0 = pc.add(axisToVec3(scaleAxis, 0.5f*flip));
            Vec3 p1 = pc.subtract(axisToVec3(scaleAxis, 0.5f*flip));
            Vec3 p2 = pc.subtract(axisToVec3(scaleAxis, 0.5f*flip)).subtract(axisToVec3(widthAxis, 1.0f));
            Vec3 p3 = pc.add(axisToVec3(scaleAxis, 0.5f*flip)).subtract(axisToVec3(widthAxis, 1.0f));

            float uv1 = 0.5f + (flipUV * 0.5f);
            float uv2 = 0.5f + (-flipUV * 0.5f);

            builder.vertex(last, (float) p0.x(), (float) p0.y(), (float) p0.z()).color(vecToFac(p0), 1.0f, 1.0f, 0.5f).uv(1f, uv1).uv2(0, 0).normal(0, 0, 0).endVertex();
            builder.vertex(last, (float) p1.x(), (float) p1.y(), (float) p1.z()).color(vecToFac(p1), 1.0f, 1.0f, 0.5f).uv(0f, uv1).uv2(0, 0).normal(0, 0, 0).endVertex();
            builder.vertex(last, (float) p2.x(), (float) p2.y(), (float) p2.z()).color(vecToFac(p2), 1.0f, 1.0f, 0.5f).uv(0f, uv2).uv2(0, 0).normal(0, 0, 0).endVertex();
            builder.vertex(last, (float) p3.x(), (float) p3.y(), (float) p3.z()).color(vecToFac(p3), 1.0f, 1.0f, 0.5f).uv(1f, uv2).uv2(0, 0).normal(0, 0, 0).endVertex();
        }
    }

    private static float vecToFac(Vec3 vec) {
        return (float)(((vec.x + 5f) + (vec.y + 5f) + (vec.z + 5f)) / 30f);
    }

    private static Vec3 axisToVec3(Direction.Axis axis, float amount) {
        return switch (axis) {
            case X -> new Vec3(amount, 0, 0);
            case Y -> new Vec3(0, amount, 0);
            case Z -> new Vec3(0, 0, amount);
        };
    }

    public static void renderShieldCube(PoseStack stack, VertexConsumer builder, float side, float alpha) {
        Matrix4f transformationMatrix = stack.last().pose();
        float halfSide = side / 2f;

        Vec3[] vertices = {
                new Vec3(-halfSide, -halfSide, -halfSide),
                new Vec3(halfSide, -halfSide, -halfSide),
                new Vec3(-halfSide, halfSide, -halfSide),
                new Vec3(halfSide, halfSide, -halfSide),
                new Vec3(-halfSide, -halfSide, halfSide),
                new Vec3(halfSide, -halfSide, halfSide),
                new Vec3(-halfSide, halfSide, halfSide),
                new Vec3(halfSide, halfSide, halfSide)
        };

        Vec3[] normals = {
                new Vec3(0, 0, -1), // Front
                new Vec3(-1, 0, 0), // Left
                new Vec3(0, 0, 1),  // Back
                new Vec3(1, 0, 0),  // Right
                new Vec3(0, -1, 0), // Bottom
                new Vec3(0, 1, 0)   // Top
        };

        int[][] faceIndices = {
                {1, 0, 2, 3}, // Front
                {0, 4, 6, 2}, // Left
                {4, 5, 7, 6}, // Back
                {5, 1, 3, 7}, // Right
                {5, 4, 0, 1}, // Bottom
                {3, 2, 6, 7}  // Top
        };

        float[][] uvMappings = {
                {1, 1}, {0, 1}, {0, 0}, {1, 0} // UV coordinates for each vertex in a face
        };

        for (int i = 0; i < faceIndices.length; i++) {
            for (int j = 0; j < faceIndices[i].length; j++) {
                Vec3 position = vertices[faceIndices[i][j]];
                Vec3 normal = normals[i];

                float u = uvMappings[j][0];
                float v = uvMappings[j][1];

                float red = Math.max(0, Math.min(1, (float) (position.x() / 2 + 0.5)));
                float green = Math.max(0, Math.min(1, (float) (position.y() / 2 + 0.5)));
                float blue = Math.max(0, Math.min(1, (float) (position.z() / 2 + 0.5)));

                builder.vertex(transformationMatrix, (float) position.x(), (float) position.y(), (float) position.z())
                        .color(red, green, blue, alpha)
                        .uv(u, v).uv2(0, 0)
                        .normal((float) normal.x, (float) normal.y, (float) normal.z)
                        .endVertex();
            }
        }
    }



}
