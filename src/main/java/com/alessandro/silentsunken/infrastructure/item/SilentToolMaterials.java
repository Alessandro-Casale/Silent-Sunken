package com.alessandro.silentsunken.infrastructure.item;

import com.alessandro.silentsunken.infrastructure.tag.SilentTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ToolMaterial;

public class SilentToolMaterials {
    public static final ToolMaterial RESONANT = new ToolMaterial(
        BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
        2500,
        10.0f,
        4.0f,
        15,
        SilentTags.REPAIRS_RESONANT_TOOLS
    );
}
