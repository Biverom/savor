package com.notjang.savor.network;

import com.notjang.savor.SavorMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.lang.reflect.InvocationTargetException;

public final class PacketHandler {

    public static final String PROTOCOL = "1";

    public static SimpleChannel INSTANCE;
    private static int id = 0;

    public static void initialize()
    {
        INSTANCE = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(SavorMod.MOD_ID, "network"))
                .networkProtocolVersion(() -> PROTOCOL)
                .clientAcceptedVersions(PROTOCOL::equals)
                .serverAcceptedVersions(PROTOCOL::equals)
                .simpleChannel();
        register(NMResetFallDistance.class);
        register(NMSetAbsoluteLevel.class);
    }

    private static <T extends IAmNetMessage> void register(Class<T> clazz)
    {
        INSTANCE.registerMessage(id++, clazz, IAmNetMessage::encode, buf -> {
            try {
                return clazz.getConstructor(new Class[] {FriendlyByteBuf.class}).newInstance(buf);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }, IAmNetMessage::handle);
    }

}
