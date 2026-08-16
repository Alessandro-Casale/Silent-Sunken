package com.alessandro.silentsunken.infrastructure.fx;

import com.alessandro.silentsunken.SilentSunken;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.LayeringTransform;
import net.minecraft.client.renderer.rendertype.OutputTarget;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;

public class SilentRenderTypes {
    public static final RenderPipeline RESONANCE_OUTLINE_PIPELINE = RenderPipeline.builder(RenderPipelines.LINES_SNIPPET)
        .withLocation(Identifier.fromNamespaceAndPath(SilentSunken.MODID, "pipeline/resonance_outline"))
        .withDepthStencilState(new DepthStencilState(CompareOp.ALWAYS_PASS, false))
        .build();

    public static final RenderType RESONANCE_OUTLINE = RenderType.create(
        "silentsunken_resonance_outline",
        RenderSetup.builder(RESONANCE_OUTLINE_PIPELINE)
            .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
            .setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
            .createRenderSetup()
    );
}
