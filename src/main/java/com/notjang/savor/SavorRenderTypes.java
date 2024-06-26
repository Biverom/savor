package com.notjang.savor;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;

public class SavorRenderTypes
{
    // Accessor functon, ensures that you don't use the raw methods below unintentionally.
    public static RenderType jawbreaker()
    {
        return CustomRenderTypes.jawbreaker();
    }

    public static class CustomRenderTypes extends RenderType
    {
        // Holds the object loaded via RegisterShadersEvent
        public static ShaderInstance jawbreakerShader;

        // Shader state for use in the render type, the supplier ensures it updates automatically with resource reloads
        private static final ShaderStateShard RENDERTYPE_JAWBREAKER_SHADER = new ShaderStateShard(() -> jawbreakerShader);

        // Dummy constructor needed to make java happy
        private CustomRenderTypes(String s, VertexFormat v, VertexFormat.Mode m, int i, boolean b, boolean b2, Runnable r, Runnable r2)
        {
            super(s, v, m, i, b, b2, r, r2);
            throw new IllegalStateException("This class is not meant to be constructed!");
        }

        // The memoize caches the output value for each input, meaning the expensive registration process doesn't have to rerun
        //public static Function<ResourceLocation, RenderType> JAWBREAKER = Util.memoize(CustomRenderTypes::jawbreaker);

        // Defines the RenderType. Make sure the name is unique by including your MODID in the name.
        private static RenderType jawbreaker()
        {
            RenderType.CompositeState rendertype$state = RenderType.CompositeState.builder()
                    .setLightmapState(LIGHTMAP)
                    .setShaderState(RENDERTYPE_JAWBREAKER_SHADER)
                    .setTextureState(BLOCK_SHEET_MIPPED)
                    .createCompositeState(true);
            return create("savor_jawbreaker", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 2097152, true, false, rendertype$state);
        }
    }
}