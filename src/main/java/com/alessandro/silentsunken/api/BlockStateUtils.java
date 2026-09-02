package com.alessandro.silentsunken.api;

import com.alessandro.silentsunken.api.nullability.NotNullParams;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

@NotNullParams
public class BlockStateUtils {
    public static BlockState copyProperties(BlockState originalState, Block newBlock) {
        return BlockStateUtils.copyProperties(originalState, newBlock.defaultBlockState());
    }

    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> BlockState copyProperties(BlockState originalState, BlockState newState) {
        var toReturn = newState;
        for (var property : originalState.getProperties()) {
            toReturn = toReturn.trySetValue((Property<T>) property, (T) originalState.getValue(property));
        }

        return toReturn;
    }
}
