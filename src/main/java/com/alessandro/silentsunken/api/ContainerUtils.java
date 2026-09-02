package com.alessandro.silentsunken.api;

import com.alessandro.silentsunken.api.nullability.NotNullParams;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;

@NotNullParams
public class ContainerUtils {
    public static boolean areContainersCompatible(ServerLevel level, BlockPos pos, BlockState newState) {
        var entity = level.getBlockEntity(pos);
        if (entity instanceof Container originalContainer) {
            var originalSize = originalContainer.getContainerSize();

            if (newState.getBlock() instanceof EntityBlock entityBlock) {
                var newEntity = entityBlock.newBlockEntity(pos, newState);

                if (newEntity instanceof Container newContainer) {
                    var newSize = newContainer.getContainerSize();

                    if (originalSize <= newSize) {
                        return true;
                    }

                    for (int index = newSize; index < originalSize; index++) {
                        if (!originalContainer.getItem(index).isEmpty()) {
                            return false;
                        }
                    }

                    return true;
                }
            }
        }

        return true;
    }
}
