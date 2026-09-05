package com.alessandro.silentsunken.infrastructure.hook;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.api.nullability.NotNullParams;
import com.alessandro.silentsunken.infrastructure.registry.SilentItems;
import com.alessandro.silentsunken.infrastructure.registry.SilentParticles;
import com.alessandro.silentsunken.infrastructure.registry.SilentVillagerProfessions;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ARGB;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@NotNullParams
@EventBusSubscriber(modid = SilentSunken.MODID)
public class VillagerEvents {
    private static final int FREEZE_TICKS = 60;
    private static final String FREEZE_UNTIL_TAG = "silentsunken_historian_freeze_until";

    private static final int SPIRAL_TURNS = 4;
    private static final double SPIRAL_RADIUS = 0.6;
    // Sampled straight from historian.png: the darkest blue and the lightest/brightest green
    // actually present in the texture, so the spiral reads as an extension of the robe itself.
    private static final int HISTORIAN_DARK_BLUE = 0x0A3951;
    private static final int HISTORIAN_LIGHT_GREEN = 0x307E26;
    private static final float DUST_SCALE = 1.1f;

    // How many raw-tablet conversions a Historian needs under its belt before it's trusted to gild
    // them. Rolled once, right when it becomes a Historian, so the threshold (3-5) is fixed
    // per-villager rather than re-rolled on every check. Stored in HistorianProgress (see
    // SilentAttachments), read by HistorianCraftingEvents.
    private static final int GILD_UNLOCK_MIN = 3;
    private static final int GILD_UNLOCK_MAX = 5;

    @SubscribeEvent
    public static void onVillagerInteract(PlayerInteractEvent.EntityInteract event) {
        var player = event.getEntity();
        if (!(player.level() instanceof ServerLevel level)) { return; }

        if (!(event.getTarget() instanceof Villager villager) || villager.isBaby()) { return; }
        if (!villager.getVillagerData().profession().is(VillagerProfession.NONE)) { return; }

        var stack = player.getItemInHand(event.getHand());
        if (!stack.is(SilentItems.RESONANT_CRYSTAL.get())) { return; }

        stack.shrink(1);

//        var gildUnlockAt = GILD_UNLOCK_MIN + villager.getRandom().nextInt(GILD_UNLOCK_MAX - GILD_UNLOCK_MIN + 1);
//        villager.getData(SilentAttachments.HISTORIAN_PROGRESS).setGildUnlockAt(gildUnlockAt);

        freezeDuringConversion(villager);
        villager.getPersistentData().putLong(FREEZE_UNTIL_TAG, level.getGameTime() + FREEZE_TICKS);

        level.playSound(null, villager.blockPosition(), SoundEvents.VILLAGER_WORK_LIBRARIAN, SoundSource.NEUTRAL, 1.0f, 1.0f);

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Pre event) {
        if (event.getEntity().level().isClientSide()) { return; }
        if (!(event.getEntity() instanceof Villager villager)) { return; }
        if (!(villager.level() instanceof ServerLevel level)) { return; }

        var data = villager.getPersistentData();
        if (!data.contains(FREEZE_UNTIL_TAG)) { return; }

        var freezeUntil = data.getLongOr(FREEZE_UNTIL_TAG, 0L);
        var remaining = freezeUntil - level.getGameTime();

        if (remaining <= 0) {
            revertFreeze(villager);
            data.remove(FREEZE_UNTIL_TAG);

            villager.setVillagerData(villager.getVillagerData().withProfession(SilentVillagerProfessions.HISTORIAN));
            villager.setVillagerXp(1); // Prevent villager reset as it has no Poi
            villager.refreshBrain(level);

            level.sendParticles(SilentParticles.HISTORIAN_SPARK.get(), villager.getX(), villager.getY() + villager.getBbHeight() / 2.0, villager.getZ(), 14, 0.4, 0.5, 0.4, 0.0);
            return;
        }

        var elapsed = FREEZE_TICKS - remaining;
        spawnSpiralStep(level, villager, (float) elapsed / FREEZE_TICKS);
    }

    private static void freezeDuringConversion(Villager villager) {
        villager.getNavigation().stop();
        villager.setDeltaMovement(0.0, 0.0, 0.0);
        villager.setNoAi(true);
    }

    private static void revertFreeze(Villager villager) {
        villager.setNoAi(false);
    }

    private static void spawnSpiralStep(ServerLevel level, Villager villager, float t) {
        var baseAngle = t * SPIRAL_TURNS * 2 * Math.PI;
        var y = villager.getY() + t * villager.getBbHeight();
        var color = new DustParticleOptions(ARGB.srgbLerp(t, HISTORIAN_DARK_BLUE, HISTORIAN_LIGHT_GREEN), DUST_SCALE);

        for (var strand = 0; strand < 2; strand++) {
            var angle = baseAngle + strand * Math.PI;
            var x = villager.getX() + Math.cos(angle) * SPIRAL_RADIUS;
            var z = villager.getZ() + Math.sin(angle) * SPIRAL_RADIUS;
            level.sendParticles(color, x, y, z, 1, 0, 0, 0, 0);
        }
    }
}
