package com.alessandro.silentsunken.infrastructure.inventory;

import com.alessandro.silentsunken.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.silentsunken.infrastructure.recipe.ResonantCraftingInput;
import com.alessandro.silentsunken.infrastructure.recipe.ResonantCraftingRecipe;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.neoforged.neoforge.event.EventHooks;

@NotNullParamsAndMethodsReturn
public class ResonantResultSlot extends Slot {
    private final Player player;
    private final ResonantCraftingGridView grid;
    private final Slot catalystSlot;
    private int removeCount;

    public ResonantResultSlot(Player player, ResonantCraftingGridView grid, Slot catalystSlot, Container resultContainer, int id, int x, int y) {
        super(resultContainer, id, x, y);
        this.player = player;
        this.grid = grid;
        this.catalystSlot = catalystSlot;
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return false;
    }

    @Override
    public ItemStack remove(int amount) {
        if (hasItem()) {
            removeCount += Math.min(amount, getItem().getCount());
        }

        return super.remove(amount);
    }

    @Override
    protected void onQuickCraft(ItemStack picked, int count) {
        removeCount += count;
        checkTakeAchievements(picked);
    }

    @Override
    protected void onSwapCraft(int count) {
        removeCount += count;
    }

    @Override
    protected void checkTakeAchievements(ItemStack carried) {
        if (removeCount > 0) {
            carried.onCraftedBy(player, removeCount);
            EventHooks.firePlayerCraftingEvent(player, carried, grid);
        }

        if (container instanceof RecipeCraftingHolder holder) {
            holder.awardUsedRecipes(player, grid.getItems());
        }
        removeCount = 0;
    }

    @Override
    public void onTake(Player takingPlayer, ItemStack carried) {
        checkTakeAchievements(carried);

        var recipeUsed = container instanceof RecipeCraftingHolder holder ? holder.getRecipeUsed() : null;
        if (recipeUsed != null && recipeUsed.value() instanceof ResonantCraftingRecipe resonantRecipe) {
            var input = new ResonantCraftingInput(grid.asCraftInput(), catalystSlot.getItem());
            var remaining = resonantRecipe.getRemainingItems(input);

            for (var slot = 0; slot < grid.getContainerSize(); slot++) {
                applyRemaining(grid, slot, remaining.get(slot));
            }

            applyRemaining(catalystSlot.container, catalystSlot.getContainerSlot(), remaining.get(grid.getContainerSize()));
        } else if (recipeUsed != null && recipeUsed.value() instanceof CraftingRecipe vanillaRecipe) {
            var input = grid.asCraftInput();
            var remaining = vanillaRecipe.getRemainingItems(input);
            for (var slot = 0; slot < grid.getContainerSize(); slot++) {
                applyRemaining(grid, slot, remaining.get(slot));
            }
        } else {
            for (var slot = 0; slot < grid.getContainerSize(); slot++) {
                if (!grid.getItem(slot).isEmpty()) { grid.removeItem(slot, 1); }
            }
        }
    }

    private void applyRemaining(Container target, int slot, ItemStack remaining) {
        var current = target.getItem(slot);

        if (!current.isEmpty()) {
            target.removeItem(slot, 1);
            current = target.getItem(slot);
        }

        if (remaining.isEmpty()) { return; }

        if (current.isEmpty()) {
            target.setItem(slot, remaining);
        } else if (ItemStack.isSameItemSameComponents(current, remaining)) {
            remaining.grow(current.getCount());
            target.setItem(slot, remaining);
        } else if (!player.getInventory().add(remaining)) {
            player.drop(remaining, false);
        }
    }

    @Override
    public boolean isFake() {
        return true;
    }
}
