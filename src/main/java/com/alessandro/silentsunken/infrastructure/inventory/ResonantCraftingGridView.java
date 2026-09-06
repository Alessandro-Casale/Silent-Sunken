package com.alessandro.silentsunken.infrastructure.inventory;

import com.alessandro.silentsunken.api.nullability.NotNullParamsAndMethodsReturn;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

@NotNullParamsAndMethodsReturn
public class ResonantCraftingGridView implements CraftingContainer {
    public static final int WIDTH = 3;
    public static final int HEIGHT = 3;
    public static final int SIZE = WIDTH * HEIGHT;

    private final Container delegate;

    public ResonantCraftingGridView(Container delegate) {
        this.delegate = delegate;
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public List<ItemStack> getItems() {
        List<ItemStack> items = new ArrayList<>(SIZE);

        for (var slot = 0; slot < SIZE; slot++) {
            items.add(delegate.getItem(slot));
        }

        return items;
    }

    @Override
    public void fillStackedContents(StackedItemContents contents) {
        for (var slot = 0; slot < SIZE; slot++) {
            contents.accountStack(delegate.getItem(slot), 1);
        }
    }

    @Override
    public int getContainerSize() { return SIZE; }

    @Override
    public boolean isEmpty() {
        for (var slot = 0; slot < SIZE; slot++) {
            if (!delegate.getItem(slot).isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return delegate.getItem(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return delegate.removeItem(slot, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return delegate.removeItemNoUpdate(slot);
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        delegate.setItem(slot, itemStack);
    }

    @Override
    public void setChanged() {
        delegate.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return delegate.stillValid(player);
    }

    @Override
    public void clearContent() {
        for (var slot = 0; slot < SIZE; slot++) {
            delegate.setItem(slot, ItemStack.EMPTY);
        }
    }
}
