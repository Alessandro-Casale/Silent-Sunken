package com.alessandro.silentsunken.infrastructure.hook;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.silentsunken.infrastructure.datagen.*;
import net.minecraft.core.Cloner;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@NotNullParamsAndMethodsReturn
@EventBusSubscriber(modid = SilentSunken.MODID)
public class DataGenEvents {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event) {
        event.createProvider(SBlockTagsProvider::new);
        event.createProvider(SItemTagsProvider::new);
        event.createProvider(SRecipeProvider.Runner::new);
        event.createProvider(SBiomeTagsProvider::new);
        event.createProvider(SLanguageProvider::new);
        event.createProvider(SModelProvider::new);
        event.createProvider(SDataMapsProvider::new);

        event.createProvider((output, lookupProvider) -> new LootTableProvider(
            output,
            Set.of(),
            List.of(
                new LootTableProvider.SubProviderEntry(SLootSubProvider::new, LootContextParamSets.BLOCK),
                new LootTableProvider.SubProviderEntry(_ -> new SChestLootSubProvider(), LootContextParamSets.CHEST)
            ),
            lookupProvider
        ));

        var worldgenBuilder = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, SWorldgenProvider::configuredFeatures)
            .add(Registries.PROCESSOR_LIST, SWorldgenProvider::processorLists)
            .add(Registries.TEMPLATE_POOL, SWorldgenProvider::templatePools)
            .add(Registries.STRUCTURE, SWorldgenProvider::structures)
            .add(Registries.STRUCTURE_SET, SWorldgenProvider::structureSets);

        event.createProvider((output, _) -> new AdvancementProvider(
            output,
            CompletableFuture.completedFuture(ownWorldgenRegistries(worldgenBuilder)),
            List.of(new SAdvancementProvider())
        ));

        event.createProvider((output, lookupProvider) -> new DatapackBuiltinEntriesProvider(
            output,
            lookupProvider,
            worldgenBuilder,
            Set.of(SilentSunken.MODID)
        ));
    }

    private static HolderLookup.Provider ownWorldgenRegistries(RegistrySetBuilder worldgenBuilder) {
        var clonerFactory = new Cloner.Factory()
            .addCodec(Registries.CONFIGURED_FEATURE, ConfiguredFeature.DIRECT_CODEC)
            .addCodec(Registries.PROCESSOR_LIST, StructureProcessorType.DIRECT_CODEC)
            .addCodec(Registries.TEMPLATE_POOL, StructureTemplatePool.DIRECT_CODEC)
            .addCodec(Registries.STRUCTURE, Structure.DIRECT_CODEC)
            .addCodec(Registries.STRUCTURE_SET, StructureSet.DIRECT_CODEC);

        return worldgenBuilder.buildPatch(
            RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY),
            HolderLookup.Provider.create(Stream.empty()),
            clonerFactory
        ).patches();
    }
}
