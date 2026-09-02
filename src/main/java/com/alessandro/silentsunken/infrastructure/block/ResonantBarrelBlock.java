package com.alessandro.silentsunken.infrastructure.block;

import com.alessandro.silentsunken.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.silentsunken.infrastructure.blockentity.ResonantBarrelBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

@NotNullParamsAndMethodsReturn
public class ResonantBarrelBlock extends BaseResonantBarrelBlock {
    public static final MapCodec<ResonantBarrelBlock> CODEC = simpleCodec(ResonantBarrelBlock::new);

    public ResonantBarrelBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ResonantBarrelBlockEntity(blockPos, blockState);
    }

    @Override
    protected @Nullable MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        if (state.getValue(LOCKED)) { return null; }

        return level.getBlockEntity(pos) instanceof ResonantBarrelBlockEntity crate ? crate : null;
    }
}
