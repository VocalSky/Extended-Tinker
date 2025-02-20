package org.vocalsky.extended_tinker.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.vocalsky.extended_tinker.Extended_tinker;
import org.vocalsky.extended_tinker.network.packet.FireworkRocketShotPacket;

public class PacketHandler {
    private static final String VERSION = "1.0";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(Extended_tinker.MODID, "tinker_packet"), () -> { return VERSION; }, (version) -> { return version.equals(VERSION); }, (version) -> { return version.equals(VERSION); });
    private static int packet_id = 0;

    public static void Init() {
        INSTANCE.registerMessage(packet_id++, FireworkRocketShotPacket.class, FireworkRocketShotPacket::encode, FireworkRocketShotPacket::decode, FireworkRocketShotPacket::handle);
    }

    public static <MSG> void sendToServer(MSG msg){
        INSTANCE.sendToServer(msg);
    }

    public static <MSG> void sendToPlayer(MSG msg, ServerPlayer player){
        INSTANCE.send(PacketDistributor.PLAYER.with(()->player),msg);
    }
}