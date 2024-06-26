package com.notjang.savor.mixin;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerFlavoredFood {

    @Unique
    private LocalPlayer savor$self() {
        return (LocalPlayer) (Object) this;
    }

    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "net/minecraft/client/player/LocalPlayer.isUsingItem ()Z", ordinal = 0))
    private void aiStepPrefix(CallbackInfo info) {
        if (savor$self().isUsingItem() && !savor$self().isPassenger()) {
            ItemStack itemstack = savor$self().getUseItem();
            if ((itemstack.getTag() != null ? itemstack.getTag().getByte("FlavorModifier") : 0) > 0) {
                savor$self().input.leftImpulse *= 5.0F;
                savor$self().input.forwardImpulse *= 5.0F;
            }
        }
    }
}
