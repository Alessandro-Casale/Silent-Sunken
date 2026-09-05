package com.alessandro.silentsunken.infrastructure.hook;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.api.nullability.NotNullParams;
import com.alessandro.silentsunken.infrastructure.codec.SoundMaterial;
import com.alessandro.silentsunken.infrastructure.data.HistorianProgress;
import com.alessandro.silentsunken.infrastructure.item.FragmentItem;
import com.alessandro.silentsunken.infrastructure.registry.SilentAttachments;
import com.alessandro.silentsunken.infrastructure.registry.SilentVillagerProfessions;
import com.alessandro.silentsunken.infrastructure.resource.SoundMaterialDefinitions;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.ArrayList;

@NotNullParams
@EventBusSubscriber(modid = SilentSunken.MODID)
public class HistorianCraftingEvents {
    private static final int CORNER_COUNT = 4;

    private static final int CRAFT_FRAGMENT_SWITCH_TICKS = 20;
    private static final int CRAFT_WORKING_TICKS = CRAFT_FRAGMENT_SWITCH_TICKS * CORNER_COUNT;
    private static final int CRAFT_REVEAL_TICKS = 15;
    private static final int CRAFT_TOTAL_TICKS = CRAFT_WORKING_TICKS + CRAFT_REVEAL_TICKS;
    private static final int CRAFT_PARTICLE_INTERVAL_TICKS = 4;

    private static final String CRAFT_START_KEY = "silentsunken_historian_craft_start";
    private static final String CRAFT_WINNER_MATERIAL_KEY = "silentsunken_historian_craft_winner_material";
    private static final String CRAFT_REVEALED_KEY = "silentsunken_historian_craft_revealed";

