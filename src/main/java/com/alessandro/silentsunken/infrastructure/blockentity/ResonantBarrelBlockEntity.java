package com.alessandro.silentsunken.infrastructure.blockentity;

import com.alessandro.silentsunken.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.silentsunken.infrastructure.registry.SilentBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.state.BlockState;

@NotNullParamsAndMethodsReturn
public class ResonantBarrelBlockEntity extends BaseResonantBarrelBlockEntity {
    private static final int CONTAINER_SIZE = 36;
    private static final Component NAME = Component.translatable("container.silentsunken.resonant_crate");

    public ResonantBarrelBlockEntity(BlockPos worldPos, BlockState blockState) {
        super(SilentBlockEntities.RESONANT_BARREL.get(), worldPos, blockState);
    }

    @Override
    protected Component getDefaultName() {
        return NAME;
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new ChestMenu(MenuType.GENERIC_9x4, containerId, inventory, this, 4);
    }

    @Override
    public int getContainerSize() {
        return CONTAINER_SIZE;
    }
}
