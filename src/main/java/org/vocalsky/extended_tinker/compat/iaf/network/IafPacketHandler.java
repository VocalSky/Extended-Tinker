package org.vocalsky.extended_tinker.compat.iaf.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.vocalsky.extended_tinker.Extended_tinker;

public class IafPacketHandler {
    private static final String VERSION = "1.0";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(Extended_tinker.getResource("iaf_packet"), () -> VERSION, (version) -> version.equals(VERSION), (version) -> version.equals(VERSION));
    private static int packet_id = 0;

    public static void Init() {
    }

    public static <MSG> void sendToServer(MSG msg){
        INSTANCE.sendToServer(msg);
    }

    public static <MSG> void sendToPlayer(MSG msg, ServerPlayer player){
        INSTANCE.send(PacketDistributor.PLAYER.with(()->player),msg);
    }
}