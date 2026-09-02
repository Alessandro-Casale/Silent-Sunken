package com.alessandro.silentsunken.infrastructure.resource;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.api.nullability.NotNullParams;
import com.alessandro.silentsunken.infrastructure.codec.SoundHintDefinition;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@NotNullParams
public class SoundHintDefinitions extends SimpleJsonResourceReloadListener<SoundHintDefinition> {
    public static final String FOLDER = "sound_hint";
    public static final Identifier ID = Identifier.fromNamespaceAndPath(SilentSunken.MODID, FOLDER + "_listener");
    public static final SoundHintDefinitions INSTANCE = new SoundHintDefinitions();

    private BiMap<Identifier, SoundHintDefinition> definitions;
    private Set<SoundEvent> soundEventsToListen;

    protected SoundHintDefinitions() {
        super(SoundHintDefinition.CODEC, FileToIdConverter.json(FOLDER));
    }

    @Override
    protected void apply(Map<Identifier, SoundHintDefinition> parsed, ResourceManager manager, ProfilerFiller filler) {
        definitions = HashBiMap.create(parsed);

        soundEventsToListen = new HashSet<>();
        definitions.forEach((_, definition) -> {
            soundEventsToListen.addAll(definition.unlockSounds());
        });

        SilentSunken.LOGGER.info("Loaded {} sound hint definitions", this.definitions.size());
    }

    public Set<Identifier> ids() {
        return definitions.keySet();
    }

    public SoundHintDefinition getDefinition(Identifier id) {
        return definitions.get(id);
    }

    public Identifier getId(SoundHintDefinition definition) {
        return definitions.inverse().get(definition);
    }

    public boolean contains(Identifier id) {
        return definitions.containsKey(id);
    }

    public boolean isListeningForSoundEvent(SoundEvent sound) {
        return soundEventsToListen.contains(sound);
    }
}
