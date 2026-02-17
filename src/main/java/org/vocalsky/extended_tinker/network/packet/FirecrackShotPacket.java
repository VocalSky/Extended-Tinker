//package org.vocalsky.extended_tinker.network.packet;
//
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraftforge.network.NetworkEvent;
//
//import java.util.function.Supplier;
//
//public class FirecrackShotPacket {
//    int playerId;
//
//    public FirecrackShotPacket(int playerId) {
//        this.playerId = playerId;
//    }
//
//    public static void encode(FirecrackShotPacket packet, FriendlyByteBuf buf) {
//        buf.writeInt(packet.playerId);
//    }
//
//    public static FirecrackShotPacket decode(FriendlyByteBuf buf) {
//        return new FirecrackShotPacket(buf.readInt());
//    }
//
//    public static void handle(FirecrackShotPacket packet, Supplier<NetworkEvent.Context> supplier) {
//        if (supplier.get().getDirection().getReceptionSide().isServer()) {
//            supplier.get().enqueueWork(() -> {
//                ServerPlayer player = supplier.get().getSender();
//                if (player != null && player.getId() == packet.playerId)
//                    fireworkRocketShot(player);
//            });
//        }
//        supplier.get().setPacketHandled(true);
//    }
//}