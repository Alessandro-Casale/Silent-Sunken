package com.alessandro.silentsunken.api;

import com.alessandro.silentsunken.api.nullability.NotNullParams;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.Container;
import net.minecraft.world.level.storage.TagValueInput;
import org.jspecify.annotations.Nullable;

@NotNullParams
public class BlockEntityUtils {
    public static @Nullable CompoundTag captureData(ServerLevel level, BlockPos pos) {
        var blockEntity = level.getBlockEntity(pos);
        if (blockEntity == null) { return null; }

        var data = blockEntity.saveWithoutMetadata(level.registryAccess());

        if (blockEntity instanceof Container container) {
            // Prevent container drop (item duplication)
            container.clearContent();
        }

        return data;
    }

    public static void restoreData(ServerLevel level, BlockPos pos, @Nullable CompoundTag data) {
        if (data == null) { return; }

        var newBlockEntity = level.getBlockEntity(pos);
        if (newBlockEntity == null) { return; }

        newBlockEntity.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, level.registryAccess(), data));
        newBlockEntity.setChanged();
    }
}
