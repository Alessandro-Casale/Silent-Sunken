package com.alessandro.silentsunken.infrastructure.data;

import com.alessandro.silentsunken.api.misc.Twin;
import com.alessandro.silentsunken.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.silentsunken.api.nullability.Nullable;
import com.alessandro.silentsunken.infrastructure.codec.SoundMaterial;
import com.alessandro.silentsunken.infrastructure.item.FragmentItem;
import com.alessandro.silentsunken.infrastructure.resource.SoundMaterialDefinitions;
import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

@NotNullParamsAndMethodsReturn
public class HistorianProgress implements ValueIOSerializable {
    private static final String ITEM_HOLDER_KEY = "item_holder";
    private static final String FRAGMENTS_TO_RAW_TABLE_CONVERSIONS_KEY = "fragments_to_raw_table_conversions";
    private static final String RAW_TO_GILDED_TABLE_CONVERSIONS_KEY = "raw_to_gilded_table_conversions";
    private static final String RAW_TO_GILDED_TABLE_CONVERSION_UNLOCKS_AFTER_KEY = "raw_to_gilded_table_conversion_unlocks_after";
    private static final String CALCULATED_REQUIRED_RESOURCES_KEY = "calculated_required_resources";

    private static final Codec<List<ItemStack>> ITEM_STACK_LIST_CODEC = ItemStack.OPTIONAL_CODEC.listOf();

    private final ItemStacksResourceHandler itemHolder = new ItemStacksResourceHandler(10);
    private int fragmentsToRawTabletConversions = 0;
    private int rawToGildedTabletConversions = 0;
    private int rawToGildedTabletConversionUnlocksAfter = 0;
    private List<ItemStack> calculatedRequiredResources;

    @Override
    public void serialize(ValueOutput output) {
        itemHolder.serialize(output.child(ITEM_HOLDER_KEY));
        output.putInt(FRAGMENTS_TO_RAW_TABLE_CONVERSIONS_KEY, fragmentsToRawTabletConversions);
        output.putInt(RAW_TO_GILDED_TABLE_CONVERSIONS_KEY, rawToGildedTabletConversions);
        output.putInt(RAW_TO_GILDED_TABLE_CONVERSION_UNLOCKS_AFTER_KEY, rawToGildedTabletConversionUnlocksAfter);

        if (calculatedRequiredResources != null) {
            output.store(CALCULATED_REQUIRED_RESOURCES_KEY, ITEM_STACK_LIST_CODEC, calculatedRequiredResources);
        }
    }

    @Override
    public void deserialize(ValueInput input) {
        itemHolder.deserialize(input.childOrEmpty(ITEM_HOLDER_KEY));
        fragmentsToRawTabletConversions = input.getIntOr(FRAGMENTS_TO_RAW_TABLE_CONVERSIONS_KEY, 0);
        rawToGildedTabletConversions = input.getIntOr(RAW_TO_GILDED_TABLE_CONVERSIONS_KEY, 0);
        rawToGildedTabletConversionUnlocksAfter = input.getIntOr(RAW_TO_GILDED_TABLE_CONVERSION_UNLOCKS_AFTER_KEY, 0);
        calculatedRequiredResources = input.read(CALCULATED_REQUIRED_RESOURCES_KEY, ITEM_STACK_LIST_CODEC).orElse(null);
    }

    public boolean isRawToGildedTabletConversionUnlocked() {
        return fragmentsToRawTabletConversions >= rawToGildedTabletConversionUnlocksAfter;
    }

    public void setRawToGildedTabletConversionUnlocksAfter(int rawToGildedTabletConversionUnlocksAfter) {
        this.rawToGildedTabletConversionUnlocksAfter = rawToGildedTabletConversionUnlocksAfter;
    }

    public void incrementFragmentsToRawTabletConversions() {
        fragmentsToRawTabletConversions += 1;
    }

    public void setItem(int index, ItemStack stack) {
        itemHolder.set(index, ItemResource.of(stack), stack.count());
    }

    public @Nullable ItemStack getCornerAt(int index) {
        if (!hasCornerAt(index)) { return null; }

        return itemHolder.copyToList().get(index);
    }

    public boolean hasCornerAt(int index) {
        if (index < 0 || index > 3) {
            throw new IllegalArgumentException("Index must be between 0 and 3");
        }

        return itemHolder.getAmountAsInt(index) > 0;
    }

    public boolean hasFirstCorner() {
        return hasCornerAt(0);
    }

    public boolean hasSecondCorner() {
        return hasCornerAt(1);
    }

    public boolean hasThirdCorner() {
        return hasCornerAt(2);
    }

    public boolean hasFourthCorner() {
        return hasCornerAt(3);
    }

    public boolean hasAllCorners() {
        return hasFirstCorner() && hasSecondCorner() && hasThirdCorner() && hasFourthCorner();
    }

    public int numberOfFragments() {
        return Math.toIntExact(fragmentsCopyList().count());
    }

    public Stream<ItemStack> fragmentsCopyList() {
        return itemHolder.copyToList()
            .stream()
            .filter(stack -> stack.getItem() instanceof FragmentItem);
    }

    public @Nullable Twin<ItemStack, Integer> resourceStackWaitingFor(RandomSource random) {
        var resources = calculateRequiredResourcesForConversion(random);
        if (resources == null) { return null; }

        for (int i = 0; i < resources.size(); i++) {
            var stack = resources.get(i);
            var stackInContainer = itemHolder.copyToList().get(4 + i);
            if (stackInContainer.isEmpty() || stackInContainer.count() < stack.count()) {
                return new Twin<>(stack.copyWithCount(stack.count() - stackInContainer.count()), 4 + i);
            }
        }

        return null;
    }

    public void recordRawTabletConversion() {
        fragmentsToRawTabletConversions++;

        for (int i = 0; i < itemHolder.size(); i++) {
            itemHolder.set(i, ItemResource.EMPTY, 0);
        }

        calculatedRequiredResources = null;
    }

    public @Nullable List<ItemStack> calculateRequiredResourcesForConversion(RandomSource random) {
        if (!hasAllCorners()) {
            if (calculatedRequiredResources != null) { calculatedRequiredResources = null; }

            return null;
        }

        if (calculatedRequiredResources == null || calculatedRequiredResources.isEmpty()) {
            var materials = new HashMap<SoundMaterial, Integer>();
            calculatedRequiredResources = new ArrayList<>();

            fragmentsCopyList().forEach(corner -> {
                if (corner.getItem() instanceof FragmentItem item) {
                    var value = materials.computeIfAbsent(item.getSoundMaterial(), _ -> 0);
                    materials.put(item.getSoundMaterial(), value + 1);
                }
            });

            for (var material : materials.keySet()) {
                var definition = SoundMaterialDefinitions.INSTANCE.getDefinition(material);

                var count = 0;
                for (int i = 0; i < materials.get(material); i++) {
                    count += random.nextIntBetweenInclusive(1, 3);
                }

                calculatedRequiredResources.add(new ItemStack(definition.counterpartMaterial(), count));
            }
        }

        return calculatedRequiredResources;
    }
}
