package com.alessandro.silentsunken.engine.server;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.api.nullability.NotNullParams;
import com.alessandro.silentsunken.infrastructure.network.packet.HighlightBlocksS2C;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;

@NotNullParams
public class OutlineManager {
    public void startScanSession(ServerLevel level, BlockPos clickedPos, int radius, int searchDurationSeconds, int outlineDurationSeconds, LongSet positions) {
        SilentSunken.LOGGER.debug("Starting scan session at {} with radius {} and {} blocks to highlight", clickedPos, radius, positions.size());

        PacketDistributor.sendToPlayersNear(
            level, null,
            clickedPos.getX(),
            clickedPos.getY(),
            clickedPos.getZ(),
            applyRadiusCorrection(radius),
            new HighlightBlocksS2C(clickedPos.asLong(), radius, searchDurationSeconds, outlineDurationSeconds, positions)
        );
    }

    public void startScanSession(ServerLevel level, BlockPos clickedPos, int radius, int searchDurationSeconds, int outlineDurationSeconds, Long2ObjectMap<BlockState> positions) {
        startScanSession(level, clickedPos, radius, searchDurationSeconds, outlineDurationSeconds, positions.keySet());
    }

    private int applyRadiusCorrection(int originalRadius) {
        return originalRadius + originalRadius / 2;
    }
}
