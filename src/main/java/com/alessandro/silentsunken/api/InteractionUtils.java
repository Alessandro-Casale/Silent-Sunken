package com.alessandro.silentsunken.api;

import com.alessandro.silentsunken.api.nullability.NotNullParams;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

@NotNullParams
public class InteractionUtils {
    public static boolean playerHasBlockingItemUseIntent(Player player, InteractionHand hand) {
        return hand == InteractionHand.MAIN_HAND && player.getOffhandItem().has(DataComponents.BLOCKS_ATTACKS) && !player.isSecondaryUseActive();
    }
}
