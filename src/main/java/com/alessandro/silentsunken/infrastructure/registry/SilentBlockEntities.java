package com.alessandro.silentsunken.infrastructure.registry;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.infrastructure.blockentity.MossyResonantBarrelBlockEntity;
import com.alessandro.silentsunken.infrastructure.blockentity.ResonantBarrelBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SilentBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, SilentSunken.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ResonantBarrelBlockEntity>> RESONANT_BARREL = BLOCK_ENTITIES.register(
        "resonant_barrel",
        () -> new BlockEntityType<>(ResonantBarrelBlockEntity::new, SilentBlocks.RESONANT_BARREL.get())
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MossyResonantBarrelBlockEntity>> MOSSY_RESONANT_CRATE = BLOCK_ENTITIES.register(
        "mossy_resonant_barrel",
        () -> new BlockEntityType<>(MossyResonantBarrelBlockEntity::new, SilentBlocks.MOSSY_RESONANT_BARREL.get())
    );
}
