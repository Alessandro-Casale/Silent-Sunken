package com.alessandro.silentsunken.infrastructure.hook;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.api.ResonanceSounds;
import com.alessandro.silentsunken.api.nullability.NotNullParams;
import com.alessandro.silentsunken.engine.SilentManager;
import com.alessandro.silentsunken.infrastructure.tag.SilentTags;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@NotNullParams
@EventBusSubscriber(modid = SilentSunken.MODID)
public class BlockEvents {
    @SubscribeEvent
    public static void playerIsBreakingBlock(PlayerEvent.BreakSpeed event) {
        var player = event.getEntity();
        if (!(player.level() instanceof ServerLevel level)) { return; }

        var state = event.getState();
        var rawPos = event.getPosition();
        if (!state.is(SilentTags.DISCOVERABLE_WITH_SCAN_SESSION) || rawPos.isEmpty()) { return; }

        var pos = rawPos.get();
        var random = level.getRandom();

        if (random.nextFloat() < 0.014f) {
            var positions = SilentManager.SEARCH_INSTANCE.sphericSearch(level, pos, 32, s -> s.is(SilentTags.DISCOVERABLE_WITH_SCAN_SESSION));

            ResonanceSounds.playImpactSound(level, pos, false);
            SilentManager.OUTLINE_INSTANCE.startScanSession(level, pos, 32, 3, 8, positions);
        }
    }
}
