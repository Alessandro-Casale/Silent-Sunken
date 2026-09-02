package com.alessandro.silentsunken.infrastructure.blockentity;

import com.alessandro.silentsunken.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.silentsunken.infrastructure.registry.SilentBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.level.block.state.BlockState;

@NotNullParamsAndMethodsReturn
public class MossyResonantBarrelBlockEntity extends BaseResonantBarrelBlockEntity {
    private static final int CONTAINER_SIZE = 27;
    private static final Component NAME = Component.translatable("container.silentsunken.mossy_resonant_crate");

    public MossyResonantBarrelBlockEntity(BlockPos worldPos, BlockState blockState) {
        super(SilentBlockEntities.MOSSY_RESONANT_CRATE.get(), worldPos, blockState);
    }

    @Override
    protected Component getDefaultName() {
        return NAME;
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return ChestMenu.threeRows(containerId, inventory, this);
    }

    @Override
    public int getContainerSize() {
        return CONTAINER_SIZE;
    }
}
