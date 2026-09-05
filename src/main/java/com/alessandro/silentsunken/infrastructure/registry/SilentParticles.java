package com.alessandro.silentsunken.infrastructure.registry;

import com.alessandro.silentsunken.SilentSunken;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SilentParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, SilentSunken.MODID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> HISTORIAN_SPARK = PARTICLE_TYPES.register("historian_spark", () -> new SimpleParticleType(false));
}
