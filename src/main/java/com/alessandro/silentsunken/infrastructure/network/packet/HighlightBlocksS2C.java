package com.alessandro.silentsunken.infrastructure.network.packet;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.silentsunken.engine.SilentClientManager;
import com.alessandro.silentsunken.infrastructure.network.SilentPacket;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

@NotNullParamsAndMethodsReturn
public record HighlightBlocksS2C(long clickedPos, int radius, int searchDurationSeconds, int outlineDurationSeconds, LongSet blockPosList) implements SilentPacket {
    public static final Type<HighlightBlocksS2C> TYPE = new Type<>(Identifier.fromNamespaceAndPath(SilentSunken.MODID, "highlight_block_s2c"));

    public static final StreamCodec<ByteBuf, HighlightBlocksS2C> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.LONG, HighlightBlocksS2C::clickedPos,
        ByteBufCodecs.INT, HighlightBlocksS2C::radius,
        ByteBufCodecs.INT, HighlightBlocksS2C::searchDurationSeconds,
        ByteBufCodecs.INT, HighlightBlocksS2C::outlineDurationSeconds,
        ByteBufCodecs.collection(LongOpenHashSet::new, ByteBufCodecs.LONG), HighlightBlocksS2C::blockPosList,
        HighlightBlocksS2C::new
    );

    @Override
    public void run(IPayloadContext context) {
        SilentClientManager.OUTLINE_INSTANCE.startScanSession(BlockPos.of(clickedPos), radius, searchDurationSeconds, outlineDurationSeconds, blockPosList);
        SilentClientManager.CAMERA_SHAKE_INSTANCE.triggerForPlayer(context.player().position());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
