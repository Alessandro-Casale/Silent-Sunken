package com.alessandro.silentsunken.infrastructure.item;

import com.alessandro.silentsunken.api.item.SoundMaterialItem;
import com.alessandro.silentsunken.infrastructure.codec.SoundMaterial;

public class FragmentItem extends SoundMaterialItem {
    private final int cornerIndex;

    public FragmentItem(Properties properties, SoundMaterial material, int cornerIndex) {
        super(properties, material);
        this.cornerIndex = cornerIndex;
    }

    public int getCornerIndex() {
        return cornerIndex;
    }
}
