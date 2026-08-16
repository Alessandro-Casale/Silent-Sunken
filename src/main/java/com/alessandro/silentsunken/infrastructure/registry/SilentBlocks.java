package com.alessandro.silentsunken.infrastructure.registry;

import com.alessandro.silentsunken.SilentSunken;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SilentBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SilentSunken.MODID);

    public static final DeferredBlock<Block> RESONANT_STONE = BLOCKS.registerSimpleBlock("resonant_stone", properties ->
        properties
            .destroyTime(1.75f)
            .explosionResistance(6)
            .sound(SoundType.STONE)
            .requiresCorrectToolForDrops()
    );
}
