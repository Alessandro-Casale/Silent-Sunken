package com.alessandro.silentsunken.api.fx;

import com.alessandro.silentsunken.api.nullability.NotNullMethodsReturn;
import net.minecraft.util.StringRepresentable;

@NotNullMethodsReturn
public enum Position implements StringRepresentable {
    TOP, BOTTOM;

    @Override
    public String getSerializedName() {
        return this.name();
    }
}
