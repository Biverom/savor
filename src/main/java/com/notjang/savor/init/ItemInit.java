package com.notjang.savor.init;

import com.notjang.savor.SavorMod;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ItemInit {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
			SavorMod.MOD_ID);

	public static final RegistryObject<Item> CARAMEL_APPLE = register("caramel_apple",
			() -> new Item(new Item.Properties().tab(SavorMod.SAVOR_TAB)));
	public static final RegistryObject<Item> CARAMEL_CAKE_SLICE = register("caramel_cake_slice",
			() -> new Item(new Item.Properties().tab(SavorMod.SAVOR_TAB)));
	public static final RegistryObject<Item> CARAMEL_ICE_CREAM = register("caramel_ice_cream",
			() -> new Item(new Item.Properties().tab(SavorMod.SAVOR_TAB)));
	public static final RegistryObject<Item> CARAMEL_MILKSHAKE = register("caramel_milkshake",
			() -> new Item(new Item.Properties().tab(SavorMod.SAVOR_TAB)));
	public static final RegistryObject<Item> JAWBREAKER_CAKE_SLICE = register("jawbreaker_cake_slice",
			() -> new Item(new Item.Properties().tab(SavorMod.SAVOR_TAB)));
	public static final RegistryObject<Item> JAWBREAKER_ICE_CREAM = register("jawbreaker_ice_cream",
			() -> new Item(new Item.Properties().tab(SavorMod.SAVOR_TAB)));
	public static final RegistryObject<Item> JAWBREAKER_MILKSHAKE = register("jawbreaker_milkshake",
			() -> new Item(new Item.Properties().tab(SavorMod.SAVOR_TAB)));
	public static final RegistryObject<Item> JAWBREAKER = register("jawbreaker",
			() -> new Item(new Item.Properties().tab(SavorMod.SAVOR_TAB)));
	
	public static final RegistryObject<Item> LEMON_CAKE_SLICE = register("lemon_cake_slice",
			() -> new Item(new Item.Properties().tab(SavorMod.SAVOR_TAB)));
	public static final RegistryObject<Item> LEMON_ICE_CREAM = register("lemon_ice_cream",
			() -> new Item(new Item.Properties().tab(SavorMod.SAVOR_TAB)));
	public static final RegistryObject<Item> LEMON_MILKSHAKE = register("lemon_milkshake",
			() -> new Item(new Item.Properties().tab(SavorMod.SAVOR_TAB)));
	public static final RegistryObject<Item> LEMON = register("lemon",
			() -> new Item(new Item.Properties().tab(SavorMod.SAVOR_TAB)));
	public static final RegistryObject<Item> LEMONADE = register("lemonade",
			() -> new Item(new Item.Properties().tab(SavorMod.SAVOR_TAB)));
	
	public static final RegistryObject<Item> NETHER_COLA = register("nether_cola",
			() -> new Item(new Item.Properties().tab(SavorMod.SAVOR_TAB)));
	public static final RegistryObject<Item> NETHER_COLA_CAKE_SLICE = register("nether_cola_cake_slice",
			() -> new Item(new Item.Properties().tab(SavorMod.SAVOR_TAB)));
	public static final RegistryObject<Item> NETHER_COLA_ICE_CREAM = register("nether_cola_ice_cream",
			() -> new Item(new Item.Properties().tab(SavorMod.SAVOR_TAB)));
	public static final RegistryObject<Item> NETHER_COLA_MILKSHAKE = register("nether_cola_milkshake",
			() -> new Item(new Item.Properties().tab(SavorMod.SAVOR_TAB)));
	public static final RegistryObject<Item> NETHER_COLA_POWDER = register("nether_cola_powder",
			() -> new Item(new Item.Properties().tab(SavorMod.SAVOR_TAB)));
	
	public static final RegistryObject<Item> SALT = register("salt",
			() -> new Item(new Item.Properties().tab(SavorMod.SAVOR_TAB)));
	public static final RegistryObject<Item> SALTED_CARAMEL = register("salted_caramel",
			() -> new Item(new Item.Properties().tab(SavorMod.SAVOR_TAB)));

	public static final RegistryObject<Item> CRACKER_SPAWN_EGG = register("cracker_spawn_egg",
			() -> new ForgeSpawnEggItem(EntityInit.CRACKER, 0x000000, 0x000000,
					new Item.Properties().tab(SavorMod.SAVOR_TAB)));

	private static <T extends Item> RegistryObject<T> register(final String name, final Supplier<T> item) {
		return ITEMS.register(name, item);
	}
	
	public static final class InitFoods {
		public static final FoodProperties MILKSHAKE = (new FoodProperties.Builder()).nutrition(3).saturationMod(0.6F).alwaysEat().build();

		public static final FoodProperties CARAMEL_CAKE = (new FoodProperties.Builder()).nutrition(1).saturationMod(0.1F).effect(() -> new MobEffectInstance(MobEffectInit.TENACITY.get(), 200), 1.0F).build();
		
		public static final FoodProperties JAWBREAKER_CAKE = (new FoodProperties.Builder()).nutrition(1).saturationMod(0.1F).effect(() -> new MobEffectInstance(MobEffectInit.ABSOLUTE.get(), 200), 1.0F).build();
		
		public static final FoodProperties LEMON_CAKE = (new FoodProperties.Builder()).nutrition(1).saturationMod(0.1F).effect(() -> new MobEffectInstance(MobEffectInit.ACID_PIERCING.get(), 200), 1.0F).build();
		
		public static final FoodProperties NETHER_COLA_CAKE = (new FoodProperties.Builder()).nutrition(1).saturationMod(0.1F).effect(() -> new MobEffectInstance(MobEffectInit.CARBONATION.get(), 200), 1.0F).build();

	}

}