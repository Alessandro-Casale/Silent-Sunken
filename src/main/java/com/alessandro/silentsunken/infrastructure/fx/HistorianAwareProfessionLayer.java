package com.alessandro.silentsunken.infrastructure.fx;

import com.alessandro.silentsunken.api.nullability.NotNullParams;
import com.alessandro.silentsunken.infrastructure.registry.SilentVillagerProfessions;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.npc.VillagerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.VillagerProfessionLayer;
import net.minecraft.client.renderer.entity.state.VillagerRenderState;
import net.minecraft.server.packs.resources.ResourceManager;

@NotNullParams
public class HistorianAwareProfessionLayer extends RenderLayer<VillagerRenderState, VillagerModel> {
    private final VillagerProfessionLayer<VillagerRenderState, VillagerModel> delegator;

    public HistorianAwareProfessionLayer(RenderLayerParent<VillagerRenderState, VillagerModel> renderer, ResourceManager resourceManager, String path, VillagerModel noHatModel, VillagerModel noHatBabyModel) {
        super(renderer);
        this.delegator = new VillagerProfessionLayer<>(renderer, resourceManager, path, noHatModel, noHatBabyModel);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, VillagerRenderState state, float yRot, float xRot) {
        var data = state.getVillagerData();
        if (data != null && data.profession().is(SilentVillagerProfessions.HISTORIAN.getKey())) { return; }

        this.delegator.submit(poseStack, submitNodeCollector, lightCoords, state, yRot, xRot);
    }
}
