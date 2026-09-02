package com.alessandro.silentsunken.infrastructure.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

public record Mossable(Block mossyVariant) {
    public static final Codec<Mossable> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        BuiltInRegistries.BLOCK.byNameCodec().fieldOf("mossy_variant").forGetter(Mossable::mossyVariant)
    ).apply(instance, Mossable::new));
}
