package com.alessandro.silentsunken.infrastructure.datagen;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.api.LootUtils;
import com.alessandro.silentsunken.api.nullability.NotNullParams;
import com.alessandro.silentsunken.infrastructure.registry.SilentItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.List;
import java.util.function.BiConsumer;

@NotNullParams
public class SChestLootSubProvider implements LootTableSubProvider {
    private static final ResourceKey<LootTable> RUINS = ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(SilentSunken.MODID, "chests/ruins"));

    private static final ResourceKey<LootTable> RUINS_TOWER_SMALL = ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(SilentSunken.MODID, "chests/ruins_tower_small"));
    private static final ResourceKey<LootTable> RUINS_TOWER_BIG = ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(SilentSunken.MODID, "chests/ruins_tower_big"));

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        output.accept(RUINS_TOWER_SMALL, LootTable.lootTable()
            .withPool(salvageTowerSmall())
            .withPool(materialsTowerSmall())
        );

        output.accept(RUINS_TOWER_BIG, LootTable.lootTable()
            .withPool(salvageTowerBig())
            .withPool(suppliesTowerBig())
            .withPool(treasureTowerBig())
        );

        output.accept(RUINS, LootTable.lootTable()
            .withPool(fragments())
            .withPool(materialsRuins())
            .withPool(rareRuins())
        );
    }

    private LootPool.Builder salvageTowerSmall() {
        var pool = LootPool.lootPool().setRolls(UniformGenerator.between(6, 9));

        LootUtils.item(pool, Items.MOSSY_COBBLESTONE, 10, 1, 3);
        LootUtils.item(pool, Items.CRACKED_STONE_BRICKS, 8, 1, 2);
        LootUtils.item(pool, Items.MOSS_BLOCK, 8, 1, 2);
        LootUtils.item(pool, Items.MOSS_CARPET, 6, 1, 3);
        LootUtils.item(pool, Items.COBBLESTONE_STAIRS, 6, 1, 2);
        LootUtils.item(pool, Items.RAW_COPPER, 10, 2, 4);
        LootUtils.item(pool, Items.COPPER_NUGGET, 15, 2, 6);
        LootUtils.item(pool, Items.BONE, 6, 1, 3);
        LootUtils.item(pool, Items.STRING, 5, 1, 2);
        LootUtils.item(pool, Items.ROTTEN_FLESH, 4, 1, 2);

        return pool;
    }

    private LootPool.Builder materialsTowerSmall() {
        var pool = LootPool.lootPool().setRolls(UniformGenerator.between(2, 3));

        LootUtils.item(pool, Items.COPPER_INGOT, 10, 1, 2);
        LootUtils.item(pool, Items.IRON_NUGGET, 8, 2, 4);
        LootUtils.item(pool, Items.IRON_INGOT, 5, 1, 1);
        LootUtils.item(pool, Items.LAPIS_LAZULI, 4, 1, 3);
        LootUtils.item(pool, Items.EMERALD, 2, 1, 1);

        return pool;
    }

    private LootPool.Builder salvageTowerBig() {
        var pool = LootPool.lootPool().setRolls(UniformGenerator.between(7, 10));

        LootUtils.item(pool, Items.MOSSY_STONE_BRICKS, 10, 1, 3);
        LootUtils.item(pool, Items.MOSSY_COBBLESTONE, 9, 1, 3);
        LootUtils.item(pool, Items.CRACKED_STONE_BRICKS, 7, 1, 2);
        LootUtils.item(pool, Items.MOSS_BLOCK, 8, 1, 3);
        LootUtils.item(pool, Items.MOSS_CARPET, 6, 1, 3);
        LootUtils.item(pool, Items.RAW_COPPER, 10, 2, 4);
        LootUtils.item(pool, Items.COPPER_INGOT, 8, 1, 2);
        LootUtils.item(pool, Items.COPPER_BARS.oxidized(), 6, 1, 2);
        LootUtils.item(pool, Items.BONE, 5, 1, 3);
        LootUtils.item(pool, Items.STRING, 4, 1, 2);
        LootUtils.item(pool, Items.ROTTEN_FLESH, 4, 1, 2);

        return pool;
    }

    private LootPool.Builder suppliesTowerBig() {
        var pool = LootPool.lootPool().setRolls(UniformGenerator.between(2, 3));

        LootUtils.item(pool, Items.BREAD, 6, 1, 2);
        LootUtils.item(pool, Items.WHEAT, 6, 1, 3);
        LootUtils.item(pool, Items.WHEAT_SEEDS, 5, 1, 4);
        LootUtils.item(pool, Items.CARROT, 4, 1, 2);
        LootUtils.item(pool, Items.POTATO, 4, 1, 2);

        return pool;
    }

    private LootPool.Builder treasureTowerBig() {
        var pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1));

        LootUtils.empty(pool, 55);
        LootUtils.item(pool, Items.RAW_GOLD_BLOCK, 10, 1, 1);
        LootUtils.item(pool, Items.DIAMOND, 8, 1, 1);
        LootUtils.item(pool, Items.EMERALD, 8, 1, 2);
        LootUtils.item(pool, Items.AMETHYST_SHARD, 8, 1, 2);
        LootUtils.item(pool, Items.SPYGLASS, 6, 1, 1);
        LootUtils.item(pool, SilentItems.RESONANT_CRYSTAL, 5, 1, 2);

        return pool;
    }

    private LootPool.Builder fragments() {
        var pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1));

        addFragmentCorners(pool, SilentItems.BLUE_FRAGMENTS_AND_TABLES);
        addFragmentCorners(pool, SilentItems.GREEN_FRAGMENTS_AND_TABLES);
        addFragmentCorners(pool, SilentItems.RED_FRAGMENTS_AND_TABLES);
        addFragmentCorners(pool, SilentItems.YELLOW_FRAGMENTS_AND_TABLES);
        addFragmentCorners(pool, SilentItems.PURPLE_FRAGMENTS_AND_TABLES);

        return pool;
    }

    private void addFragmentCorners(LootPool.Builder pool, List<DeferredItem<? extends Item>> fragmentsAndTables) {
        fragmentsAndTables.subList(0, 4).forEach(item -> LootUtils.item(pool, item));
    }

    private LootPool.Builder materialsRuins() {
        var pool = LootPool.lootPool().setRolls(UniformGenerator.between(4, 6));

        LootUtils.item(pool, Items.AMETHYST_SHARD, 10, 1, 2);
        LootUtils.item(pool, Items.COPPER_INGOT, 9, 1, 3);
        LootUtils.item(pool, Items.IRON_INGOT, 8, 1, 3);
        LootUtils.item(pool, SilentItems.RESONANT_CRYSTAL, 10, 1, 3);
        LootUtils.item(pool, Items.GOLD_INGOT, 7, 1, 2);
        LootUtils.item(pool, Items.LAPIS_LAZULI, 6, 1, 3);
        LootUtils.item(pool, Items.COPPER_NUGGET, 8, 2, 5);
        LootUtils.item(pool, Items.IRON_NUGGET, 6, 2, 4);

        return pool;
    }

    private LootPool.Builder rareRuins() {
        var pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1));

        LootUtils.empty(pool, 72);
        LootUtils.item(pool, Items.EMERALD, 8, 1, 2);
        LootUtils.item(pool, Items.DIAMOND, 10, 1, 2);
        LootUtils.item(pool, SilentItems.RESONANT_CRYSTAL, 10, 1, 3);

        return pool;
    }
}
