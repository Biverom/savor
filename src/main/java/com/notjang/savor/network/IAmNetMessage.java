package com.notjang.savor.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface IAmNetMessage
{
    void encode(FriendlyByteBuf buffer);

    void handle(Supplier<NetworkEvent.Context> supplier);
}
