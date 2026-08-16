package com.alessandro.silentsunken.infrastructure.registry;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.infrastructure.item.ResonantHammerItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SilentItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SilentSunken.MODID);

    public static final DeferredItem<BlockItem> RESONANT_STONE = ITEMS.registerSimpleBlockItem(SilentBlocks.RESONANT_STONE);
    public static final DeferredItem<Item> RESONANT_HAMMER = ITEMS.registerItem("resonant_hammer", ResonantHammerItem::new, properties ->
        properties
            .durability(120)
            .rarity(Rarity.UNCOMMON)
            .useCooldown(5)
            // .repairable() // TODO: Choose repair material
    );
}
