package com.notjang.savor;

import com.notjang.savor.init.ItemInit;
import com.notjang.savor.init.RecipeInit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ApplyFlavorRecipe extends CustomRecipe {
    public ApplyFlavorRecipe(ResourceLocation p_43833_) {
        super(p_43833_);
    }

    @Override
    public boolean matches(CraftingContainer container, @NotNull Level level) {
        boolean foundFood = false;
        boolean foundFlavour = false;

        for(int i = 0; i < container.getContainerSize(); ++i) {
            ItemStack itemstack = container.getItem(i);
            if (!itemstack.isEmpty()) {
                if (itemstack.getItem() == ItemInit.SALT.get() || itemstack.getItem() == Items.SUGAR) {
                    if (!foundFlavour) {
                        foundFlavour = true;
                    }
                    else {
                        return false;
                    }
                } else if (itemstack.getItem().isEdible()) {
                    if (!foundFood) {
                        foundFood = true;
                        if ((itemstack.getTag() != null ? itemstack.getTag().getByte("FlavorModifier") : 0) > 0) {
                            return false;
                        }
                    }
                    else {
                        return false;
                    }
                }
            }
        }

        return foundFood && foundFlavour;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingContainer container) {
        byte flavor = 0;
        ItemStack food = ItemStack.EMPTY;

        for(int i = 0; i < container.getContainerSize(); ++i) {
            ItemStack itemstack = container.getItem(i);
            if (!itemstack.isEmpty()) {
                if (itemstack.getItem() == ItemInit.SALT.get()) {
                    flavor = 1;
                }
                else if (itemstack.getItem() == Items.SUGAR) {
                    flavor = 2;
                }else if (itemstack.getItem().isEdible()) {
                    food = itemstack;
                }
            }
        }

        ItemStack flavoredFood = food.copy();
        flavoredFood.setCount(1);
        flavoredFood.getOrCreateTag().putByte("FlavorModifier", flavor);

        return flavoredFood;
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeInit.FOOD_FLAVORING.get();
    }
}
