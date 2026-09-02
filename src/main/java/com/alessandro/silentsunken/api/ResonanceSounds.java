package com.alessandro.silentsunken.api;

import com.alessandro.silentsunken.api.nullability.NotNullParams;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

@NotNullParams
public class ResonanceSounds {
    public static void playImpactSound(ServerLevel level, BlockPos pos, boolean boosted) {
        level.playSound(null, pos, SoundEvents.STONE_HIT, SoundSource.BLOCKS, 1.2f, 0.65f);
        level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 1.0f, 0.8f);
        level.playSound(null, pos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 0.35f, 1.9f);

        if (boosted) {
            level.playSound(null, pos, SoundEvents.BEACON_POWER_SELECT, SoundSource.BLOCKS, 0.6f, 0.5f);
        }
    }
}
