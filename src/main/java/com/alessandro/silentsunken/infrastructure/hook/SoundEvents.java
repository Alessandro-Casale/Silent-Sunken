package com.alessandro.silentsunken.infrastructure.hook;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.api.nullability.NotNullParams;
import com.alessandro.silentsunken.engine.SilentManager;
import com.alessandro.silentsunken.infrastructure.resource.SoundHintDefinitions;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.PlayLevelSoundEvent;

@NotNullParams
@EventBusSubscriber(modid = SilentSunken.MODID)
public class SoundEvents {
    @SubscribeEvent
    public static void soundAtPosition(PlayLevelSoundEvent.AtPosition event) {
        var sound = event.getSound();
        if (sound == null) { return; }

        if (event.getLevel() instanceof ServerLevel serverLevel) {
            tryUnlockAtPosition(serverLevel, event.getPosition(), sound);
        }
    }

    @SubscribeEvent
    public static void soundAtEntity(PlayLevelSoundEvent.AtEntity event) {
        var sound = event.getSound();
        if (sound == null) { return; }

        if (event.getLevel() instanceof ServerLevel serverLevel) {
            var entity = event.getEntity();
            tryUnlockAtEntity(serverLevel, entity.position(), entity, sound);
        }
    }

    public static void tryUnlockAtPosition(ServerLevel level, Vec3 position, Holder<SoundEvent> holder) {
        var sound = holder.value();
        if (!SoundHintDefinitions.INSTANCE.isListeningForSoundEvent(sound)) { return; }

        SilentManager.SOUND_LISTENER_INSTANCE.forEachInRange(level, position, (pos, soundSensible) -> {
            if (soundSensible.checkForRequiredSound(sound)) {
                soundSensible.interceptSoundAtPos(level, pos, sound);
            }
        });
    }

    public static void tryUnlockAtEntity(ServerLevel level, Vec3 position, Entity entity, Holder<SoundEvent> holder) {
        var sound = holder.value();
        if (!SoundHintDefinitions.INSTANCE.isListeningForSoundEvent(sound)) { return; }

        SilentManager.SOUND_LISTENER_INSTANCE.forEachInRange(level, position, (pos, soundSensible) -> {
            if (soundSensible.checkForRequiredSound(sound)) {
                soundSensible.interceptSoundAtEntity(level, pos, entity, sound);
            }
        });
    }
}
