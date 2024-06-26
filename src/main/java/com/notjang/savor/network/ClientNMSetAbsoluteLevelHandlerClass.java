package com.notjang.savor.network;

import com.notjang.savor.ClientProxy;
import com.notjang.savor.CommonProxy;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class ClientNMSetAbsoluteLevelHandlerClass {
    public static void handlePacket(NMSetAbsoluteLevel msg, Supplier<NetworkEvent.Context> ctx) {
        CommonProxy.setAbsoluteLevel(ClientProxy.minecraft.player, msg.level, false);
    }
}