    @SubscribeEvent
    public static void onFragmentOffer(PlayerInteractEvent.EntityInteract event) {
        var player = event.getEntity();
        if (!(player.level() instanceof ServerLevel level)) { return; }
        if (!(event.getTarget() instanceof Villager villager) || villager.isBaby()) { return; }
        if (!villager.getVillagerData().profession().is(SilentVillagerProfessions.HISTORIAN.getKey())) { return; }

        if (villager.isNoAi()) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.FAIL);
            return;
        }

        var stack = player.getItemInHand(event.getHand());
        var progress = villager.getData(SilentAttachments.HISTORIAN_PROGRESS);
        var random = level.getRandom();

        if (stack.getItem() instanceof FragmentItem fragment) {
            // The player is offering a fragment to the historian villager. Check if the corner is already occupied.
            var fragmentCorner = fragment.getCornerIndex() - 1; // FragmentItem is 1-based, HistorianProgress is 0-based
            var soundMaterial = fragment.getSoundMaterial();
            var color = SoundMaterialDefinitions.INSTANCE.getDefinition(soundMaterial).color();

            if (progress.hasCornerAt(fragmentCorner)) {
                denyFragmentFeedback(level, villager);
                player.sendOverlayMessage(Component.translatable("message.silentsunken.historian.corner_occupied"));

                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.FAIL);
            } else {
                progress.setItem(fragmentCorner, stack.copyWithCount(1));
                stack.shrink(1);
                var fragmentsSoFar = progress.numberOfFragments();
                acceptFragmentFeedback(level, villager, color, fragmentsSoFar);

                if (progress.hasAllCorners()) {
                    var resource = progress.resourceStackWaitingFor(random).id();
                    player.sendOverlayMessage(Component.translatable("message.silentsunken.historian.missing_material", resource.count(), resource.getHoverName()));
                } else {
                    player.sendOverlayMessage(Component.translatable("message.silentsunken.historian.fragment_accepted", fragmentsSoFar));
                }

                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        } else {
            // The player is offering a resource to the historian villager. Check if the villager is waiting for a specific resource.
            var twin = progress.resourceStackWaitingFor(random);
            if (twin == null) {
                if (!progress.hasAllCorners()) {
                    denyResourceFeedback(level, villager);
                    player.sendOverlayMessage(Component.translatable("message.silentsunken.historian.waiting_for_fragments"));
                }

                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.FAIL);
                return;
            }

            var resource = twin.id();
            var itemHolderIndex = twin.value();
            if (!stack.is(resource.getItem())) {
                denyResourceFeedback(level, villager);
                player.sendOverlayMessage(Component.translatable("message.silentsunken.historian.invalid_material", resource.count(), resource.getHoverName()));

                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.FAIL);
                return;
            }

            var expectingCount = resource.count();
            if (stack.count() < expectingCount) {
                denyResourceFeedback(level, villager);
                player.sendOverlayMessage(Component.translatable("message.silentsunken.historian.invalid_count", expectingCount));

                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.FAIL);
                return;
            }

            progress.setItem(itemHolderIndex, resource);
            stack.shrink(expectingCount);

            if (progress.resourceStackWaitingFor(random) == null) {
                startRawTabletCrafting(level, villager, progress);
                player.sendOverlayMessage(Component.translatable("message.silentsunken.historian.crafting_started"));
            } else {
                acceptResourceFeedback(level, villager, colorForCounterpartMaterial(resource.getItem()));
            }

            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
        }
    }

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Pre event) {
        if (!(event.getEntity() instanceof Villager villager)) { return; }
        if (!(villager.level() instanceof ServerLevel level)) { return; }

        var data = villager.getPersistentData();
        if (!data.contains(CRAFT_START_KEY)) { return; }

        var elapsed = level.getGameTime() - data.getLongOr(CRAFT_START_KEY, 0L);
        var winnerMaterialName = data.getStringOr(CRAFT_WINNER_MATERIAL_KEY, "");

        if (elapsed >= CRAFT_TOTAL_TICKS) {
            finishRawTabletCrafting(level, villager, villager.getData(SilentAttachments.HISTORIAN_PROGRESS), winnerMaterialName);
        } else if (elapsed >= CRAFT_WORKING_TICKS) {
            if (!data.getBooleanOr(CRAFT_REVEALED_KEY, false)) {
                data.putBoolean(CRAFT_REVEALED_KEY, true);
                revealRawTablet(level, villager, winnerMaterialName);
            }
        } else {
            tickCraftingWorkingPhase(level, villager, (int) elapsed);
        }
    }

    private static void startRawTabletCrafting(ServerLevel level, Villager villager, HistorianProgress progress) {
        var random = level.getRandom();

        var cornerMaterials = new ArrayList<SoundMaterial>(CORNER_COUNT);
        for (var i = 0; i < CORNER_COUNT; i++) {
            if (progress.getCornerAt(i).getItem() instanceof FragmentItem fragment) {
                cornerMaterials.add(fragment.getSoundMaterial());
            }
        }
        var winnerMaterial = cornerMaterials.get(random.nextInt(cornerMaterials.size()));

        villager.getPersistentData().putLong(CRAFT_START_KEY, level.getGameTime());
        villager.getPersistentData().putString(CRAFT_WINNER_MATERIAL_KEY, winnerMaterial.name());

        villager.getNavigation().stop();
        villager.setDeltaMovement(0, 0, 0);
        villager.setNoAi(true);
        villager.setDropChance(EquipmentSlot.MAINHAND, 0);
    }

    private static void tickCraftingWorkingPhase(ServerLevel level, Villager villager, int elapsed) {
        if (elapsed % CRAFT_PARTICLE_INTERVAL_TICKS == 0) {
            var random = level.getRandom();
            var note = random.nextFloat();
            var x = villager.getX() + jitteredOffset(random, 0.6);
            var z = villager.getZ() + jitteredOffset(random, 0.6);
            level.sendParticles(ParticleTypes.NOTE, x, villager.getY() + villager.getBbHeight() + 0.2, z, 0, note, 0, 0, 1);
        }

        if (elapsed % CRAFT_FRAGMENT_SWITCH_TICKS == 0) {
            var cornerIndex = elapsed / CRAFT_FRAGMENT_SWITCH_TICKS;
            var progress = villager.getData(SilentAttachments.HISTORIAN_PROGRESS);
            var fragment = progress.getCornerAt(cornerIndex);
            villager.setItemSlot(EquipmentSlot.MAINHAND, fragment);
        }
    }

    private static void revealRawTablet(ServerLevel level, Villager villager, String winnerMaterialName) {
        var definition = SoundMaterialDefinitions.INSTANCE.getDefinition(SoundMaterial.valueOf(winnerMaterialName));
        villager.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(definition.rawTablet()));

        var x = villager.getX();
        var y = villager.getY() + villager.getBbHeight() * 0.75;
        var z = villager.getZ();
        var random = level.getRandom();
        var dust = new DustParticleOptions(definition.color(), 1.2f);
        level.sendParticles(dust, x, y, z, 20, 0.3, 0.35, 0.3, 0);
        var revealNote = jitteredNote(random, 1.0f, 0.2f);
        level.sendParticles(ParticleTypes.NOTE, x + jitteredOffset(random, 0.4), y + 0.3, z + jitteredOffset(random, 0.4), 0, revealNote, 0, 0, 1);
        level.playSound(null, villager.blockPosition(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.NEUTRAL, 1, 1.2f);
    }

    private static void finishRawTabletCrafting(ServerLevel level, Villager villager, HistorianProgress progress, String winnerMaterialName) {
        var data = villager.getPersistentData();
        data.remove(CRAFT_START_KEY);
        data.remove(CRAFT_WINNER_MATERIAL_KEY);
        data.remove(CRAFT_REVEALED_KEY);

        villager.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        villager.setNoAi(false);

        var definition = SoundMaterialDefinitions.INSTANCE.getDefinition(SoundMaterial.valueOf(winnerMaterialName));
        villager.getData(SilentAttachments.HISTORIAN_PROGRESS).recordRawTabletConversion();

        villager.spawnAtLocation(level, new ItemStack(definition.rawTablet()));
        level.playSound(null, villager.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.NEUTRAL, 0.8f, 1);
        progress.incrementFragmentsToRawTabletConversions();
    }

    private static int colorForCounterpartMaterial(Item item) {
        for (var id : SoundMaterialDefinitions.INSTANCE.ids()) {
            var definition = SoundMaterialDefinitions.INSTANCE.getDefinition(id);
            if (definition.counterpartMaterial() == item) { return definition.color(); }
        }

        return 0xFFFFFF;
    }

    private static void acceptFragmentFeedback(ServerLevel level, Villager villager, int color, int fragmentsSoFar) {
        var random = level.getRandom();
        var dust = new DustParticleOptions(color, 0.8f);
        level.sendParticles(dust, villager.getX(), villager.getY() + villager.getBbHeight() * 0.75, villager.getZ(), 5, 0.2, 0.2, 0.2, 0);
        var baseNote = Math.min(1, 0.15f + (fragmentsSoFar - 1) * 0.2f);
        var note = jitteredNote(random, baseNote, 0.1f);
        var x = villager.getX() + jitteredOffset(random, 0.4);
        var z = villager.getZ() + jitteredOffset(random, 0.4);
        level.sendParticles(ParticleTypes.NOTE, x, villager.getY() + villager.getBbHeight() + 0.2, z, 0, note, 0, 0, 1);
        level.playSound(null, villager.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.NEUTRAL, 0.6f, 1.4f);
        level.playSound(null, villager.blockPosition(), SoundEvents.VILLAGER_YES, SoundSource.NEUTRAL, 1, 1 + (fragmentsSoFar - 1) * 0.08f);
    }

    private static void denyFragmentFeedback(ServerLevel level, Villager villager) {
        var dust = new DustParticleOptions(0x8A8A8A, 0.7f);
        level.sendParticles(dust, villager.getX(), villager.getY() + villager.getBbHeight() * 0.75, villager.getZ(), 10, 0.15, 0.15, 0.15, 0);
        level.playSound(null, villager.blockPosition(), SoundEvents.VILLAGER_NO, SoundSource.NEUTRAL, 1, 1);
    }

    private static void acceptResourceFeedback(ServerLevel level, Villager villager, int color) {
        var random = level.getRandom();
        var dust = new DustParticleOptions(color, 0.9f);
        level.sendParticles(dust, villager.getX(), villager.getY() + villager.getBbHeight() * 0.75, villager.getZ(), 6, 0.2, 0.25, 0.2, 0);
        var note = jitteredNote(random, 0.6f, 0.15f);
        var x = villager.getX() + jitteredOffset(random, 0.4);
        var z = villager.getZ() + jitteredOffset(random, 0.4);
        level.sendParticles(ParticleTypes.NOTE, x, villager.getY() + villager.getBbHeight() + 0.2, z, 0, note, 0, 0, 1);
        level.playSound(null, villager.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.NEUTRAL, 0.6f, 1.4f);
        level.playSound(null, villager.blockPosition(), SoundEvents.VILLAGER_YES, SoundSource.NEUTRAL, 1, 1);
    }

    private static void denyResourceFeedback(ServerLevel level, Villager villager) {
        var dust = new DustParticleOptions(0x8A8A8A, 0.7f);
        level.sendParticles(dust, villager.getX(), villager.getY() + villager.getBbHeight() * 0.75, villager.getZ(), 4, 0.15, 0.15, 0.15, 0);
        level.playSound(null, villager.blockPosition(), SoundEvents.VILLAGER_NO, SoundSource.NEUTRAL, 1, 1);
    }

    private static float jitteredNote(RandomSource random, float base, float jitter) {
        var value = base + (random.nextFloat() - 0.5f) * jitter;
        return Math.clamp(value, 0.0f, 1.0f);
    }

    private static double jitteredOffset(RandomSource random, double range) {
        return (random.nextDouble() - 0.5) * range;
    }
}
