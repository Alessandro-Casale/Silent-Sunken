package com.alessandro.silentsunken.infrastructure.fx;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.api.GraphicUtils;
import com.alessandro.silentsunken.api.TimeUtils;
import com.alessandro.silentsunken.api.nullability.NotNullParams;
import com.alessandro.silentsunken.api.session.ScanSession;
import com.alessandro.silentsunken.engine.SilentClientManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.SubmitCustomGeometryEvent;
import org.joml.Vector3f;

import java.util.concurrent.atomic.AtomicInteger;

@NotNullParams
@EventBusSubscriber(modid = SilentSunken.MODID, value = Dist.CLIENT)
public class ScanRenderer {
    private static final float SPHERE_LINE_WIDTH = 10;
    private static final int SPHERE_SEGMENTS = 48;

    private static final int EDGE_COUNT = 12;
    private static final float OUTLINE_LINE_WIDTH = 2;
    private static final float OUTLINE_GLOW_LINE_WIDTH = 4;
    private static final double OUTLINE_POP_IN_SECONDS = 0.2;

    @SubscribeEvent
    public static void onSubmitGeometry(SubmitCustomGeometryEvent event) {
        if (!SilentClientManager.OUTLINE_INSTANCE.hasActiveSessions()) { return; }

        var collector = event.getSubmitNodeCollector();
        var poseStack = event.getPoseStack();
        var cameraPos = event.getLevelRenderState().cameraRenderState.pos;
        var currentNanos = TimeUtils.getTimeNanos();

        var sessions = SilentClientManager.OUTLINE_INSTANCE.getSessions();
        for (var session : sessions) {
            if (session.isSearchActive(TimeUtils.getTimeNanos())) {
                renderSphere(collector, poseStack, cameraPos, currentNanos, session);
            }

            for (var target : session.targets()) {
                if (!target.isRevealed() && !target.isExpired(currentNanos)) {
                    renderBlockOutline(collector, poseStack, cameraPos, currentNanos, target);
                }
            }
        }

        SilentClientManager.OUTLINE_INSTANCE.removeExpiredSessions(currentNanos);
    }

    private static void renderSphere(SubmitNodeCollector collector, PoseStack poseStack, Vec3 cameraPos, long currentTime, ScanSession session) {
        var elapsedTime = currentTime - session.getStartTime();
        var totalTime = Math.max(session.getEndTime() - session.getStartTime(), 1);
        var progress = (currentTime - session.getStartTime()) / (double) totalTime;

        var radius = session.getRadius() * progress;
        if (radius <= 0.1) { return; }

        var pulse = 0.55 + 0.25 * Math.sin(TimeUtils.nanosToSeconds(elapsedTime) * 6);
        var alpha = 0.55 * (1 - progress * 0.4) * pulse;
        var color = ARGB.color((int) (alpha * 255), 0x33, 0xE0, 0xFF);

        var center = GraphicUtils.adjustToCameraPos(session.getOrigin(), cameraPos);

        collector.submitCustomGeometry(poseStack, SilentRenderTypes.RESONANCE_OUTLINE, (pose, buffer) -> {
            drawCircle(pose, buffer, center, radius, new Vec3(1, 0, 0), new Vec3(0, 1, 0), color);
            drawCircle(pose, buffer, center, radius, new Vec3(0, 0, 1), new Vec3(0, 1, 0), color);
            drawCircle(pose, buffer, center, radius, new Vec3(1, 0, 1), new Vec3(0, 1, 0), color);
            drawCircle(pose, buffer, center, radius, new Vec3(-1, 0, 1), new Vec3(0, 1, 0), color);

            drawHorizontalCircle(pose, buffer, center, radius, radius * 0.5, color);
            drawHorizontalCircle(pose, buffer, center, radius, 0, color);
            drawHorizontalCircle(pose, buffer, center, radius, -radius * 0.5, color);
        });
    }

    private static void drawHorizontalCircle(PoseStack.Pose pose, VertexConsumer buffer, Vec3 center, double radius, double heightOffset, int color) {
        if (Math.abs(heightOffset) >= radius) { return; }

        var ringRadius = Math.sqrt(radius * radius - heightOffset * heightOffset);
        var ringCenter = center.add(0, heightOffset, 0);

        drawCircle(pose, buffer, ringCenter, ringRadius, new Vec3(1, 0, 0), new Vec3(0, 0, 1), color);
    }

