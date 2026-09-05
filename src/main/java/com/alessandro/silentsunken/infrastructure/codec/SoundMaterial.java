package com.alessandro.silentsunken.infrastructure.codec;

import com.alessandro.silentsunken.api.nullability.NotNullMethodsReturn;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;

@NotNullMethodsReturn
public enum SoundMaterial implements StringRepresentable {
    BLUE, GREEN, RED, YELLOW, PURPLE;

    @Override
    public String getSerializedName() {
        return this.type();
    }

    public String type() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
