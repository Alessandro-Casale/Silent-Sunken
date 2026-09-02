package com.alessandro.silentsunken.infrastructure.registry;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.infrastructure.datamap.Mossable;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

import java.util.HashMap;
import java.util.Map;

public class SilentDataMaps {
    public static final Map<Block, Block> INVERSE_MOSS_VARIANT = new HashMap<>();

    public static final DataMapType<Block, Mossable> MOSSABLES = DataMapType.builder(
        Identifier.fromNamespaceAndPath(SilentSunken.MODID, "mossables"),
        Registries.BLOCK,
        Mossable.CODEC
    ).build();
}
