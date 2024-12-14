package com.notjang.savor.render.client;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.notjang.savor.SavorMod;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterShadersEvent;

import java.io.IOException;
import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public class SavorRenderTypes
{

    public static void registerShaders(RegisterShadersEvent event) {
        try {
            registerShader(event, SavorRenderTypes.jawbreakerShield(),"savor:rendertype_jawbreaker_shield", shaderInstance -> CustomRenderTypes.jawbreakerShieldShader = shaderInstance);
            registerShader(event, SavorRenderTypes.jawbreakerShieldOutline(), "savor:rendertype_jawbreaker_shield_outline", shaderInstance -> CustomRenderTypes.jawbreakerShieldOutlineShader = shaderInstance);
            registerShader(event, SavorRenderTypes.glowing(), "savor:rendertype_glowing", shaderInstance -> CustomRenderTypes.glowingShader = shaderInstance);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void registerShader(RegisterShadersEvent e, RenderType rt, String name, Consumer<ShaderInstance> onLoaded) throws IOException {
        e.registerShader(new ShaderInstance(e.getResourceManager(), new ResourceLocation(name), rt.format()), onLoaded);
    }

    // Accessor functon, ensures that you don't use the raw methods below unintentionally.
    public static RenderType jawbreakerShield() { return CustomRenderTypes.jawbreakerShield(); }
    public static RenderType jawbreakerShieldOutline() { return CustomRenderTypes.jawbreakerShieldOutline(); }
    public static RenderType glowing()
    {
        return CustomRenderTypes.glowing();
    }

    public static class CustomRenderTypes extends RenderType
    {
        // Holds the object loaded via RegisterShadersEvent
        public static ShaderInstance jawbreakerShieldShader;
        public static ShaderInstance jawbreakerShieldOutlineShader;
        public static ShaderInstance glowingShader;

        // Shader state for use in the render type, the supplier ensures it updates automatically with resource reloads
        private static final ShaderStateShard RENDERTYPE_JAWBREAKER_SHIELD_SHADER = new ShaderStateShard(() -> jawbreakerShieldShader);
        private static final ShaderStateShard RENDERTYPE_JAWBREAKER_SHIELD_OUTLINE_SHADER = new ShaderStateShard(() -> jawbreakerShieldOutlineShader);
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
                    .setShaderState(RENDERTYPE_JAWBREAKER_SHIELD_SHADER)
                    .createCompositeState(false);
            return create("savor:rendertype_jawbreaker_shield",
                    new VertexFormat(ImmutableMap.<String, VertexFormatElement>builder()
                            .put("Position", DefaultVertexFormat.ELEMENT_POSITION)
                            .put("Color", DefaultVertexFormat.ELEMENT_COLOR)
                            .put("UV0", DefaultVertexFormat.ELEMENT_UV0).build()), VertexFormat.Mode.QUADS, 2097152, true, false, rendertype$state);
        }

        private static RenderType jawbreakerShieldOutline()
        {
            RenderType.CompositeState rendertype$state = RenderType.CompositeState.builder()
                    .setWriteMaskState(new RenderStateShard.WriteMaskStateShard(true, false))
                    .setLightmapState(new RenderStateShard.LightmapStateShard(false))
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation(SavorMod.MOD_ID, "textures/misc/jawbreaker_shield_outline.png"), false, false))
                    .setShaderState(RENDERTYPE_JAWBREAKER_SHIELD_OUTLINE_SHADER)
                    .createCompositeState(false);
            return create("savor:rendertype_jawbreaker_shield_outline",
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