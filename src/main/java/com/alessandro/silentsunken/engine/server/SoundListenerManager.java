package com.alessandro.silentsunken.engine.server;

import com.alessandro.silentsunken.api.nullability.NotNullParams;
import com.alessandro.silentsunken.api.resonance.SoundSensible;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

@NotNullParams
public class SoundListenerManager {
    private final Map<ServerLevel, LongSet> blockEntityListenersByLevel = new IdentityHashMap<>();

    public void register(ServerLevel level, BlockPos pos) {
        blockEntityListenersByLevel.computeIfAbsent(level, _ -> new LongOpenHashSet()).add(pos.asLong());
    }

    public void unregister(ServerLevel level, BlockPos pos) {
        var positions = blockEntityListenersByLevel.get(level);

        if (positions != null) {
            positions.remove(pos.asLong());
        }
    }

    public void forEachInRange(ServerLevel level, Vec3 center, BiConsumer<BlockPos, SoundSensible> consumer) {
        var positions = blockEntityListenersByLevel.get(level);
        if (positions == null || positions.isEmpty()) { return; }

        var iterator = positions.iterator();
        while (iterator.hasNext()) {
            var pos = BlockPos.of(iterator.nextLong());

            if (!(level.getBlockEntity(pos) instanceof SoundSensible soundSensible)) {
                iterator.remove();
                continue;
            }

            var radius = soundSensible.interceptSoundRadius();
            if (Vec3.atCenterOf(pos).distanceToSqr(center) <= (double) radius * radius) {
                consumer.accept(pos, soundSensible);
            }
        }
    }
}
