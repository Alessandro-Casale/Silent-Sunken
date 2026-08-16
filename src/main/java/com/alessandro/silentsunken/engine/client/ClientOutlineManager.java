package com.alessandro.silentsunken.engine.client;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.api.nullability.NotNullParams;
import com.alessandro.silentsunken.api.session.ScanSession;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;

@NotNullParams
public class ClientOutlineManager {
    private final List<ScanSession> sessions = new ArrayList<>();

    public void startScanSession(BlockPos clickedPos, int radius, int searchDurationSeconds, int outlineDurationSeconds, LongSet positions) {
        SilentSunken.LOGGER.debug("Starting scan session at {} with radius {} and {} blocks to highlight", clickedPos, radius, positions.size());

        sessions.add(new ScanSession(clickedPos, radius, searchDurationSeconds, outlineDurationSeconds, positions));
    }

    public List<ScanSession> getSessions() {
        return sessions;
    }

    public boolean hasActiveSessions() {
        return !sessions.isEmpty();
    }

    public void removeExpiredSessions(long currentNanos) {
        sessions.removeIf(session -> session.isFinished(currentNanos));
    }
}
