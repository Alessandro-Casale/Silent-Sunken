package com.alessandro.silentsunken.infrastructure.recipe;

import com.alessandro.silentsunken.api.nullability.NotNullParamsAndMethodsReturn;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeInput;

@NotNullParamsAndMethodsReturn
public record ResonantCraftingInput(CraftingInput grid, ItemStack catalyst) implements RecipeInput {
    @Override
    public ItemStack getItem(int index) {
        return index < grid.size() ? grid.getItem(index) : catalyst;
    }

    @Override
    public int size() {
        return grid.size() + 1;
    }
}
