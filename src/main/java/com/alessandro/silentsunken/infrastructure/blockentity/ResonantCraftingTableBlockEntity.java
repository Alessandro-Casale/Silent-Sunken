package com.alessandro.silentsunken.infrastructure.blockentity;

import com.alessandro.silentsunken.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.silentsunken.infrastructure.inventory.ResonantCraftingMenu;
import com.alessandro.silentsunken.infrastructure.registry.SilentBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

@NotNullParamsAndMethodsReturn
public class ResonantCraftingTableBlockEntity extends BaseContainerBlockEntity {
    public static final int GRID_SIZE = 9;
    public static final int CATALYST_SLOT = GRID_SIZE;
    public static final int CONTAINER_SIZE = GRID_SIZE + 1;
    private static final Component NAME = Component.translatable("container.silentsunken.resonant_crafting_table");

    private NonNullList<ItemStack> items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);

    public ResonantCraftingTableBlockEntity(BlockPos worldPos, BlockState blockState) {
        super(SilentBlockEntities.RESONANT_CRAFTING_TABLE.get(), worldPos, blockState);
    }

    @Override
    protected Component getDefaultName() {
        return NAME;
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new ResonantCraftingMenu(containerId, inventory, this, ContainerLevelAccess.create(level, worldPosition));
    }

    @Override
    public int getContainerSize() {
        return CONTAINER_SIZE;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(input, items);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, items);
    }
}
