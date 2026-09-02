package com.alessandro.silentsunken.api;

import com.alessandro.silentsunken.api.nullability.NotNullParams;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

@NotNullParams
public class LootUtils {
    public static void empty(LootPool.Builder builder) {
        empty(builder, 1);
    }

    public static void empty(LootPool.Builder builder, int weight) {
        builder.add(EmptyLootItem.emptyItem().setWeight(weight)).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)));
    }

    public static void item(LootPool.Builder builder, ItemLike item) {
        item(builder, item, 1, 1);
    }

    public static void item(LootPool.Builder builder, ItemLike item, int weight, float count) {
        builder.add(LootItem.lootTableItem(item).setWeight(weight)).apply(SetItemCountFunction.setCount(ConstantValue.exactly(count)));
    }

    public static void item(LootPool.Builder builder, ItemLike item, int weight, float minCount, float maxCount) {
        builder.add(LootItem.lootTableItem(item).setWeight(weight)).apply(SetItemCountFunction.setCount(UniformGenerator.between(minCount, maxCount)));
    }
}
