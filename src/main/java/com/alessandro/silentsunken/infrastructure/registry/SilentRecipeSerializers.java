package com.alessandro.silentsunken.infrastructure.registry;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.infrastructure.recipe.ResonantCraftingRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SilentRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, SilentSunken.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ResonantCraftingRecipe>> RESONANT_CRAFTING = RECIPE_SERIALIZERS.register("resonant_crafting", () -> new RecipeSerializer<>(ResonantCraftingRecipe.MAP_CODEC, ResonantCraftingRecipe.STREAM_CODEC));
}
