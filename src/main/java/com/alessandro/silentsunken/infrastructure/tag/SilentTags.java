package com.alessandro.silentsunken.infrastructure.tag;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.api.nullability.NotNullMethodsReturn;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

@NotNullMethodsReturn
public class SilentTags {
    public static TagKey<Block> CLICKABLE_WITH_RESONANT_HAMMER = block("clickable_with_resonant_hammer");
    public static TagKey<Block> DISCOVERABLE_WITH_SCAN_SESSION = block("discorable_with_scan_session");
    public static TagKey<Block> ANCIENT_BLOCKS = block("ancient_blocks");

    public static TagKey<Biome> HAS_STRUCTURE_RUINS = biome("has_structure/ruins");

    public static TagKey<Item> item(String identifier) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(SilentSunken.MODID, identifier));
    }

    public static TagKey<Block> block(String identifier) {
        return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(SilentSunken.MODID, identifier));
    }

    public static TagKey<Biome> biome(String identifier) {
        return TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(SilentSunken.MODID, identifier));
    }
}