    private static void drawCircle(PoseStack.Pose pose, VertexConsumer buffer, Vec3 center, double radius, Vec3 unitVectorU, Vec3 unitVectorV, int color) {
        var direction = new Vector3f(0, 1, 0);
        Vec3 previousPoint = null;

        for (var i = 0; i <= SPHERE_SEGMENTS; i++) {
            var theta = (Math.PI * 2 * i) / SPHERE_SEGMENTS;

            var point = center
                .add(unitVectorU.normalize().scale(Math.cos(theta) * radius))
                .add(unitVectorV.normalize().scale(Math.sin(theta) * radius));

            if (previousPoint != null) {
                buffer.addVertex(pose.pose(), (float) previousPoint.x, (float) previousPoint.y, (float) previousPoint.z)
                    .setColor(color).setNormal(pose, direction).setLineWidth(SPHERE_LINE_WIDTH);

                buffer.addVertex(pose.pose(), (float) point.x, (float) point.y, (float) point.z)
                    .setColor(color).setNormal(pose, direction).setLineWidth(SPHERE_LINE_WIDTH);
            }

            previousPoint = point;
        }
    }

    private static void renderBlockOutline(SubmitNodeCollector collector, PoseStack poseStack, Vec3 cameraPos, long currentTime, ScanSession.Target target) {
        var searchDuration = TimeUtils.nanosToSeconds(target.getEndTime() - target.getRevealTime());
        var age = TimeUtils.nanosToSeconds(currentTime - target.getRevealTime());

        var fadeIn = Mth.clamp(age / OUTLINE_POP_IN_SECONDS, 0, 1);
        var fadeStart = searchDuration * (1 - OUTLINE_POP_IN_SECONDS);
        var fadeOut = age > fadeStart ? Mth.clamp(1.0 - (age - fadeStart) / (searchDuration - fadeStart), 0, 1) : 1;
        var alphaMultiplier = fadeIn * fadeOut;
        if (alphaMultiplier <= 0.01) { return; }

        var relativePos = GraphicUtils.adjustToCameraPos(target.getVectorPos(), cameraPos);
        var chase = (float) ((age * (1 + OUTLINE_POP_IN_SECONDS)) % 1.0);

        collector.submitCustomGeometry(poseStack, SilentRenderTypes.RESONANCE_OUTLINE, (pose, buffer) -> {
            AtomicInteger edgeIndex = new AtomicInteger();
            Shapes.block().forAllEdges((x1, y1, z1, x2, y2, z2) -> {
                drawLine(pose, buffer, relativePos, edgeIndex.get(), chase, alphaMultiplier, x1, y1, z1, x2, y2, z2);
                edgeIndex.getAndIncrement();
            });
        });
    }

    private static void drawLine(PoseStack.Pose pose, VertexConsumer buffer, Vec3 relativePos, int edgeIndex, float chase, double alphaMultiplier, double x1, double y1, double z1, double x2, double y2, double z2) {
        var edgeParam = (edgeIndex % EDGE_COUNT) / (float) EDGE_COUNT;

        var diff = Math.abs(edgeParam - chase);
        diff = Math.min(diff, 1 - diff);

        var glow = (float) Math.pow(Math.max(0, 1.0 - diff * 3.5), 2);
        var hue = 0.5f + diff * 0.35f;
        var brightness = 0.55f + glow * 0.45f;
        var alpha = (0.35 + glow * 0.65) * alphaMultiplier;
        var color = Mth.hsvToArgb(hue, 0.65f, brightness, (int) (alpha * 255));

        var direction = new Vector3f((float) (x2 - x1), (float) (y2 - y1), (float) (z2 - z1)).normalize();
        var width = OUTLINE_LINE_WIDTH + glow * (OUTLINE_GLOW_LINE_WIDTH - OUTLINE_LINE_WIDTH);

        var relativeX1 = (float) (x1 + relativePos.x);
        var relativeY1 = (float) (y1 + relativePos.y);
        var relativeZ1 = (float) (z1 + relativePos.z);
        var relativeX2 = (float) (x2 + relativePos.x);
        var relativeY2 = (float) (y2 + relativePos.y);
        var relativeZ2 = (float) (z2 + relativePos.z);

        buffer.addVertex(pose.pose(), relativeX1, relativeY1, relativeZ1)
            .setColor(color).setNormal(pose, direction).setLineWidth(width);

        buffer.addVertex(pose.pose(), relativeX2, relativeY2, relativeZ2)
            .setColor(color).setNormal(pose, direction).setLineWidth(width);
    }
}
