package com.alessandro.silentsunken.infrastructure.network;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.api.nullability.NotNullParams;
import com.alessandro.silentsunken.api.nullability.Nullable;
import com.alessandro.silentsunken.infrastructure.network.packet.HighlightBlocksS2C;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;

@NotNullParams
@EventBusSubscriber(modid = SilentSunken.MODID)
public class Networking {
    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1.0.0").executesOn(HandlerThread.NETWORK);

        registrar.playToClient(HighlightBlocksS2C.TYPE, HighlightBlocksS2C.STREAM_CODEC, HighlightBlocksS2C::handle);
    }

    public static void sendTo(@Nullable ServerPlayer player, CustomPacketPayload packet) {
        if (player == null) {
            PacketDistributor.sendToAllPlayers(packet);
        } else {
            PacketDistributor.sendToPlayer(player, packet);
        }
    }
}
