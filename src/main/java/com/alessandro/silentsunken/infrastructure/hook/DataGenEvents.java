package com.alessandro.silentsunken.infrastructure.hook;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.api.nullability.NotNullParams;
import com.alessandro.silentsunken.infrastructure.datagen.SBlockTagsProvider;
import com.alessandro.silentsunken.infrastructure.datagen.SLanguageProvider;
import com.alessandro.silentsunken.infrastructure.datagen.SLootSubProvider;
import com.alessandro.silentsunken.infrastructure.datagen.SModelProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;

@NotNullParams
@EventBusSubscriber(modid = SilentSunken.MODID)
public class DataGenEvents {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event) {
        event.createProvider(SBlockTagsProvider::new);
        event.createProvider(SLanguageProvider::new);
        event.createProvider(SModelProvider::new);

        event.createProvider((output, lookupProvider) -> new LootTableProvider(
            output,
            Set.of(),
            List.of(new LootTableProvider.SubProviderEntry(
                SLootSubProvider::new,
                LootContextParamSets.BLOCK
            )),
            lookupProvider
        ));
    }
}
