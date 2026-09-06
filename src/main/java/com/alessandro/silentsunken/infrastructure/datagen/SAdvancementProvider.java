package com.alessandro.silentsunken.infrastructure.datagen;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.silentsunken.infrastructure.codec.SoundMaterial;
import com.alessandro.silentsunken.infrastructure.registry.SilentBlocks;
import com.alessandro.silentsunken.infrastructure.registry.SilentCriteriaTriggers;
import com.alessandro.silentsunken.infrastructure.registry.SilentItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.advancements.criterion.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@NotNullParamsAndMethodsReturn
public class SAdvancementProvider implements AdvancementSubProvider {
    private static final String ID_PREFIX = SilentSunken.MODID + ":";

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> output) {
        var root = Advancement.Builder.advancement()
            .display(
                SilentItems.RESONANT_CRYSTAL.get(),
                title("root"), description("root"),
                // Identifier.fromNamespaceAndPath(SilentSunken.MODID, "gui/advancements/backgrounds/_"),
                Identifier.fromNamespaceAndPath(SilentSunken.MODID, "block/resonant_crystal_ore"),
                AdvancementType.TASK, true, false, false
            )
            .addCriterion("tick", PlayerTrigger.TriggerInstance.tick())
            .save(output, ID_PREFIX + "root");

        var hammer = obtainAdvancement(output, root, "resonant_hammer", SilentItems.RESONANT_HAMMER.get(), AdvancementType.TASK);
        triggeredAdvancement(output, hammer, "hammer_scan", Blocks.COBBLED_DEEPSLATE, AdvancementType.TASK, SilentCriteriaTriggers.HAMMER_SCAN);

        var ore = obtainAdvancement(output, root, "resonant_crystal_ore", SilentBlocks.RESONANT_CRYSTAL_ORE.get(), AdvancementType.TASK);
        obtainAdvancement(output, ore, "resonant_crystal", SilentItems.RESONANT_CRYSTAL.get(), AdvancementType.TASK);
        triggeredAdvancement(output, ore, "resonant_ore_scan", SilentBlocks.RESONANT_CRYSTAL_ORE.get(), AdvancementType.TASK, SilentCriteriaTriggers.RESONANT_ORE_SCAN);

        var structureHolder = registries.lookupOrThrow(Registries.STRUCTURE).getOrThrow(SWorldgenProvider.RUINS_STRUCTURE);
        var ruinsDiscovered = Advancement.Builder.advancement()
            .parent(root)
            .display(Blocks.MOSSY_STONE_BRICKS, title("ruins_discovered"), description("ruins_discovered"), null, AdvancementType.TASK, true, false, false)
            .addCriterion("in_ruins", PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(structureHolder)))
            .save(output, ID_PREFIX + "ruins_discovered");

        var barrelOpened = triggeredAdvancement(output, ruinsDiscovered, "ruins_barrel_opened", SilentBlocks.MOSSY_RESONANT_BARREL.get(), AdvancementType.GOAL, SilentCriteriaTriggers.BARREL_OPENED);
        triggeredAdvancement(output, barrelOpened, "barrel_unmossed", SilentBlocks.RESONANT_BARREL.get(), AdvancementType.GOAL, SilentCriteriaTriggers.BARREL_UNMOSSED);

        var mossApplied = triggeredAdvancement(output, root, "moss_applied", Blocks.MOSS_BLOCK, AdvancementType.TASK, SilentCriteriaTriggers.MOSS_APPLIED);
        triggeredAdvancement(output, mossApplied, "moss_removed", Items.IRON_AXE, AdvancementType.TASK, SilentCriteriaTriggers.MOSS_REMOVED);

        var historianVillager = triggeredAdvancement(output, barrelOpened, "historian_villager", Items.VILLAGER_SPAWN_EGG, AdvancementType.TASK, SilentCriteriaTriggers.HISTORIAN_CONVERTED);
        var gildingAnvil = triggeredAdvancement(output, historianVillager, "gilding_anvil", Items.ANVIL, AdvancementType.TASK, SilentCriteriaTriggers.TABLET_GILDED);
        var craftingTable = obtainAdvancement(output, gildingAnvil, "resonant_crafting_table", SilentBlocks.RESONANT_CRAFTING_TABLE.get(), AdvancementType.GOAL);
        obtainAdvancement(output, craftingTable, "resonant_pickaxe", SilentItems.RESONANT_PICKAXE.get(), AdvancementType.GOAL);

        soundMaterialAdvancements(output, barrelOpened, SoundMaterial.BLUE, SilentItems.BLUE_FRAGMENTS_AND_TABLES);
        soundMaterialAdvancements(output, barrelOpened, SoundMaterial.GREEN, SilentItems.GREEN_FRAGMENTS_AND_TABLES);
        soundMaterialAdvancements(output, barrelOpened, SoundMaterial.RED, SilentItems.RED_FRAGMENTS_AND_TABLES);
        soundMaterialAdvancements(output, barrelOpened, SoundMaterial.YELLOW, SilentItems.YELLOW_FRAGMENTS_AND_TABLES);
        soundMaterialAdvancements(output, barrelOpened, SoundMaterial.PURPLE, SilentItems.PURPLE_FRAGMENTS_AND_TABLES);
    }

    private void soundMaterialAdvancements(Consumer<AdvancementHolder> output, AdvancementHolder parent, SoundMaterial material, List<DeferredItem<? extends Item>> items) {
        var fragment = fragmentAdvancement(output, parent, material, items);
        var rawTablet = obtainAdvancement(output, fragment, "raw_" + material.type() + "_tablet", items.get(4), AdvancementType.TASK);
        obtainAdvancement(output, rawTablet, "gilded_" + material.type() + "_tablet", items.get(5), AdvancementType.GOAL);
    }

    private AdvancementHolder obtainAdvancement(Consumer<AdvancementHolder> output, AdvancementHolder parent, String id, ItemLike item, AdvancementType type) {
        return Advancement.Builder.advancement()
            .parent(parent)
            .display(item, title(id), description(id), null, type, true, false, false)
            .addCriterion("obtained", InventoryChangeTrigger.TriggerInstance.hasItems(item))
            .save(output, ID_PREFIX + id);
    }

    private AdvancementHolder triggeredAdvancement(Consumer<AdvancementHolder> output, AdvancementHolder parent, String id, ItemLike icon, AdvancementType type, DeferredHolder<CriterionTrigger<?>, PlayerTrigger> trigger) {
        return Advancement.Builder.advancement()
            .parent(parent)
            .display(icon, title(id), description(id), null, type, true, false, false)
            .addCriterion("triggered", trigger.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty())))
            .save(output, ID_PREFIX + id);
    }

    private AdvancementHolder fragmentAdvancement(Consumer<AdvancementHolder> output, AdvancementHolder parent, SoundMaterial material, List<DeferredItem<? extends Item>> items) {
        var id = "fragment_" + material.type();
        return Advancement.Builder.advancement()
            .parent(parent)
            .display(items.getFirst(), title(id), description(id), null, AdvancementType.TASK, true, false, false)
            .addCriterion("corner_1", InventoryChangeTrigger.TriggerInstance.hasItems(items.getFirst()))
            .addCriterion("corner_2", InventoryChangeTrigger.TriggerInstance.hasItems(items.get(1)))
            .addCriterion("corner_3", InventoryChangeTrigger.TriggerInstance.hasItems(items.get(2)))
            .addCriterion("corner_4", InventoryChangeTrigger.TriggerInstance.hasItems(items.get(3)))
            .requirements(AdvancementRequirements.Strategy.OR)
            .save(output, ID_PREFIX + id);
    }

    private static Component title(String id) {
        return Component.translatable("advancements.silentsunken." + id + ".title");
    }

    private static Component description(String id) {
        return Component.translatable("advancements.silentsunken." + id + ".description");
    }
}
