package com.alessandro.silentsunken.infrastructure.registry;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.infrastructure.recipe.ResonantCraftingRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SilentRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, SilentSunken.MODID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<ResonantCraftingRecipe>> RESONANT_CRAFTING = RECIPE_TYPES.register("resonant_crafting", () -> RecipeType.simple(Identifier.fromNamespaceAndPath(SilentSunken.MODID, "resonant_crafting")));
}
