package com.alessandro.silentsunken.infrastructure.hook;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.api.nullability.NotNullParams;
import com.alessandro.silentsunken.infrastructure.datagen.*;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;

@NotNullParams
@EventBusSubscriber(modid = SilentSunken.MODID)
public class DataGenEvents {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event) {
        event.createProvider(SBlockTagsProvider::new);
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

        event.createProvider((output, lookupProvider) -> new DatapackBuiltinEntriesProvider(
            output,
            lookupProvider,
            new RegistrySetBuilder()
                .add(Registries.CONFIGURED_FEATURE, SWorldgenProvider::configuredFeatures)
                .add(Registries.PROCESSOR_LIST, SWorldgenProvider::processorLists)
                .add(Registries.TEMPLATE_POOL, SWorldgenProvider::templatePools)
                .add(Registries.STRUCTURE, SWorldgenProvider::structures)
                .add(Registries.STRUCTURE_SET, SWorldgenProvider::structureSets),
            Set.of(SilentSunken.MODID)
        ));
    }
}
