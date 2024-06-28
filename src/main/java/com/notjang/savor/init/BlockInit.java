package com.notjang.savor.init;

import com.google.common.base.Supplier;
import com.notjang.savor.SavorMod;
import com.notjang.savor.block.SaltBlock;
import com.notjang.savor.init.ItemInit.InitFoods;
import com.teamabnormals.neapolitan.common.block.FlavoredCakeBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

public class BlockInit {
	public static final DeferredRegister<Block> BLOCKS =
			DeferredRegister.create(ForgeRegistries.BLOCKS, SavorMod.MOD_ID);
	public static final DeferredRegister<Item> ITEMS = ItemInit.ITEMS;
	
	public static final RegistryObject<Block> CARAMEL_ICE_CREAM_BLOCK = register("caramel_ice_cream_block",
			() -> new Block(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_LIGHT_GRAY).strength(3.0f)
					.sound(SoundType.METAL).requiresCorrectToolForDrops()),
			object -> () -> new BlockItem(object.get(), new Item.Properties().tab(SavorMod.SAVOR_TAB)));
	public static final RegistryObject<Block> JAWBREAKER_ICE_CREAM_BLOCK = register("jawbreaker_ice_cream_block",
			() -> new Block(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_LIGHT_GRAY).strength(3.0f)
					.sound(SoundType.METAL).requiresCorrectToolForDrops()),
			object -> () -> new BlockItem(object.get(), new Item.Properties().tab(SavorMod.SAVOR_TAB)));
	public static final RegistryObject<Block> LEMON_ICE_CREAM_BLOCK = register("lemon_ice_cream_block",
			() -> new Block(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_LIGHT_GRAY).strength(3.0f)
					.sound(SoundType.METAL).requiresCorrectToolForDrops()),
			object -> () -> new BlockItem(object.get(), new Item.Properties().tab(SavorMod.SAVOR_TAB)));
	public static final RegistryObject<Block> NETHER_COLA_ICE_CREAM_BLOCK = register("nether_cola_ice_cream_block",
			() -> new Block(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_LIGHT_GRAY).strength(3.0f)
					.sound(SoundType.METAL).requiresCorrectToolForDrops()),
			object -> () -> new BlockItem(object.get(), new Item.Properties().tab(SavorMod.SAVOR_TAB)));
	
	public static final RegistryObject<Block> CARAMEL_CAKE = register("caramel_cake",
			() -> new FlavoredCakeBlock(InitFoods.CARAMEL_CAKE, BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_LIGHT_GRAY).strength(3.0f)
					.sound(SoundType.METAL).requiresCorrectToolForDrops()),
			object -> () -> new BlockItem(object.get(), new Item.Properties().tab(SavorMod.SAVOR_TAB)));
	public static final RegistryObject<Block> JAWBREAKER_CAKE = register("jawbreaker_cake",
			() -> new FlavoredCakeBlock(InitFoods.JAWBREAKER_CAKE, BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_LIGHT_GRAY).strength(3.0f)
					.sound(SoundType.METAL).requiresCorrectToolForDrops()),
			object -> () -> new BlockItem(object.get(), new Item.Properties().tab(SavorMod.SAVOR_TAB)));
	public static final RegistryObject<Block> LEMON_CAKE = register("lemon_cake",
			() -> new FlavoredCakeBlock(InitFoods.LEMON_CAKE, BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_LIGHT_GRAY).strength(3.0f)
					.sound(SoundType.METAL).requiresCorrectToolForDrops()),
			object -> () -> new BlockItem(object.get(), new Item.Properties().tab(SavorMod.SAVOR_TAB)));
	public static final RegistryObject<Block> NETHER_COLA_CAKE = register("nether_cola_cake",
			() -> new FlavoredCakeBlock(InitFoods.NETHER_COLA_CAKE, BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_LIGHT_GRAY).strength(3.0f)
					.sound(SoundType.METAL).requiresCorrectToolForDrops()),
			object -> () -> new BlockItem(object.get(), new Item.Properties().tab(SavorMod.SAVOR_TAB)));

	public static final RegistryObject<Block> SALT_BLOCK = register("salt_block",
			() -> new SaltBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY).randomTicks().strength(3.0f)
					.sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops()),
			object -> () -> new BlockItem(object.get(), new Item.Properties().tab(SavorMod.SAVOR_TAB)));

	private static RotatedPillarBlock log(MaterialColor p_50789_, MaterialColor p_50790_) {
		return new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.WOOD, (p_152624_) -> p_152624_.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? p_50789_ : p_50790_).strength(2.0F).sound(SoundType.WOOD));
	}
	private static <T extends Block> RegistryObject<T> registerBlock(final String name,
			final java.util.function.Supplier<? extends T> block) {
		return BLOCKS.register(name, block);
	}

	private static <T extends Block> RegistryObject<T> register(final String name, final java.util.function.Supplier<? extends T> block,
			Function<RegistryObject<T>, Supplier<? extends Item>> item) {
		RegistryObject<T> obj = registerBlock(name, block);
		ITEMS.register(name, item.apply(obj));
		return obj;
	}
}