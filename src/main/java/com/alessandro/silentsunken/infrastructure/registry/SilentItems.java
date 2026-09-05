package com.alessandro.silentsunken.infrastructure.registry;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.silentsunken.infrastructure.codec.SoundMaterial;
import com.alessandro.silentsunken.infrastructure.item.FragmentItem;
import com.alessandro.silentsunken.infrastructure.item.RawTabletItem;
import com.alessandro.silentsunken.infrastructure.item.ResonantHammerItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.List;

@NotNullParamsAndMethodsReturn
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

    public static List<DeferredItem<? extends Item>> BLUE_FRAGMENTS_AND_TABLES = fragmentsAndTables(SoundMaterial.BLUE);
    public static List<DeferredItem<? extends Item>> GREEN_FRAGMENTS_AND_TABLES = fragmentsAndTables(SoundMaterial.GREEN);
    public static List<DeferredItem<? extends Item>> RED_FRAGMENTS_AND_TABLES = fragmentsAndTables(SoundMaterial.RED);
    public static List<DeferredItem<? extends Item>> YELLOW_FRAGMENTS_AND_TABLES = fragmentsAndTables(SoundMaterial.YELLOW);
    public static List<DeferredItem<? extends Item>> PURPLE_FRAGMENTS_AND_TABLES = fragmentsAndTables(SoundMaterial.PURPLE);

    public static @UnmodifiableView List<DeferredItem<? extends Item>> fragmentsAndTables(SoundMaterial soundMaterial) {
        var fragmentCorner1 = ITEMS.registerItem(soundMaterial.type() + "_fragment_corner_1", properties -> new FragmentItem(properties, soundMaterial, 1), Item.Properties::new);
        var fragmentCorner2 = ITEMS.registerItem(soundMaterial.type() + "_fragment_corner_2", properties -> new FragmentItem(properties, soundMaterial, 2), Item.Properties::new);
        var fragmentCorner3 = ITEMS.registerItem(soundMaterial.type() + "_fragment_corner_3", properties -> new FragmentItem(properties, soundMaterial, 3), Item.Properties::new);
        var fragmentCorner4 = ITEMS.registerItem(soundMaterial.type() + "_fragment_corner_4", properties -> new FragmentItem(properties, soundMaterial, 4), Item.Properties::new);

        var rawTablet = ITEMS.registerItem("raw_" + soundMaterial.type() + "_tablet", properties -> new RawTabletItem(properties, soundMaterial), Item.Properties::new);
        var gildedTablet = ITEMS.registerSimpleItem("gilded_" + soundMaterial.type() + "_tablet", Item.Properties::new);

        return List.of(fragmentCorner1, fragmentCorner2, fragmentCorner3, fragmentCorner4, rawTablet, gildedTablet);
    }
}
