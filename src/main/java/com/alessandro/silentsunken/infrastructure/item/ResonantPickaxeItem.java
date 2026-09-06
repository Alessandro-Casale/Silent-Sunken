package com.alessandro.silentsunken.infrastructure.item;

import com.alessandro.silentsunken.api.nullability.NotNullParamsAndMethodsReturn;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;

@NotNullParamsAndMethodsReturn
public class ResonantPickaxeItem extends Item {
    private static boolean aoeInProgress = false;

    public ResonantPickaxeItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        var result = super.mineBlock(stack, level, state, pos, entity);
        if (!(entity instanceof ServerPlayer player)) { return result; }

        if (state.is(BlockTags.MINEABLE_WITH_PICKAXE) && !aoeInProgress && !player.isShiftKeyDown()) {
            aoeInProgress = true;

            try {
                breakSurroundingBlocks(level, pos, entity, player);
            } finally {
                aoeInProgress = false;
            }
        }

        return result;
    }

    private void breakSurroundingBlocks(Level level, BlockPos center, LivingEntity entity, ServerPlayer serverPlayer) {
        var axis = Direction.getApproximateNearest(entity.getLookAngle()).getAxis();

        var offsets = new ArrayList<BlockPos>(8);
        for (var x = -1; x <= 1; x++) {
            for (var z = -1; z <= 1; z++) {
                if (x == 0 && z == 0) { continue; }

                offsets.add(switch (axis) {
                    case X -> center.offset(0, x, z);
                    case Y -> center.offset(x, 0, z);
                    case Z -> center.offset(x, z, 0);
                });
            }
        }

        for (var pos : offsets) {
            if (level.isOutsideBuildHeight(pos)) { continue; }
            var state = level.getBlockState(pos);
            if (state.isAir() || !state.is(BlockTags.MINEABLE_WITH_PICKAXE)) { continue; }
            if (serverPlayer.getMainHandItem().isEmpty()) { break; }

            serverPlayer.gameMode.destroyBlock(pos);
        }
    }
}
