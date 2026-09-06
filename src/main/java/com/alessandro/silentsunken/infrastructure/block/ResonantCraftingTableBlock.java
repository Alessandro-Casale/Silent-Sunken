package com.alessandro.silentsunken.infrastructure.block;

import com.alessandro.silentsunken.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.silentsunken.infrastructure.blockentity.ResonantCraftingTableBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

@NotNullParamsAndMethodsReturn
public class ResonantCraftingTableBlock extends BaseEntityBlock {
    public static final MapCodec<ResonantCraftingTableBlock> CODEC = simpleCodec(ResonantCraftingTableBlock::new);

    public ResonantCraftingTableBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ResonantCraftingTableBlockEntity(blockPos, blockState);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level instanceof ServerLevel) {
            var menuProvider = getMenuProvider(state, level, pos);
            if (menuProvider != null) { player.openMenu(menuProvider); }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected @Nullable MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof ResonantCraftingTableBlockEntity blockEntity ? blockEntity : null;
    }
}
