package com.alessandro.silentsunken.infrastructure.blockentity;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.silentsunken.api.nullability.Nullable;
import com.alessandro.silentsunken.api.resonance.SoundSensible;
import com.alessandro.silentsunken.engine.SilentManager;
import com.alessandro.silentsunken.infrastructure.block.BaseResonantBarrelBlock;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NotNullParamsAndMethodsReturn
public abstract class BaseResonantBarrelBlockEntity extends RandomizableContainerBlockEntity implements SoundSensible {
    private static final ResourceKey<LootTable> LOOT_TABLE = ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(SilentSunken.MODID, "chests/ruins"));
    private static final String CURRENT_PHASE_DEFINITION_KEY = "CurrentPhase";
    private static final String PHASES_DEFINITION_KEY = "Phases";
    private static final String INTERCEPT_RADIUS_DEFINITION_KEY = "InterceptRadius";

    private NonNullList<ItemStack> items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
    private int currentPhase = 0;
    private @Nullable List<Identifier> phaseIds;
    private int interceptRadius = 12;
    private boolean initialLoad = false;

    protected BaseResonantBarrelBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState) {
        super(type, worldPosition, blockState);
    }

    @Override
    public int getCurrentPhase() {
        return currentPhase;
    }

    @Override
    public @Nullable List<Identifier> getPhaseIds() {
        return phaseIds;
    }

    @Override
    public void interceptSound(ServerLevel level, BlockPos pos, SoundEvent sound) {
        var state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof BaseResonantBarrelBlock) || !state.getValue(BaseResonantBarrelBlock.LOCKED)) { return; }
        if (!(level.getBlockEntity(pos) instanceof BaseResonantBarrelBlockEntity crate)) { return; }
        currentPhase += 1;

        if (!isWaitingForAnotherSound()) {
            level.setBlockAndUpdate(pos, state.setValue(BaseResonantBarrelBlock.LOCKED, false));
            crate.setLootTable(LOOT_TABLE);
            crate.setChanged();

            level.playSound(null, pos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1.0f, 1.2f);
            level.sendParticles(ParticleTypes.END_ROD, pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5, 24, 0.35, 0.35, 0.35, 0.04);
        }
    }

    @Override
    public int interceptSoundRadius() {
        return interceptRadius;
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);

        if (level instanceof ServerLevel serverLevel) {
            SilentManager.SOUND_LISTENER_INSTANCE.register(serverLevel, getBlockPos());
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();

        if (level instanceof ServerLevel serverLevel) {
            SilentManager.SOUND_LISTENER_INSTANCE.unregister(serverLevel, getBlockPos());
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        if (!tryLoadLootTable(input)) {
            ContainerHelper.loadAllItems(input, items);
        }

        interceptRadius = input.getIntOr(INTERCEPT_RADIUS_DEFINITION_KEY, 12);
        currentPhase = input.getIntOr(CURRENT_PHASE_DEFINITION_KEY, 0);

        var previousPhaseIds = phaseIds == null ? null : new ArrayList<>(phaseIds);
        var rawList = input.list(PHASES_DEFINITION_KEY, Identifier.CODEC).orElse(null);
        if (rawList != null) {
            phaseIds = Lists.newArrayList(rawList);
        } else {
            phaseIds = null;
        }

        if (!initialLoad && !Objects.equals(previousPhaseIds, phaseIds)) {
            lockForAssignedHint();
        }

        initialLoad = true;
    }

    private void lockForAssignedHint() {
        if (level == null) { return; }

        var state = level.getBlockState(worldPosition);
        if (state.getBlock() instanceof BaseResonantBarrelBlock && !state.getValue(BaseResonantBarrelBlock.LOCKED)) {
            level.setBlockAndUpdate(worldPosition, state.setValue(BaseResonantBarrelBlock.LOCKED, true));
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        if (!trySaveLootTable(output)) {
            ContainerHelper.saveAllItems(output, items);
        }

        output.putInt(INTERCEPT_RADIUS_DEFINITION_KEY, interceptRadius);
        output.putInt(CURRENT_PHASE_DEFINITION_KEY, currentPhase);

        if (phaseIds != null) {
            var list = output.list(PHASES_DEFINITION_KEY, Identifier.CODEC);
            phaseIds.forEach(list::add);

            if (phaseIds.isEmpty()) {
                output.discard(PHASES_DEFINITION_KEY);
            }
        }
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }
}
