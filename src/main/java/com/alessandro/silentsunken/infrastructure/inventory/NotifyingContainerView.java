package com.alessandro.silentsunken.infrastructure.inventory;

import com.alessandro.silentsunken.api.nullability.NotNullParamsAndMethodsReturn;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

@NotNullParamsAndMethodsReturn
public class NotifyingContainerView implements Container {
    private final Container delegate;
    private final AbstractContainerMenu menu;

    public NotifyingContainerView(Container delegate, AbstractContainerMenu menu) {
        this.delegate = delegate;
        this.menu = menu;
    }

    @Override
    public int getContainerSize() {
        return delegate.getContainerSize();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return delegate.getItem(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        var result = delegate.removeItem(slot, amount);

        if (!result.isEmpty()) {
            menu.slotsChanged(this);
        }

        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return delegate.removeItemNoUpdate(slot);
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        delegate.setItem(slot, itemStack);
        menu.slotsChanged(this);
    }

    @Override
    public void setChanged() {
        delegate.setChanged();
        menu.slotsChanged(this);
    }

    @Override
    public boolean stillValid(Player player) {
        return delegate.stillValid(player);
    }

    @Override
    public void clearContent() {
        delegate.clearContent();
        menu.slotsChanged(this);
    }
}
