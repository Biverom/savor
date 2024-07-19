package com.notjang.savor.utils.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class RenderUtils {

    public static void renderJawbreakerShield(PoseStack stack, VertexConsumer builder, Level level, BlockPos center, float side) {
        float a = side / 2f;
        int intSide = Math.round(side);

        renderShieldContactSide(stack, builder, level, Vec3.ZERO.add(-a, -a, -a), center, Direction.Axis.X, intSide, -1);
        renderShieldContactSide(stack, builder, level, Vec3.ZERO.add(-a, -a, -a), center, Direction.Axis.Y, intSide, -1);
        renderShieldContactSide(stack, builder, level, Vec3.ZERO.add(-a, -a, -a), center, Direction.Axis.Z, intSide, -1);
        renderShieldContactSide(stack, builder, level, Vec3.ZERO.add(a-0.5f, -a, -a), center, Direction.Axis.X, intSide, 1);
        renderShieldContactSide(stack, builder, level, Vec3.ZERO.add(-a, a-0.5f, -a), center, Direction.Axis.Y, intSide, 1);
        renderShieldContactSide(stack, builder, level, Vec3.ZERO.add(-a, -a, a-0.5f), center, Direction.Axis.Z, intSide, 1);
    }

    private static void renderShieldContactSide(PoseStack stack, VertexConsumer builder, Level level, Vec3 corner, BlockPos center, Direction.Axis axisW, int size, int flipUV) {
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
                        renderShieldContactPart(stack, builder, level, pos, pb, axisU, axisV, axisW, 1, 1, flipUV);
                    if (i != 0)
                        renderShieldContactPart(stack, builder, level, pos, pb, axisU, axisV, axisW, -1, -1, flipUV);
                    if (j != size - 1)
                        renderShieldContactPart(stack, builder, level, pos, pb, axisV, axisU, axisW, 1, -1, flipUV);
                    if (j != 0)
                        renderShieldContactPart(stack, builder, level, pos, pb, axisV, axisU, axisW, -1, 1, flipUV);
                }
            }
        }
    }

    private static Vec3i Vec3Floor(Vec3 vec) {
        return new Vec3i((int) Math.round(vec.x), (int) Math.round(vec.y), (int) Math.round(vec.z));
    }

    private static void renderShieldContactPart(PoseStack stack, VertexConsumer builder, Level level, Vec3 p, BlockPos bp, Direction.Axis translateAxis, Direction.Axis scaleAxis, Direction.Axis widthAxis, int amount, int flip, int flipUV) {
        Matrix4f last = stack.last().pose();

        BlockPos nextPos = bp.relative(translateAxis, amount);
        if (!level.getBlockState(nextPos).isSolidRender(level, nextPos)) {
            Vec3 pc = p.add(axisToVec3(translateAxis, 0.505f*amount));

            if (widthAxis.isVertical()) flip *= -1;

            Vec3 p0 = pc.add(axisToVec3(scaleAxis, 0.505f*flip));
            Vec3 p1 = pc.subtract(axisToVec3(scaleAxis, 0.505f*flip));
            Vec3 p2 = pc.subtract(axisToVec3(scaleAxis, 0.505f*flip)).subtract(axisToVec3(widthAxis, 0.5f));
            Vec3 p3 = pc.add(axisToVec3(scaleAxis, 0.505f*flip)).subtract(axisToVec3(widthAxis, 0.5f));

            float uv1 = 0.75f + (flipUV * 0.25f);
            float uv2 = 0.75f + (-flipUV * 0.25f);

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

    public static void renderCube(PoseStack stack, VertexConsumer builder, float side, Color color, float alpha, boolean renderBackside) {
        float r = color.getRed() / 255f;
        float g = color.getGreen() / 255f;
        float b = color.getBlue() / 255f;

        Matrix4f last = stack.last().pose();
        float a = side / 2f;

        Vec3 p0 = new Vec3(-a, -a, -a);
        Vec3 p1 = new Vec3(a, -a, -a);
        Vec3 p2 = new Vec3(-a, a, -a);
        Vec3 p3 = new Vec3(a, a, -a);
        Vec3 p4 = new Vec3(-a, -a, a);
        Vec3 p5 = new Vec3(a, -a, a);
        Vec3 p6 = new Vec3(-a, a, a);
        Vec3 p7 = new Vec3(a, a, a);

        renderCubePart(builder, last, r, g, b, alpha, p1, p0, p2, p3, new Vec3(0, 0, -1));
        renderCubePart(builder, last, r, g, b, alpha, p0, p4, p6, p2, new Vec3(-1, 0, 0));
        renderCubePart(builder, last, r, g, b, alpha, p4, p5, p7, p6, new Vec3(0, 0, 1));
        renderCubePart(builder, last, r, g, b, alpha, p5, p1, p3, p7, new Vec3(1, 0, 0));
        renderCubePart(builder, last, r, g, b, alpha, p5, p4, p0, p1, new Vec3(0, -1, 0));
        renderCubePart(builder, last, r, g, b, alpha, p3, p2, p6, p7, new Vec3(0, 1, 0));

        if (renderBackside) {
            renderCube(stack, builder, -side, color, alpha, false);
        }
    }

    private static void renderCubePart(VertexConsumer builder, Matrix4f last, float r, float g, float b, float alpha, Vec3 p0, Vec3 p1, Vec3 p2, Vec3 p3, Vec3 normal) {
        builder.vertex(last, (float) p0.x(), (float) p0.y(), (float) p0.z()).color(r, g, b, alpha).uv(1, 1).uv2(0, 0).normal((float)normal.x, (float)normal.y, (float)normal.z).endVertex();
        builder.vertex(last, (float) p1.x(), (float) p1.y(), (float) p1.z()).color(r, g, b, alpha).uv(0, 1).uv2(0, 0).normal((float)normal.x, (float)normal.y, (float)normal.z).endVertex();
        builder.vertex(last, (float) p2.x(), (float) p2.y(), (float) p2.z()).color(r, g, b, alpha).uv(0, 0).uv2(0, 0).normal((float)normal.x, (float)normal.y, (float)normal.z).endVertex();
        builder.vertex(last, (float) p3.x(), (float) p3.y(), (float) p3.z()).color(r, g, b, alpha).uv(1, 0).uv2(0, 0).normal((float)normal.x, (float)normal.y, (float)normal.z).endVertex();
    }

    public static void renderSphere(PoseStack mStack, VertexConsumer builder, float radius, int longs, int lats, Color color, float alpha, float endU) {
        float r = color.getRed() / 255f;
        float g = color.getGreen() / 255f;
        float b = color.getBlue() / 255f;

        Matrix4f last = mStack.last().pose();
        float startU = 0;
        float startV = 0;
        float endV = Mth.PI;
        float stepU = (endU - startU) / longs;
        float stepV = (endV - startV) / lats;
        for (int i = 0; i < longs; ++i) {
            for (int j = 0; j < lats; ++j) {
                float u = i * stepU + startU;
                float v = j * stepV + startV;
                float un = (i + 1 == longs) ? endU : (i + 1) * stepU + startU;
                float vn = (j + 1 == lats) ? endV : (j + 1) * stepV + startV;
                Vec3 p0 = parametricSphere(u, v, radius);
                Vec3 p1 = parametricSphere(u, vn, radius);
                Vec3 p2 = parametricSphere(un, v, radius);
                Vec3 p3 = parametricSphere(un, vn, radius);

                builder.vertex(last, (float) p1.x(), (float) p1.y(), (float) p1.z()).color(r, g, b, alpha).uv(0, 0).uv2(0, 0).normal(0, 0, 0).endVertex();
                builder.vertex(last, (float) p0.x(), (float) p0.y(), (float) p0.z()).color(r, g, b, alpha).uv(0, 0).uv2(0, 0).normal(0, 0, 0).endVertex();
                builder.vertex(last, (float) p2.x(), (float) p2.y(), (float) p2.z()).color(r, g, b, alpha).uv(0, 0).uv2(0, 0).normal(0, 0, 0).endVertex();
                builder.vertex(last, (float) p3.x(), (float) p3.y(), (float) p3.z()).color(r, g, b, alpha).uv(0, 0).uv2(0, 0).normal(0, 0, 0).endVertex();
            }
        }
    }

    public static void renderSphere(PoseStack mStack, VertexConsumer builder, float radius, int longs, int lats, Color color, float alpha) {
        renderSphere(mStack, builder, radius, longs, lats, color, alpha, Mth.PI * 2);
    }

    public static void renderSemiSphere(PoseStack mStack, VertexConsumer builder, float radius, int longs, int lats, Color color, float alpha) {
        renderSphere(mStack, builder, radius, longs, lats, color, alpha, Mth.PI);
    }

    public static Vec3 parametricSphere(float u, float v, float r) {
        return new Vec3(Mth.cos(u) * Mth.sin(v) * r, Mth.cos(v) * r, Mth.sin(u) * Mth.sin(v) * r);
    }


    public static void renderAura(PoseStack mStack, VertexConsumer builder, float radius, float size, int longs, Color color1, Color color2, float alpha1, float alpha2, boolean renderSide, boolean renderFloor) {
        float r1 = color1.getRed() / 255f;
        float g1 = color1.getGreen() / 255f;
        float b1 = color1.getBlue() / 255f;

        float r2 = color2.getRed() / 255f;
        float g2 = color2.getGreen() / 255f;
        float b2 = color2.getBlue() / 255f;

        float startU = 0;
        float endU = Mth.PI * 2;
        float stepU = (endU - startU) / longs;
        for (int i = 0; i < longs; ++i) {
            float u = i * stepU + startU;
            float un = (i + 1 == longs) ? endU : (i + 1) * stepU + startU;

            auraPiece(mStack, builder, radius, size, u, r2, g2, b2, alpha2);
            auraPiece(mStack, builder, radius, 0, u, r1, g2, b1, alpha1);
            auraPiece(mStack, builder, radius, 0, un, r1, g2, b1, alpha1);
            auraPiece(mStack, builder, radius, size, un, r2, g2, b2, alpha2);

            if (renderSide) {
                auraPiece(mStack, builder, radius, 0, u, r1, g2, b1, alpha1);
                auraPiece(mStack, builder, radius, size, u, r2, g2, b2, alpha2);
                auraPiece(mStack, builder, radius, size, un, r2, g2, b2, alpha2);
                auraPiece(mStack, builder, radius, 0, un, r1, g2, b1, alpha1);
            }

            if (renderFloor) {
                auraPiece(mStack, builder, 0, 0, u,r2, g2, b2, alpha2);
                auraPiece(mStack, builder, 0, 0, un, r2, g2, b2, alpha2);
                auraPiece(mStack, builder, radius, 0, u, r1, g1, b1, alpha1);
                auraPiece(mStack, builder, radius, 0, un, r1, g1, b1, alpha1);

                if (renderSide) {
                    auraPiece(mStack, builder, 0, 0, un, r2, g2, b2, alpha2);
                    auraPiece(mStack, builder, 0, 0, u,r2, g2, b2, alpha2);
                    auraPiece(mStack, builder, radius, 0, un, r1, g1, b1, alpha1);
                    auraPiece(mStack, builder, radius, 0, u, r1, g1, b1, alpha1);
                }
            }
        }
    }


    public static void auraPiece(PoseStack mStack, VertexConsumer builder, float radius, float size, float angle, float r, float g, float b, float alpha) {
        mStack.pushPose();
        //mStack.mulPose(Axis.YP.rotationDegrees((float) Math.toDegrees(angle)));
        mStack.translate(radius, 0, 0);
        Matrix4f mat = mStack.last().pose();
        builder.vertex(mat, 0, size, 0).color(r, g, b, alpha).uv(0, 0).uv2(0, 0).normal(0, 0, 0).endVertex();
        mStack.popPose();
    }
}
