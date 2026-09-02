package com.alessandro.silentsunken.infrastructure.item;

import com.alessandro.silentsunken.api.ResonanceSounds;
import com.alessandro.silentsunken.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.silentsunken.api.resonance.ResonanceUpgrade;
import com.alessandro.silentsunken.engine.SilentManager;
import com.alessandro.silentsunken.infrastructure.tag.SilentTags;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

@NotNullParamsAndMethodsReturn
public class ResonantHammerItem extends Item implements ResonanceUpgrade {
    public ResonantHammerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getLevel() instanceof ServerLevel level) {
            var player = context.getPlayer();
            if (player == null) { return InteractionResult.PASS; }

            var clickedPos = context.getClickedPos();
            var block = level.getBlockState(clickedPos);
            if (!block.is(SilentTags.CLICKABLE_WITH_RESONANT_HAMMER)) { return InteractionResult.PASS; }

            var stack = context.getItemInHand();
            if (player.getCooldowns().isOnCooldown(stack)) { return InteractionResult.FAIL; }

            var playerPos = player.blockPosition();
            var radius = false ? 64 : 32; // TODO: implement boosted search radius based on player position
            var positions = SilentManager.SEARCH_INSTANCE.sphericSearch(level, clickedPos, radius, state -> state.is(SilentTags.DISCOVERABLE_WITH_SCAN_SESSION));

            ResonanceSounds.playImpactSound(level, clickedPos, false);
            SilentManager.OUTLINE_INSTANCE.startScanSession(level, clickedPos, radius, 3, 8, positions);

            var cooldown = stack.get(DataComponents.USE_COOLDOWN).ticks();
            player.getCooldowns().addCooldown(stack, cooldown);
            stack.hurtAndBreak(1, player, context.getHand().asEquipmentSlot());

            return InteractionResult.SUCCESS;
        }

        return super.useOn(context);
    }
}
