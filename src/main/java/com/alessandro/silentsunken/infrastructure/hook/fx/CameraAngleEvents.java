package com.alessandro.silentsunken.infrastructure.hook.fx;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.engine.SilentClientManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;

@EventBusSubscriber(modid = SilentSunken.MODID, value = Dist.CLIENT)
public class CameraAngleEvents {
    @SubscribeEvent
    public static void computeCameraAngle(ViewportEvent.ComputeCameraAngles event) {
        var offsets = SilentClientManager.CAMERA_SHAKE_INSTANCE.buildSample();
        if (offsets == null) { return; }

        event.setYaw(event.getYaw() + (float) offsets.yaw());
        event.setPitch(event.getPitch() + (float) offsets.pitch());
        event.setRoll(event.getRoll() + (float) offsets.roll());
    }
}
