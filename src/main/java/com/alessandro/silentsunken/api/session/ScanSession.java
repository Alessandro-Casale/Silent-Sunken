package com.alessandro.silentsunken.api.session;

import com.alessandro.silentsunken.api.TimeUtils;
import com.alessandro.silentsunken.api.nullability.NotNullParamsAndMethodsReturn;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

@NotNullParamsAndMethodsReturn
public class ScanSession {
    private final Vec3 origin;
    private final int radius;
    private final long startTimeNanos;
    private final long endTimeNanos;

    private final List<Target> associatedTargets = new ArrayList<>();

    public ScanSession(BlockPos origin, int radius, int searchDurationSeconds, int outlineDurationSeconds, LongSet positions) {
        this.origin = Vec3.atCenterOf(origin);
        this.radius = radius;
        this.startTimeNanos = TimeUtils.getTimeNanos();
        this.endTimeNanos = startTimeNanos + TimeUtils.secondsToNanos(searchDurationSeconds);

        for (var position : positions) {
            var pos = BlockPos.of(position);
            var distance = pos.distToCenterSqr(this.origin);

            long revealTimeNanos = startTimeNanos + (long) (distance / radius * searchDurationSeconds);
            long endTimeNanos = revealTimeNanos + TimeUtils.secondsToNanos(outlineDurationSeconds);

            associatedTargets.add(Target.target(pos, revealTimeNanos, endTimeNanos));
        }
    }

    public List<Target> targets() {
        return associatedTargets;
    }

    public Vec3 getOrigin() {
        return origin;
    }

    public int getRadius() {
        return radius;
    }

    public long getStartTime() {
        return startTimeNanos;
    }

    public long getEndTime() {
        return endTimeNanos;
    }

    public boolean isSearchActive(long currentNanos) {
        return currentNanos < endTimeNanos;
    }

    public boolean isFinished(long currentNanos) {
        if (isSearchActive(currentNanos)) { return false; }

        for (var target : associatedTargets) {
            if (currentNanos < target.getEndTime()) { return false; }
        }

        return true;
    }

    public static class Target {
        private final Vec3 vectorPos;
        private final BlockPos pos;
        private final long revealTimeNanos;
        private final long endTimeNanos;

        private boolean revealed = false;

        private Target(BlockPos pos, long revealTimeNanos, long endTimeNanos) {
            this.vectorPos = Vec3.atLowerCornerOf(pos);
            this.pos = pos;
            this.revealTimeNanos = revealTimeNanos;
            this.endTimeNanos = endTimeNanos;
        }

        public static Target target(BlockPos pos, long revealTimeNanos, long endTimeNanos) {
            return new Target(pos, revealTimeNanos, endTimeNanos);
        }

        public BlockPos getBlockPos() {
            return pos;
        }

        public Vec3 getVectorPos() {
            return vectorPos;
        }

        public long getRevealTime() {
            return revealTimeNanos;
        }

        public long getEndTime() {
            return endTimeNanos;
        }

        public boolean isRevealed() {
            return revealed;
        }

        public boolean isExpired(long currentNanos) {
            return currentNanos >= endTimeNanos;
        }

        public void reveal() {
            revealed = true;
        }
    }
}
