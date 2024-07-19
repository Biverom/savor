package com.notjang.savor;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;

public class SavorRenderTypes
{
    // Accessor functon, ensures that you don't use the raw methods below unintentionally.
    public static RenderType jawbreakerShield() { return CustomRenderTypes.jawbreakerShield(); }
    public static RenderType jawbreakerShieldContact() { return CustomRenderTypes.jawbreakerShieldContact(); }
    public static RenderType glowing()
    {
        return CustomRenderTypes.glowing();
    }

    public static class CustomRenderTypes extends RenderType
    {
        // Holds the object loaded via RegisterShadersEvent
        public static ShaderInstance jawbreakerShieldShader;
        public static ShaderInstance jawbreakerShieldContactShader;
        public static ShaderInstance glowingShader;

        // Shader state for use in the render type, the supplier ensures it updates automatically with resource reloads
        private static final ShaderStateShard RENDERTYPE_JAWBREAKER_SHIELD_SHADER = new ShaderStateShard(() -> jawbreakerShieldShader);
        private static final ShaderStateShard RENDERTYPE_JAWBREAKER_SHIELD_CONTACT_SHADER = new ShaderStateShard(() -> jawbreakerShieldContactShader);
        private static final ShaderStateShard RENDERTYPE_GLOWING_SHADER = new ShaderStateShard(() -> glowingShader);

        // Dummy constructor needed to make java happy
        private CustomRenderTypes(String s, VertexFormat v, VertexFormat.Mode m, int i, boolean b, boolean b2, Runnable r, Runnable r2)
        {
            super(s, v, m, i, b, b2, r, r2);
            throw new IllegalStateException("This class is not meant to be constructed!");
        }

        // The memoize caches the output value for each input, meaning the expensive registration process doesn't have to rerun
        //public static Function<ResourceLocation, RenderType> JAWBREAKER = Util.memoize(CustomRenderTypes::jawbreaker);

        // Defines the RenderType. Make sure the name is unique by including your MODID in the name.
        private static RenderType jawbreakerShield()
        {
            RenderType.CompositeState rendertype$state = RenderType.CompositeState.builder()
                    .setWriteMaskState(new RenderStateShard.WriteMaskStateShard(true, false))
                    .setLightmapState(new RenderStateShard.LightmapStateShard(false))
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation(SavorMod.MOD_ID, "textures/shaders/jawbreaker_shield.png"), false, false))
                    .setShaderState(RENDERTYPE_JAWBREAKER_SHIELD_SHADER)
                    .createCompositeState(false);
            return create("savor:rendertype_jawbreaker_shield",
                    new VertexFormat(ImmutableMap.<String, VertexFormatElement>builder()
                            .put("Position", DefaultVertexFormat.ELEMENT_POSITION)
                            .put("Color", DefaultVertexFormat.ELEMENT_COLOR)
                            .put("UV0", DefaultVertexFormat.ELEMENT_UV0).build()), VertexFormat.Mode.QUADS, 2097152, true, false, rendertype$state);
        }

        private static RenderType jawbreakerShieldContact()
        {
            RenderType.CompositeState rendertype$state = RenderType.CompositeState.builder()
                    .setWriteMaskState(new RenderStateShard.WriteMaskStateShard(true, false))
                    .setLightmapState(new RenderStateShard.LightmapStateShard(false))
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation(SavorMod.MOD_ID, "textures/shaders/jawbreaker_shield_contact.png"), false, false))
                    .setShaderState(RENDERTYPE_JAWBREAKER_SHIELD_CONTACT_SHADER)
                    .createCompositeState(false);
            return create("savor:rendertype_jawbreaker_shield_contact",
                    new VertexFormat(ImmutableMap.<String, VertexFormatElement>builder()
                            .put("Position", DefaultVertexFormat.ELEMENT_POSITION)
                            .put("Color", DefaultVertexFormat.ELEMENT_COLOR)
                            .put("UV0", DefaultVertexFormat.ELEMENT_UV0).build()), VertexFormat.Mode.QUADS, 2097152, true, false, rendertype$state);
        }

        private static RenderType glowing()
        {
            RenderType.CompositeState rendertype$state = RenderType.CompositeState.builder()
                    .setWriteMaskState(new RenderStateShard.WriteMaskStateShard(true, false))
                    .setLightmapState(new RenderStateShard.LightmapStateShard(false))
                    .setTransparencyState(ADDITIVE_TRANSPARENCY)
                    .setShaderState(RENDERTYPE_GLOWING_SHADER)
                    .createCompositeState(false);
            return create("savor:rendertype_glowing", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, true, false, rendertype$state);
        }
    }
}