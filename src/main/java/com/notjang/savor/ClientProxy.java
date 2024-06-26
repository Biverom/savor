package com.notjang.savor;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.notjang.savor.entity.client.CrackerModel;
import com.notjang.savor.entity.client.CrackerRenderer;
import com.notjang.savor.entity.client.ModModelLayers;
import com.notjang.savor.init.EntityInit;
import com.notjang.savor.init.ParticleInit;
import com.notjang.savor.particle.NetherColaBubbleParticle;
import com.notjang.savor.particle.NetherColaBubblePopParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterNamedRenderTypesEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.io.IOException;

public class ClientProxy extends CommonProxy {

    public static Minecraft minecraft;

    @Override
    public void start() {
        super.start();

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(this::registerParticleProviders);
        bus.addListener(this::registerShaders);
        bus.addListener(this::registerRenderTypes);
        bus.addListener(this::registerLayer);
    }

    @Override
    public void setupClient() {
        MinecraftForge.EVENT_BUS.register(this);
        minecraft = Minecraft.getInstance();

        EntityRenderers.register(EntityInit.CRACKER.get(), CrackerRenderer::new);
    }

    @SubscribeEvent
    public void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.register(ParticleInit.NETHER_COLA_BUBBLE.get(), NetherColaBubbleParticle.Provider::new);
        event.register(ParticleInit.NETHER_COLA_BUBBLE_POP.get(), NetherColaBubblePopParticle.Provider::new);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        LocalPlayer player = minecraft.player;
        if (event.phase != TickEvent.Phase.END || player == null) return;

        ClientTenacityWallJump.tickPlayer(player, getTenacityLevel(player) >= 0);
    }

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        Component saltedComponent = Component.translatable("item.flavor_modifier.salted").withStyle(style -> style.withColor(0xa5654b));
        Component sweetenedComponent = Component.translatable("item.flavor_modifier.sweetened").withStyle(style -> style.withColor(0x545468));

        ItemStack itemStack = event.getItemStack();
        if (itemStack.getTag() != null) {
            byte flavor = itemStack.getTag().getByte("FlavorModifier");
            if (flavor == 1) {
                event.getToolTip().add(1, saltedComponent);
            }
            else if (flavor == 2) {
                event.getToolTip().add(1, sweetenedComponent);
            }
        }
    }

    @SubscribeEvent
    public void registerShaders(RegisterShadersEvent event)
    {
        try {
            event.registerShader(new ShaderInstance(event.getResourceManager(), new ResourceLocation("savor:rendertype_jawbreaker"), DefaultVertexFormat.BLOCK), shaderInstance -> SavorRenderTypes.CustomRenderTypes.jawbreakerShader = shaderInstance);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SubscribeEvent
    public void registerRenderTypes(RegisterNamedRenderTypesEvent event)
    {
        //event.register("jawbreaker", SavorRenderTypes.jawbreaker(), Sheets.solidBlockSheet());
        event.register("jawbreaker", RenderType.solid(), Sheets.solidBlockSheet());
    }

    @SubscribeEvent
    public void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.CRACKER_LAYER, CrackerModel::createBodyLayer);
    }
}