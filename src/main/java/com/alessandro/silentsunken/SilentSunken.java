package com.alessandro.silentsunken;

import com.alessandro.silentsunken.infrastructure.registry.SilentBlockEntities;
import com.alessandro.silentsunken.infrastructure.registry.SilentBlocks;
import com.alessandro.silentsunken.infrastructure.registry.SilentCreativeModeTabs;
import com.alessandro.silentsunken.infrastructure.registry.SilentItems;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(SilentSunken.MODID)
public class SilentSunken {
    public static final String MODID = "silentsunken";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SilentSunken(IEventBus modEventBus, ModContainer ignoredModContainer) {
        SilentItems.ITEMS.register(modEventBus);
        SilentBlocks.BLOCKS.register(modEventBus);
        SilentBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        SilentCreativeModeTabs.CREATIVE_MODE_TABS.register(modEventBus);
    }
}
