package com.alessandro.silentsunken.infrastructure.registry;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.infrastructure.block.MossyResonantBarrelBlock;
import com.alessandro.silentsunken.infrastructure.block.ResonantBarrelBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SilentBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SilentSunken.MODID);

    public static final DeferredBlock<Block> RESONANT_CRYSTAL_ORE = BLOCKS.registerSimpleBlock("resonant_crystal_ore", properties ->
        properties
            .destroyTime(3.0f)
            .explosionResistance(6)
            .sound(SoundType.AMETHYST_CLUSTER)
            .requiresCorrectToolForDrops()
    );

    public static final DeferredBlock<ResonantBarrelBlock> RESONANT_BARREL = BLOCKS.registerBlock("resonant_barrel", ResonantBarrelBlock::new, properties ->
        properties
            .strength(22.5f, 600.0f)
            .sound(SoundType.STONE)
            .noOcclusion()
    );

    public static final DeferredBlock<MossyResonantBarrelBlock> MOSSY_RESONANT_BARREL = BLOCKS.registerBlock("mossy_resonant_barrel", MossyResonantBarrelBlock::new, properties ->
        properties
            .strength(22.5f, 600.0f)
            .sound(SoundType.MOSS)
            .noOcclusion()
    );
}
