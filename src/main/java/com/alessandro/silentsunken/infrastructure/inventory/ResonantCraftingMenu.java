package com.alessandro.silentsunken.infrastructure.inventory;

import com.alessandro.silentsunken.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.silentsunken.infrastructure.blockentity.ResonantCraftingTableBlockEntity;
import com.alessandro.silentsunken.infrastructure.recipe.ResonantCraftingInput;
import com.alessandro.silentsunken.infrastructure.registry.SilentBlocks;
import com.alessandro.silentsunken.infrastructure.registry.SilentMenuTypes;
import com.alessandro.silentsunken.infrastructure.registry.SilentRecipeTypes;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

@NotNullParamsAndMethodsReturn
public class ResonantCraftingMenu extends AbstractContainerMenu {
    public static final int RESULT_SLOT = 0;
    private static final int GRID_SLOT_START = 1;
    private static final int GRID_SLOT_COUNT = 9;
    private static final int GRID_SLOT_END = GRID_SLOT_START + GRID_SLOT_COUNT;
    private static final int CATALYST_SLOT = GRID_SLOT_END;
    private static final int INV_SLOT_START = CATALYST_SLOT + 1;
    private static final int INV_SLOT_END = INV_SLOT_START + 27;
    private static final int USE_ROW_SLOT_START = INV_SLOT_END;
    private static final int USE_ROW_SLOT_END = USE_ROW_SLOT_START + 9;

    private final ContainerLevelAccess access;
    private final Player player;
    private final ResonantCraftingGridView grid;
    private final Slot catalystSlot;
    private final ResultContainer resultContainer = new ResultContainer();

    public ResonantCraftingMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(ResonantCraftingTableBlockEntity.CONTAINER_SIZE), ContainerLevelAccess.NULL);
    }

    public ResonantCraftingMenu(int containerId, Inventory inventory, ResonantCraftingTableBlockEntity blockEntity, ContainerLevelAccess access) {
        this(containerId, inventory, (Container) blockEntity, access);
    }

    private ResonantCraftingMenu(int containerId, Inventory inventory, Container container, ContainerLevelAccess access) {
        super(SilentMenuTypes.RESONANT_CRAFTING_TABLE.get(), containerId);
        checkContainerSize(container, ResonantCraftingTableBlockEntity.CONTAINER_SIZE);
        this.access = access;
        this.player = inventory.player;

        var notifyingContainer = new NotifyingContainerView(container, this);
        this.grid = new ResonantCraftingGridView(notifyingContainer);

        var catalystSlotLocal = new Slot(notifyingContainer, ResonantCraftingTableBlockEntity.CATALYST_SLOT, 128, 18);

        addSlot(new ResonantResultSlot(player, grid, catalystSlotLocal, resultContainer, RESULT_SLOT, 124, 44));
        for (var row = 0; row < ResonantCraftingGridView.HEIGHT; row++) {
            for (var col = 0; col < ResonantCraftingGridView.WIDTH; col++) {
                addSlot(new Slot(notifyingContainer, col + row * ResonantCraftingGridView.WIDTH, 30 + col * 18, 17 + row * 18));
            }
        }

        this.catalystSlot = addSlot(catalystSlotLocal);
        this.addStandardInventorySlots(inventory, 8, 84);
    }

    @Override
    public void slotsChanged(Container container) {
        access.execute((level, _) -> {
            if (level instanceof ServerLevel serverLevel) {
                updateResult(serverLevel);
            }
        });
    }

    private void updateResult(ServerLevel level) {
        var serverPlayer = (ServerPlayer) player;
        var recipeManager = level.getServer().getRecipeManager();
        var result = ItemStack.EMPTY;

        var catalyst = catalystSlot.getItem();
        if (!catalyst.isEmpty()) {
            var resonantInput = new ResonantCraftingInput(grid.asCraftInput(), catalyst);
            var resonantRecipe = recipeManager.getRecipeFor(SilentRecipeTypes.RESONANT_CRAFTING.get(), resonantInput, level);

            if (resonantRecipe.isPresent()) {
                var recipeHolder = resonantRecipe.get();

                if (resultContainer.setRecipeUsed(serverPlayer, recipeHolder)) {
                    var recipeResult = recipeHolder.value().assemble(resonantInput);
                    if (recipeResult.isItemEnabled(level.enabledFeatures())) {
                        result = recipeResult;
                    }
                }
            }
        }

        if (result.isEmpty()) {
            var vanillaInput = grid.asCraftInput();
            var vanillaFallbackRecipe = recipeManager.getRecipeFor(RecipeType.CRAFTING, vanillaInput, level);

            if (vanillaFallbackRecipe.isPresent()) {
                var recipeHolder = vanillaFallbackRecipe.get();

                if (resultContainer.setRecipeUsed(serverPlayer, recipeHolder)) {
                    var recipeResult = recipeHolder.value().assemble(vanillaInput);
                    if (recipeResult.isItemEnabled(level.enabledFeatures())) {
                        result = recipeResult;
                    }
                }
            }
        }

        resultContainer.setItem(0, result);
        setRemoteSlot(RESULT_SLOT, result);
        serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(containerId, incrementStateId(), RESULT_SLOT, result));
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, SilentBlocks.RESONANT_CRAFTING_TABLE.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        var clicked = ItemStack.EMPTY;
        var slot = slots.get(slotIndex);

        if (slot.hasItem()) {
            var stack = slot.getItem();
            clicked = stack.copy();
            if (slotIndex == RESULT_SLOT) {
                stack.getItem().onCraftedBy(stack, player);
                if (!moveItemStackTo(stack, INV_SLOT_START, USE_ROW_SLOT_END, true)) { return ItemStack.EMPTY; }
                slot.onQuickCraft(stack, clicked);
            } else if (slotIndex >= GRID_SLOT_START && slotIndex < INV_SLOT_START) {
                if (!moveItemStackTo(stack, INV_SLOT_START, USE_ROW_SLOT_END, false)) { return ItemStack.EMPTY; }
            } else if (slotIndex >= INV_SLOT_START && slotIndex < USE_ROW_SLOT_END) {
                if (!moveItemStackTo(stack, GRID_SLOT_START, CATALYST_SLOT + 1, false)) {
                    if (slotIndex < INV_SLOT_END) {
                        if (!moveItemStackTo(stack, USE_ROW_SLOT_START, USE_ROW_SLOT_END, false)) { return ItemStack.EMPTY; }
                    } else if (!moveItemStackTo(stack, INV_SLOT_START, INV_SLOT_END, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!moveItemStackTo(stack, INV_SLOT_START, USE_ROW_SLOT_END, false)) {
                return ItemStack.EMPTY;
            }

            if (stack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == clicked.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
            if (slotIndex == RESULT_SLOT) {
                player.drop(stack, false);
            }
        }

        return clicked;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack carried, Slot target) {
        return target.container != resultContainer && super.canTakeItemForPickAll(carried, target);
    }
}
