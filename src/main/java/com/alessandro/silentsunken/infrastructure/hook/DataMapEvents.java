package com.alessandro.silentsunken.infrastructure.hook;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.api.nullability.NotNullParams;
import com.alessandro.silentsunken.infrastructure.registry.SilentDataMaps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapsUpdatedEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@NotNullParams
@EventBusSubscriber(modid = SilentSunken.MODID)
public class DataMapEvents {
    @SubscribeEvent
    public static void registerDataMaps(RegisterDataMapTypesEvent event) {
        event.register(SilentDataMaps.MOSSABLES);
    }

    @SubscribeEvent
    public static void onDataMapsUpdate(DataMapsUpdatedEvent event) {
        event.ifRegistry(Registries.BLOCK, registry -> {
            SilentDataMaps.INVERSE_MOSS_VARIANT.clear();

            registry.getDataMap(SilentDataMaps.MOSSABLES).forEach((resourceKey, result) -> {
                var block = BuiltInRegistries.BLOCK.getValue(resourceKey);
                SilentDataMaps.INVERSE_MOSS_VARIANT.put(result.mossyVariant(), block);
            });
        });
    }
}
