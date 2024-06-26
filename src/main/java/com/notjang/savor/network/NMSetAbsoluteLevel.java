package com.notjang.savor.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class NMSetAbsoluteLevel implements IAmNetMessage {

    public int level;
    //public int duration;

    public NMSetAbsoluteLevel(int level) {
        this.level = level;
        System.out.println(this.level);
        //this.duration = duration;
    }

    public NMSetAbsoluteLevel(FriendlyByteBuf buffer) {
        this.level = buffer.readInt();
        System.out.println("Decoded with " + level);
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        System.out.println("Encoded with " + this.level);
        buffer.writeInt(this.level);
        //buffer.writeInt(duration);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> supplier) {
        supplier.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientNMSetAbsoluteLevelHandlerClass.handlePacket(this, supplier))
        );
        supplier.get().setPacketHandled(true);
    }
}
