package com.alessandro.silentsunken.infrastructure.codec;

import com.alessandro.silentsunken.api.codec.Validators;
import com.alessandro.silentsunken.api.fx.Position;
import com.alessandro.silentsunken.api.nullability.NotNullParams;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@NotNullParams
public record SoundHintDefinition(List<SoundEvent> unlockSounds, Optional<String> description, String instructions, Map<Position, Item> hintItems) {
    public static final Codec<SoundHintDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        BuiltInRegistries.SOUND_EVENT.byNameCodec().listOf().fieldOf("unlock_sounds").validate(Validators::nonEmptyList).forGetter(SoundHintDefinition::unlockSounds),
        Codec.STRING.optionalFieldOf("description").forGetter(SoundHintDefinition::description),
        Codec.STRING.fieldOf("instructions").validate(Validators::nonBlankString).forGetter(SoundHintDefinition::instructions),
        Codec.unboundedMap(StringRepresentable.fromEnum(Position::values), BuiltInRegistries.ITEM.byNameCodec()).fieldOf("hint_items").forGetter(SoundHintDefinition::hintItems)
    ).apply(instance, SoundHintDefinition::new));

    public boolean hasSound(SoundEvent sound) {
        return unlockSounds.contains(sound);
    }
}
