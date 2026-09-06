package com.alessandro.silentsunken.infrastructure.recipe;

import com.alessandro.silentsunken.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.silentsunken.infrastructure.registry.SilentRecipeSerializers;
import com.alessandro.silentsunken.infrastructure.registry.SilentRecipeTypes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.Level;

@NotNullParamsAndMethodsReturn
public class ResonantCraftingRecipe implements Recipe<ResonantCraftingInput> {
    public static final MapCodec<ResonantCraftingRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ShapedRecipePattern.MAP_CODEC.forGetter(ResonantCraftingRecipe::pattern),
        Ingredient.CODEC.fieldOf("catalyst").forGetter(ResonantCraftingRecipe::catalyst),
        ItemStackTemplate.CODEC.fieldOf("result").forGetter(ResonantCraftingRecipe::result)
    ).apply(instance, ResonantCraftingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ResonantCraftingRecipe> STREAM_CODEC = StreamCodec.composite(
        ShapedRecipePattern.STREAM_CODEC, ResonantCraftingRecipe::pattern,
        Ingredient.CONTENTS_STREAM_CODEC, ResonantCraftingRecipe::catalyst,
        ItemStackTemplate.STREAM_CODEC, ResonantCraftingRecipe::result,
        ResonantCraftingRecipe::new
    );

    private final ShapedRecipePattern pattern;
    private final Ingredient catalyst;
    private final ItemStackTemplate result;

    public ResonantCraftingRecipe(ShapedRecipePattern pattern, Ingredient catalyst, ItemStackTemplate result) {
        this.pattern = pattern;
        this.catalyst = catalyst;
        this.result = result;
    }

    public ShapedRecipePattern pattern() {
        return pattern;
    }

    public Ingredient catalyst() {
        return catalyst;
    }

    public ItemStackTemplate result() {
        return result;
    }

    @Override
    public boolean matches(ResonantCraftingInput input, Level level) {
        return catalyst.test(input.catalyst()) && pattern.matches(input.grid());
    }

    @Override
    public ItemStack assemble(ResonantCraftingInput input) {
        return result.create();
    }

    public NonNullList<ItemStack> getRemainingItems(ResonantCraftingInput input) {
        var remainingItems = NonNullList.withSize(input.size(), ItemStack.EMPTY);

        for (var slot = 0; slot < remainingItems.size(); slot++) {
            var item = input.getItem(slot);
            var remainder = item.getCraftingRemainder();
            remainingItems.set(slot, remainder != null ? remainder.create() : ItemStack.EMPTY);
        }

        return remainingItems;
    }

    @Override
    public boolean showNotification() {
        return true;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public RecipeSerializer<? extends Recipe<ResonantCraftingInput>> getSerializer() {
        return SilentRecipeSerializers.RESONANT_CRAFTING.get();
    }

    @Override
    public RecipeType<? extends Recipe<ResonantCraftingInput>> getType() {
        return SilentRecipeTypes.RESONANT_CRAFTING.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }
}
