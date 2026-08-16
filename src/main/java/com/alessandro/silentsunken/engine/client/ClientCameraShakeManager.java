package com.alessandro.silentsunken.engine.client;

import com.alessandro.silentsunken.api.TimeUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class ClientCameraShakeManager {
    private final Random RANDOM = new Random();

    private double magnitude = 0;
    private long triggeredAt = 0;
    private double duration = 0;

    public void triggerForPlayer(Vec3 pos) {
        var player = Minecraft.getInstance().player;
        var distance = player != null ? player.position().distanceTo(pos) : 0.0;
        var falloff = (float) Mth.clamp(1.0 - distance / 10.0, 0.0, 1.0);

        if (falloff > 0.02f) {
            trigger(0.9 * falloff, 0.35);
        }
    }

    public void trigger(double magnitude, double duration) {
        this.magnitude = magnitude;
        this.duration = duration;
        triggeredAt = TimeUtils.getTimeNanos();
    }

    public Sample buildSample() {
        if (magnitude <= 0.0f) {
            return null;
        }

        var elapsedSeconds = TimeUtils.nanosToSeconds(TimeUtils.getTimeNanos() - triggeredAt);
        if (elapsedSeconds >= duration) {
            magnitude = 0.0f;
            return null;
        }

        var decay = 1.0f - elapsedSeconds / duration;
        var currentStrength = magnitude * decay * decay;

        var wobble = elapsedSeconds * 90.0f;
        var jitter = RANDOM.nextFloat() * 0.15f;
        var yaw = Math.sin(wobble) * currentStrength;
        var pitch = Math.cos(wobble * 1.3f) * currentStrength * 0.6f;
        var roll = Math.sin(wobble * 0.7f + jitter) * currentStrength * 0.4f;

        return new Sample(yaw, pitch, roll);
    }

    public record Sample(double yaw, double pitch, double roll) { }
}
