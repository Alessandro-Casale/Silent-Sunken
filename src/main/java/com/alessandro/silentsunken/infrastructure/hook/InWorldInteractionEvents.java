package com.alessandro.silentsunken.infrastructure.hook;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.api.nullability.NotNullParams;
import com.alessandro.silentsunken.infrastructure.codec.SoundMaterial;
import com.alessandro.silentsunken.infrastructure.item.RawTabletItem;
import com.alessandro.silentsunken.infrastructure.item.ResonantHammerItem;
import com.alessandro.silentsunken.infrastructure.resource.SoundMaterialDefinitions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

@NotNullParams
@EventBusSubscriber(modid = SilentSunken.MODID)
public class InWorldInteractionEvents {
    @SubscribeEvent
    public static void onClickOverAnAnvil(PlayerInteractEvent.RightClickBlock event) {
        if (!(event.getLevel() instanceof ServerLevel level)) { return; }

        var pos = event.getPos();
        if (!level.getBlockState(pos).is(BlockTags.ANVIL)) { return; }

        var stack = event.getItemStack();
        if (!(stack.getItem() instanceof ResonantHammerItem)) { return; }

        var searchBox = new AABB(pos).inflate(0.4, 0, 0.4).expandTowards(0, 1.2, 0);
        var itemEntities = level.getEntitiesOfClass(ItemEntity.class, searchBox);

        ItemEntity rawTabletFound = null;
        SoundMaterial soundMaterial = null;
        var goldEntities = new ArrayList<ItemEntity>();
        var goldCount = 0;

        for (var itemEntity : itemEntities) {
            var itemStack = itemEntity.getItem();

            if (itemStack.is(Items.GOLD_INGOT)) {
                goldCount += itemStack.getCount();
                goldEntities.add(itemEntity);
                continue;
            }

            if (itemStack.getItem() instanceof RawTabletItem rawTablet) {
                if (rawTabletFound == null) {
                    rawTabletFound = itemEntity;
                    soundMaterial = rawTablet.getSoundMaterial();
                }
            }
        }

        if (rawTabletFound == null && goldCount == 0) { return; }

        var player = event.getEntity();
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);

        if (rawTabletFound == null) {
            denyGildingFeedback(level, pos);
            player.sendOverlayMessage(Component.translatable("message.silentsunken.gilding.missing_tablet"));
            return;
        }

        var definition = SoundMaterialDefinitions.INSTANCE.getDefinition(soundMaterial);
        var requiredGoldIngots = definition.requiredGoldIngotsForConversion();
        if (goldCount < requiredGoldIngots) {
            denyGildingFeedback(level, pos);
            player.sendOverlayMessage(Component.translatable("message.silentsunken.gilding.missing_gold", requiredGoldIngots - goldCount));
            return;
        }

        shrinkItemEntity(rawTabletFound, 1);
        consumeGold(goldEntities, requiredGoldIngots);

        var gildedTablet = new ItemStack(definition.gildedTablet());
        level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, gildedTablet.copy()));

        stack.hurtAndBreak(1, player, event.getHand().asEquipmentSlot());

        gildingSuccessFeedback(level, pos, definition.color());
        player.sendOverlayMessage(Component.translatable("message.silentsunken.gilding.success", gildedTablet.getHoverName()));
    }

    private static void consumeGold(List<ItemEntity> goldEntities, int amountNeeded) {
        var remaining = amountNeeded;
        for (var entity : goldEntities) {
            if (remaining <= 0) { break; }

            var toRemove = Math.min(entity.getItem().getCount(), remaining);
            shrinkItemEntity(entity, toRemove);
            remaining -= toRemove;
        }
    }

    private static void shrinkItemEntity(ItemEntity entity, int amount) {
        var stack = entity.getItem().copy();
        stack.shrink(amount);

        if (stack.isEmpty()) {
            entity.discard();
        } else {
            entity.setItem(stack);
        }
    }

    private static void gildingSuccessFeedback(ServerLevel level, BlockPos pos, int color) {
        var x = pos.getX() + 0.5;
        var y = pos.getY() + 1.1;
        var z = pos.getZ() + 0.5;

        var dust = new DustParticleOptions(color, 1.3f);
        level.sendParticles(dust, x, y, z, 25, 0.35, 0.3, 0.35, 0);
        level.playSound(null, pos, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1.0f, 1.1f);
        level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1, 1.3f);
    }

    private static void denyGildingFeedback(ServerLevel level, BlockPos pos) {
        level.sendParticles(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 1.1, pos.getZ() + 0.5, 6, 0.15, 0.15, 0.15, 0.01);
        level.playSound(null, pos, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 0.5f, 0.6f);
    }
}
