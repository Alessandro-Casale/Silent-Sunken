package com.alessandro.silentsunken.infrastructure.fx;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.silentsunken.infrastructure.registry.SilentVillagerProfessions;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.npc.BabyVillagerModel;
import net.minecraft.client.model.npc.VillagerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.entity.layers.VillagerProfessionLayer;
import net.minecraft.client.renderer.entity.state.VillagerRenderState;
import net.minecraft.resources.Identifier;

@NotNullParamsAndMethodsReturn
public class SilentVillagerRenderer extends VillagerRenderer {
    private static final Identifier HISTORIAN_LOCATION = Identifier.fromNamespaceAndPath(SilentSunken.MODID, "textures/entity/villager/profession/historian.png");

    public SilentVillagerRenderer(EntityRendererProvider.Context context) {
        super(context);

        layers.removeIf(layer -> layer instanceof VillagerProfessionLayer);
        addLayer(new HistorianAwareProfessionLayer(
            this,
            context.getResourceManager(),
            "villager",
            new VillagerModel(context.bakeLayer(ModelLayers.VILLAGER_NO_HAT)),
            new BabyVillagerModel(context.bakeLayer(ModelLayers.VILLAGER_BABY_NO_HAT))
        ));
    }

    @Override
    public Identifier getTextureLocation(VillagerRenderState state) {
        var data = state.getVillagerData();

        if (data != null && data.profession().is(SilentVillagerProfessions.HISTORIAN.getKey())) {
            return HISTORIAN_LOCATION;
        }

        return super.getTextureLocation(state);
    }
}
