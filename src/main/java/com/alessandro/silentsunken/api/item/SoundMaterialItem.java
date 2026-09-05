package com.alessandro.silentsunken.api.item;

import com.alessandro.silentsunken.infrastructure.codec.SoundMaterial;
import net.minecraft.world.item.Item;

public class SoundMaterialItem extends Item {
    private final SoundMaterial material;

    public SoundMaterialItem(Properties properties, SoundMaterial material) {
        super(properties);
        this.material = material;
    }

    public SoundMaterial getSoundMaterial() {
        return material;
    }
}
