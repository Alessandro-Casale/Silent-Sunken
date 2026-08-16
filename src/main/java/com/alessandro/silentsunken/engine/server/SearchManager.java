package com.alessandro.silentsunken.engine.server;

import com.alessandro.silentsunken.api.nullability.NotNullParamsAndMethodsReturn;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Predicate;

@NotNullParamsAndMethodsReturn
public class SearchManager {
    public Long2ObjectMap<BlockState> sphericSearch(ServerLevel level, BlockPos center, int radius, Predicate<BlockState> isSearched) {
        var toReturn = new Long2ObjectOpenHashMap<BlockState>();

        var from = center.offset(-radius, -radius, -radius);
        var to = center.offset(radius, radius, radius);

        var squaredRadius = radius * radius;

        for (var pos : BlockPos.betweenClosed(from, to)) {
            if (pos.distSqr(center) <= squaredRadius) {
                var state = level.getBlockState(pos);

                if (isSearched.test(state)) {
                    toReturn.put(pos.asLong(), state);
                }
            }
        }

        return toReturn;
    }
}
