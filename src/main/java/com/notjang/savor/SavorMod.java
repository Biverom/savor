package com.notjang.savor;

import com.notjang.savor.init.ItemInit;
import com.notjang.savor.network.PacketHandler;
import com.notjang.savor.proxy.CommonProxy;
import com.notjang.savor.proxy.client.ClientProxy;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;

@Mod("savor")
public class SavorMod {
	
	public static final String MOD_ID = "savor";
	//public static final CommonProxy PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
	public static CommonProxy PROXY;

	public static final CreativeModeTab SAVOR_TAB = new CreativeModeTab(MOD_ID) {
		
		@Override
		@OnlyIn(Dist.CLIENT)
		public @NotNull ItemStack makeIcon() {
			return new ItemStack(ItemInit.JAWBREAKER.get());
		}
	};
	
	public SavorMod() {
		PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
		PROXY.start();

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
	}

	private void onCommonSetup(FMLCommonSetupEvent event) {
		PacketHandler.initialize();
		PROXY.setupCommon();
	}

	private void onClientSetup(FMLClientSetupEvent event) {
		PROXY.setupClient();
	}
}
