package com.notjang.savor.init;

import com.notjang.savor.recipe.ApplyFlavorRecipe;
import com.notjang.savor.SavorMod;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeInit {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SavorMod.MOD_ID);

    public static final RegistryObject<SimpleRecipeSerializer<ApplyFlavorRecipe>> FOOD_FLAVORING = RECIPE_SERIALIZERS.register("crafting_special_foodflavoring", () -> new SimpleRecipeSerializer<>(ApplyFlavorRecipe::new));
}
