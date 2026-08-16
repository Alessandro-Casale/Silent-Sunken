package com.alessandro.silentsunken.infrastructure.network;

import com.alessandro.silentsunken.api.nullability.NotNull;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface SilentPacket extends CustomPacketPayload {
    void run(IPayloadContext context);

    default void handle(@NotNull IPayloadContext context) {
        context.enqueueWork(() -> run(context));
    }
}
