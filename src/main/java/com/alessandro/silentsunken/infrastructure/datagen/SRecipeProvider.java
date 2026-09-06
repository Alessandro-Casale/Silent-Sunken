package com.alessandro.silentsunken.infrastructure.datagen;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.silentsunken.infrastructure.recipe.ResonantCraftingRecipe;
import com.alessandro.silentsunken.infrastructure.registry.SilentBlocks;
import com.alessandro.silentsunken.infrastructure.registry.SilentItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.block.Blocks;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@NotNullParamsAndMethodsReturn
public class SRecipeProvider extends RecipeProvider {
    protected SRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        var pattern = ShapedRecipePattern.of(
            Map.of(
                'X', Ingredient.of(SilentItems.RESONANT_CRYSTAL.get()),
                'Y', Ingredient.of(Items.STICK)
            ),
            "XXX",
            " Y ",
            " Y "
        );

        var catalyst = Ingredient.of(SilentItems.BLUE_FRAGMENTS_AND_TABLES.get(5).get());
        var result = new ItemStackTemplate(SilentItems.RESONANT_PICKAXE.get());
        var recipe = new ResonantCraftingRecipe(pattern, catalyst, result);

        this.output.accept(
            ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(SilentSunken.MODID, "resonant_pickaxe")),
            recipe,
            null
        );

        ShapedRecipeBuilder.shaped(this.items, RecipeCategory.MISC, SilentBlocks.RESONANT_CRAFTING_TABLE.get())
            .pattern("XRX")
            .pattern("GAG")
            .pattern("XGX")
            .define('X', Items.SPRUCE_LOG)
            .define('R', SilentItems.RESONANT_CRYSTAL.get())
            .define('G', Blocks.GOLD_BLOCK)
            .define('A', Blocks.CRAFTING_TABLE)
            .unlockedBy("has_resonant_crystal", this.has(SilentItems.RESONANT_CRYSTAL.get()))
            .save(this.output, ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(SilentSunken.MODID, "resonant_crafting_table")));
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new SRecipeProvider(registries, output);
        }

        @Override
        public String getName() {
            return "Silent Sunken Recipes";
        }
    }
}
