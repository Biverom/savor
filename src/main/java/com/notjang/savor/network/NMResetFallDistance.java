package com.notjang.savor.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class NMResetFallDistance implements IAmNetMessage {

    public NMResetFallDistance() {
    }

    public NMResetFallDistance(FriendlyByteBuf buffer) {
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {

    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() ->
                Objects.requireNonNull(supplier.get().getSender()).fallDistance = 0);
        supplier.get().setPacketHandled(true);
    }
}
