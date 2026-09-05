package com.alessandro.silentsunken.infrastructure.fx;

import com.alessandro.silentsunken.api.nullability.NotNullParamsAndMethodsReturn;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SuspendedTownParticle;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

@NotNullParamsAndMethodsReturn
public class HistorianSparkParticle extends SuspendedTownParticle {
    private static final Particle.LifetimeAlpha FADE_OUT = new Particle.LifetimeAlpha(1.0F, 0.0F, 0.15F, 1.0F);

    private int initialLifetime;

    public HistorianSparkParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, TextureAtlasSprite sprite) {
        super(level, x, y, z, xa, ya, za, sprite);
        this.initialLifetime = this.lifetime;
    }

    @Override
    public void setLifetime(int lifetime) {
        super.setLifetime(lifetime);
        this.initialLifetime = lifetime;
    }

    @Override
    public SingleQuadParticle.Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    @Override
    public void tick() {
        this.age++;
        super.tick();
    }

    @Override
    public void extract(QuadParticleRenderState particleTypeRenderState, Camera camera, float partialTickTime) {
        this.setAlpha(FADE_OUT.currentAlphaForAge(this.age, this.initialLifetime, partialTickTime));
        super.extract(particleTypeRenderState, camera, partialTickTime);
    }
}
