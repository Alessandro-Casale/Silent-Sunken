package com.alessandro.silentsunken.infrastructure.registry;

import com.alessandro.silentsunken.SilentSunken;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.criterion.PlayerTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SilentCriteriaTriggers {
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGER_TYPES = DeferredRegister.create(Registries.TRIGGER_TYPE, SilentSunken.MODID);

    public static final DeferredHolder<CriterionTrigger<?>, PlayerTrigger> RESONANT_ORE_SCAN = TRIGGER_TYPES.register("resonant_ore_scan", PlayerTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, PlayerTrigger> HAMMER_SCAN = TRIGGER_TYPES.register("hammer_scan", PlayerTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, PlayerTrigger> BARREL_OPENED = TRIGGER_TYPES.register("barrel_opened", PlayerTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, PlayerTrigger> BARREL_UNMOSSED = TRIGGER_TYPES.register("barrel_unmossed", PlayerTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, PlayerTrigger> MOSS_APPLIED = TRIGGER_TYPES.register("moss_applied", PlayerTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, PlayerTrigger> MOSS_REMOVED = TRIGGER_TYPES.register("moss_removed", PlayerTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, PlayerTrigger> HISTORIAN_CONVERTED = TRIGGER_TYPES.register("historian_converted", PlayerTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, PlayerTrigger> TABLET_GILDED = TRIGGER_TYPES.register("tablet_gilded", PlayerTrigger::new);
}
