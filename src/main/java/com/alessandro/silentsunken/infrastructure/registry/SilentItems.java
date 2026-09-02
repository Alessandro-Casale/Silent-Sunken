package com.alessandro.silentsunken.infrastructure.registry;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.api.nullability.NotNullMethodsReturn;
import com.alessandro.silentsunken.infrastructure.item.ResonantHammerItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.List;

@NotNullMethodsReturn
public class SilentItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SilentSunken.MODID);

    public static final DeferredItem<BlockItem> RESONANT_CRYSTAL_ORE = ITEMS.registerSimpleBlockItem(SilentBlocks.RESONANT_CRYSTAL_ORE);
    public static final DeferredItem<Item> RESONANT_CRYSTAL = ITEMS.registerSimpleItem("resonant_crystal");
    public static final DeferredItem<BlockItem> RESONANT_BARREL = ITEMS.registerSimpleBlockItem(SilentBlocks.RESONANT_BARREL);
    public static final DeferredItem<BlockItem> MOSSY_RESONANT_BARREL = ITEMS.registerSimpleBlockItem(SilentBlocks.MOSSY_RESONANT_BARREL);
    public static final DeferredItem<Item> RESONANT_HAMMER = ITEMS.registerItem("resonant_hammer", ResonantHammerItem::new, properties ->
        properties
            .durability(120)
            .rarity(Rarity.UNCOMMON)
            .useCooldown(5)
            // .repairable() // TODO: Choose repair material
    );

    public static List<DeferredItem<Item>> BLUE_FRAGMENTS_AND_TABLES = fragmentsAndTables("blue");
    public static List<DeferredItem<Item>> GREEN_FRAGMENTS_AND_TABLES = fragmentsAndTables("green");
    public static List<DeferredItem<Item>> RED_FRAGMENTS_AND_TABLES = fragmentsAndTables("red");
    public static List<DeferredItem<Item>> YELLOW_FRAGMENTS_AND_TABLES = fragmentsAndTables("yellow");
    public static List<DeferredItem<Item>> PURPLE_FRAGMENTS_AND_TABLES = fragmentsAndTables("purple");

    public static @UnmodifiableView List<DeferredItem<Item>> fragmentsAndTables(String type) {
        var fragmentCorner1 = ITEMS.registerSimpleItem(type + "_fragment_corner_1", Item.Properties::new);
        var fragmentCorner2 = ITEMS.registerSimpleItem(type + "_fragment_corner_2", Item.Properties::new);
        var fragmentCorner3 = ITEMS.registerSimpleItem(type + "_fragment_corner_3", Item.Properties::new);
        var fragmentCorner4 = ITEMS.registerSimpleItem(type + "_fragment_corner_4", Item.Properties::new);

        var rawTablet = ITEMS.registerSimpleItem("raw_" + type + "_tablet", Item.Properties::new);
        var gildedTablet = ITEMS.registerSimpleItem("gilded_" + type + "_tablet", Item.Properties::new);

        return List.of(fragmentCorner1, fragmentCorner2, fragmentCorner3, fragmentCorner4, rawTablet, gildedTablet);
    }
}
