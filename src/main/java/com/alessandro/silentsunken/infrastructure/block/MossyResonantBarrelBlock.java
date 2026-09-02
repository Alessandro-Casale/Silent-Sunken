package com.alessandro.silentsunken.infrastructure.block;

import com.alessandro.silentsunken.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.silentsunken.infrastructure.blockentity.MossyResonantBarrelBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

@NotNullParamsAndMethodsReturn
public class MossyResonantBarrelBlock extends BaseResonantBarrelBlock {
    public static final MapCodec<MossyResonantBarrelBlock> CODEC = simpleCodec(MossyResonantBarrelBlock::new);

    public MossyResonantBarrelBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new MossyResonantBarrelBlockEntity(blockPos, blockState);
    }

    @Override
    protected @Nullable MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        if (state.getValue(LOCKED)) { return null; }

        return level.getBlockEntity(pos) instanceof MossyResonantBarrelBlockEntity crate ? crate : null;
    }
}
