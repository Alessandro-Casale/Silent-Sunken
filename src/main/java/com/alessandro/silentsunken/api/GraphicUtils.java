package com.alessandro.silentsunken.api;

import com.alessandro.silentsunken.api.nullability.NotNullParamsAndMethodsReturn;
import net.minecraft.world.phys.Vec3;

@NotNullParamsAndMethodsReturn
public class GraphicUtils {
    public static Vec3 adjustToCameraPos(Vec3 toAdjust, Vec3 cameraPos) {
        return toAdjust.subtract(cameraPos);
    }
}
