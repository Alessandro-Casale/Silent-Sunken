package com.alessandro.silentsunken.infrastructure.registry;

import com.alessandro.silentsunken.SilentSunken;
import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SilentVillagerProfessions {
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS = DeferredRegister.create(Registries.VILLAGER_PROFESSION, SilentSunken.MODID);

    public static final DeferredHolder<VillagerProfession, VillagerProfession> HISTORIAN = VILLAGER_PROFESSIONS.register("historian",
        () -> new VillagerProfession(
            Component.translatable("entity." + SilentSunken.MODID + ".villager.historian"),
            PoiType.NONE,
            PoiType.NONE,
            ImmutableSet.of(),
            ImmutableSet.of(),
            SoundEvents.VILLAGER_WORK_LIBRARIAN,
            Int2ObjectMaps.emptyMap()
        )
    );
}
