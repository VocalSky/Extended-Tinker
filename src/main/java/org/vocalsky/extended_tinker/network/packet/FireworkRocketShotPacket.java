package org.vocalsky.extended_tinker.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import static org.vocalsky.extended_tinker.common.tool.Firecrack.fireworkRocketShot;

import java.util.function.Supplier;

public class FireworkRocketShotPacket {
    int playerId;

    public FireworkRocketShotPacket(int playerId) {
        this.playerId = playerId;
    }

    public static void encode(FireworkRocketShotPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.playerId);
    }

    public static FireworkRocketShotPacket decode(FriendlyByteBuf buf) {
        return new FireworkRocketShotPacket(buf.readInt());
    }

    public static void handle(FireworkRocketShotPacket packet, Supplier<NetworkEvent.Context> supplier) {
        if (supplier.get().getDirection().getReceptionSide().isServer()) {
            supplier.get().enqueueWork(() -> {
                ServerPlayer player = supplier.get().getSender();
                if (player != null && player.getId() == packet.playerId)
                    fireworkRocketShot(player);
            });
        }
        supplier.get().setPacketHandled(true);
    }
}