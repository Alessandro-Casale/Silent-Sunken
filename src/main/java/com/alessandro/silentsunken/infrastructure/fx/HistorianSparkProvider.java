package com.alessandro.silentsunken.infrastructure.fx;

import com.alessandro.silentsunken.api.nullability.NotNullParams;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

@NotNullParams
public class HistorianSparkProvider implements ParticleProvider<SimpleParticleType> {
    private static final int LIFETIME_TICKS = 8;
    private final SpriteSet sprites;

    public HistorianSparkProvider(SpriteSet sprites) {
        this.sprites = sprites;
    }

    @Override
    public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
        var particle = new HistorianSparkParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprites.get(random));
        particle.setColor(1.0F, 1.0F, 1.0F);
        particle.setLifetime(LIFETIME_TICKS);
        return particle;
    }
}
