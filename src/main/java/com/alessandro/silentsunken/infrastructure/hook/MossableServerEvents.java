package com.alessandro.silentsunken.infrastructure.hook;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.api.BlockEntityUtils;
import com.alessandro.silentsunken.api.BlockStateUtils;
import com.alessandro.silentsunken.api.ContainerUtils;
import com.alessandro.silentsunken.api.InteractionUtils;
import com.alessandro.silentsunken.api.nullability.NotNullParams;
import com.alessandro.silentsunken.infrastructure.registry.SilentDataMaps;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;

@NotNullParams
@EventBusSubscriber(modid = SilentSunken.MODID)
public class MossableServerEvents {
    @SubscribeEvent
    public static void onItemUse(UseItemOnBlockEvent event) {
        if (event.getUsePhase() != UseItemOnBlockEvent.UsePhase.ITEM_BEFORE_BLOCK) { return; }

        var player = event.getPlayer();
        var hand = event.getHand();
        if (player == null) { return; }
        if (InteractionUtils.playerHasBlockingItemUseIntent(player, hand)) { return; }

        var level = event.getLevel();
        var context = event.getUseOnContext();
        var pos = context.getClickedPos();
        var originalState = level.getBlockState(pos);
        var stack = event.getItemStack();

        var mossVariantData = originalState.getData(SilentDataMaps.MOSSABLES);
        var nonMossVariant = SilentDataMaps.INVERSE_MOSS_VARIANT.getOrDefault(originalState.getBlock(), null);

        boolean isApply = mossVariantData != null && stack.is(ItemTags.MOSS_BLOCKS);
        boolean isRemove = !isApply && nonMossVariant != null && stack.getItem() instanceof AxeItem;

        if (!isApply && !isRemove) { return; }

        if (!(level instanceof ServerLevel serverLevel)) {
            event.cancelWithResult(InteractionResult.SUCCESS);
            return;
        }

        boolean handled = isApply
            ? applyMoss(serverLevel, stack, pos, originalState, mossVariantData.mossyVariant())
            : removeMoss(player, hand, serverLevel, stack, pos, originalState, nonMossVariant);

        if (handled) {
            event.cancelWithResult(InteractionResult.SUCCESS);
        }
    }

    private static boolean applyMoss(ServerLevel level, ItemStack moss, BlockPos pos, BlockState originalState, Block mossVariant) {
        var newState = BlockStateUtils.copyProperties(originalState, mossVariant);
        if (!ContainerUtils.areContainersCompatible(level, pos, newState)) { return false; }

        var data = BlockEntityUtils.captureData(level, pos);

        level.setBlockAndUpdate(pos, newState);
        BlockEntityUtils.restoreData(level, pos, data);

        moss.shrink(1);
        return true;
    }

    private static boolean removeMoss(Player player, InteractionHand hand, ServerLevel level, ItemStack axe, BlockPos pos, BlockState originalState, Block nonMossVariant) {
        var newState = BlockStateUtils.copyProperties(originalState, nonMossVariant);
        if (!ContainerUtils.areContainersCompatible(level, pos, newState)) { return false; }

        var data = BlockEntityUtils.captureData(level, pos);

        level.setBlockAndUpdate(pos, newState);
        BlockEntityUtils.restoreData(level, pos, data);
        axe.hurtAndBreak(1, player, hand.asEquipmentSlot());
        return true;
    }
}
