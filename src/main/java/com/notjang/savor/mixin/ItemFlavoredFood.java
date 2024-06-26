package com.notjang.savor.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemFlavoredFood {

    @Inject(method = "use", at = @At(value = "INVOKE", target = "net/minecraft/world/item/ItemStack.getFoodProperties (Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/food/FoodProperties;", ordinal = 0), cancellable = true)
    private void use(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        ItemStack itemstack = player.getItemInHand(hand);
        if ((itemstack.getTag() != null ? itemstack.getTag().getByte("FlavorModifier") : 0) > 0) {
            player.startUsingItem(hand);
            cir.setReturnValue(InteractionResultHolder.consume(itemstack));
            cir.cancel();
        }
    }
}
