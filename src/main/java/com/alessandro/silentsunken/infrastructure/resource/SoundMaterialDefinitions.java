package com.alessandro.silentsunken.infrastructure.resource;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.api.nullability.NotNullParams;
import com.alessandro.silentsunken.infrastructure.codec.SoundMaterial;
import com.alessandro.silentsunken.infrastructure.codec.SoundMaterialDefinition;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;
import java.util.Set;

@NotNullParams
public class SoundMaterialDefinitions extends SimpleJsonResourceReloadListener<SoundMaterialDefinition> {
    public static final String FOLDER = "sound_material";
    public static final Identifier ID = Identifier.fromNamespaceAndPath(SilentSunken.MODID, FOLDER + "_listener");
    public static final SoundMaterialDefinitions INSTANCE = new SoundMaterialDefinitions();

    private BiMap<Identifier, SoundMaterialDefinition> definitions;

    protected SoundMaterialDefinitions() {
        super(SoundMaterialDefinition.CODEC, FileToIdConverter.json(FOLDER));
    }

    @Override
    protected void apply(Map<Identifier, SoundMaterialDefinition> parsed, ResourceManager manager, ProfilerFiller filler) {
        definitions = HashBiMap.create(parsed);
        SilentSunken.LOGGER.info("Loaded {} sound material definitions", this.definitions.size());
    }

    public Set<Identifier> ids() {
        return definitions.keySet();
    }

    public SoundMaterialDefinition getDefinition(SoundMaterial material) {
        return definitions.values().stream().filter(def -> def.material().equals(material)).findFirst().orElse(null);
    }

    public SoundMaterialDefinition getDefinition(Identifier id) {
        return definitions.get(id);
    }
}
