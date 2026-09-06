package com.alessandro.silentsunken.infrastructure.registry;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.infrastructure.inventory.ResonantCraftingMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SilentMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, SilentSunken.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<ResonantCraftingMenu>> RESONANT_CRAFTING_TABLE = MENU_TYPES.register("resonant_crafting_table", () -> new MenuType<>(ResonantCraftingMenu::new, FeatureFlags.VANILLA_SET));
}
