package com.alessandro.silentsunken.api.resonance;

import com.alessandro.silentsunken.api.nullability.Nullable;
import com.alessandro.silentsunken.infrastructure.codec.SoundHintDefinition;
import com.alessandro.silentsunken.infrastructure.resource.SoundHintDefinitions;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;

import java.util.List;

public interface SoundSensible {
    void interceptSound(ServerLevel level, BlockPos pos, SoundEvent sound);
    int getCurrentPhase();
    @Nullable List<Identifier> getPhaseIds();
    int interceptSoundRadius();

    default boolean isWaitingForAnotherSound() {
        if (getPhaseIds() == null) {
            return false;
        }

        return getCurrentPhase() < getNumberOfPhases();
    }

    default int getNumberOfPhases() {
        return getPhaseIds().size();
    }

    default SoundHintDefinition getCurrentDefinition() {
        if (getPhaseIds() == null) { return null; }

        var id = getPhaseIds().get(getCurrentPhase());
        return SoundHintDefinitions.INSTANCE.getDefinition(id);
    }

    default void interceptSoundAtPos(ServerLevel level, BlockPos pos, SoundEvent sound) {
        interceptSound(level, pos, sound);
    }

    default void interceptSoundAtEntity(ServerLevel level, BlockPos pos, Entity entity, SoundEvent sound) {
        interceptSound(level, pos, sound);
    }

    default boolean checkForRequiredSound(SoundEvent sound) {
        var definition = getCurrentDefinition();
        if (definition == null) { return false; }

        return definition.hasSound(sound);
    }
}
