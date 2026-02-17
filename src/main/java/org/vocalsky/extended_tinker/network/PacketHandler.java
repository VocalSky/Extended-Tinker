package org.vocalsky.extended_tinker.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.vocalsky.extended_tinker.Extended_tinker;

public class PacketHandler {
    private static final String VERSION = "1.0";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(Extended_tinker.getResource("tinker_packet"), () -> VERSION, (version) -> version.equals(VERSION), (version) -> version.equals(VERSION));
    private static int packet_id = 0;

    public static void Init() {
//        INSTANCE.registerMessage(packet_id++, FirecrackShotPacket.class, FirecrackShotPacket::encode, FirecrackShotPacket::decode, FirecrackShotPacket::handle);
    }

    public static <MSG> void sendToServer(MSG msg){
        INSTANCE.sendToServer(msg);
    }

    public static <MSG> void sendToPlayer(MSG msg, ServerPlayer player){
        INSTANCE.send(PacketDistributor.PLAYER.with(()->player),msg);
    }
}