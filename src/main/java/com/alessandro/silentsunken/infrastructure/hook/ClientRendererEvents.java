package com.alessandro.silentsunken.infrastructure.hook;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.api.nullability.NotNullParams;
import com.alessandro.silentsunken.infrastructure.fx.SilentVillagerRenderer;
import net.minecraft.world.entity.EntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@NotNullParams
@EventBusSubscriber(modid = SilentSunken.MODID, value = Dist.CLIENT)
public class ClientRendererEvents {
    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityType.VILLAGER, SilentVillagerRenderer::new);
    }
}
