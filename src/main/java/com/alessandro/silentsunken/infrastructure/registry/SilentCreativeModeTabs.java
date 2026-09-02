package com.alessandro.silentsunken.infrastructure.registry;

import com.alessandro.silentsunken.SilentSunken;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SilentCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, SilentSunken.MODID);

    public static final Supplier<CreativeModeTab> SILENT_CREATIVE_TAB = CREATIVE_MODE_TABS.register("creative_tab", () -> CreativeModeTab.builder()
        .title(Component.translatable("itemGroup." + SilentSunken.MODID))
        .icon(() -> new ItemStack(SilentItems.RESONANT_CRYSTAL_ORE.get()))
        .displayItems((_, output) -> {
            output.accept(SilentItems.RESONANT_CRYSTAL_ORE);
            output.accept(SilentItems.RESONANT_CRYSTAL);
            output.accept(SilentItems.RESONANT_BARREL);
            output.accept(SilentItems.MOSSY_RESONANT_BARREL);
            output.accept(SilentItems.RESONANT_HAMMER);

            SilentItems.BLUE_FRAGMENTS_AND_TABLES.forEach(output::accept);
            SilentItems.GREEN_FRAGMENTS_AND_TABLES.forEach(output::accept);
            SilentItems.RED_FRAGMENTS_AND_TABLES.forEach(output::accept);
            SilentItems.YELLOW_FRAGMENTS_AND_TABLES.forEach(output::accept);
            SilentItems.PURPLE_FRAGMENTS_AND_TABLES.forEach(output::accept);
        })
        .build()
    );
}
