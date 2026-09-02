package com.alessandro.silentsunken.infrastructure.datagen;

import com.alessandro.silentsunken.SilentSunken;
import com.alessandro.silentsunken.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.silentsunken.infrastructure.registry.SilentBlocks;
import com.alessandro.silentsunken.infrastructure.tag.SilentTags;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;

import java.util.List;
import java.util.Optional;

@NotNullParamsAndMethodsReturn
public class SWorldgenProvider {
    public static final ResourceKey<StructureProcessorList> RUINS_COURTYARD_PROPS = ResourceKey.create(Registries.PROCESSOR_LIST, id("ruins_courtyard_props"));

    public static final ResourceKey<StructureTemplatePool> RUINS_COURTYARD = ResourceKey.create(Registries.TEMPLATE_POOL, id("ruins/courtyard"));
    public static final ResourceKey<StructureTemplatePool> RUINS_WALL = ResourceKey.create(Registries.TEMPLATE_POOL, id("ruins/wall"));
    public static final ResourceKey<StructureTemplatePool> RUINS_TOWER = ResourceKey.create(Registries.TEMPLATE_POOL, id("ruins/tower"));

    public static final ResourceKey<Structure> RUINS_STRUCTURE = ResourceKey.create(Registries.STRUCTURE, id("ruins"));
    public static final ResourceKey<StructureSet> RUINS_STRUCTURE_SET = ResourceKey.create(Registries.STRUCTURE_SET, id("ruins"));

    public static final ResourceKey<ConfiguredFeature<?, ?>> RESONANT_CRYSTAL_ORE_SINGLE = ResourceKey.create(Registries.CONFIGURED_FEATURE, id("resonant_crystal_ore_single"));
    public static final ResourceKey<ConfiguredFeature<?, ?>> RESONANT_CRYSTAL_ORE_CLUSTER = ResourceKey.create(Registries.CONFIGURED_FEATURE, id("resonant_crystal_ore_cluster"));

    public static void processorLists(BootstrapContext<StructureProcessorList> context) {
        context.register(RUINS_COURTYARD_PROPS, new StructureProcessorList(List.of(
            new BlockIgnoreProcessor(List.of(Blocks.STRUCTURE_VOID))
        )));
    }

    public static void templatePools(BootstrapContext<StructureTemplatePool> context) {
        HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);
        HolderGetter<StructureProcessorList> processorLists = context.lookup(Registries.PROCESSOR_LIST);

        Holder<StructureTemplatePool> empty = pools.getOrThrow(Pools.EMPTY);
        Holder<StructureProcessorList> courtyardProps = processorLists.getOrThrow(RUINS_COURTYARD_PROPS);

        context.register(RUINS_COURTYARD, new StructureTemplatePool(
            empty,
            List.of(
                Pair.of(StructurePoolElement.single("silentsunken:ruins/courtyard_amethyst", courtyardProps), 1),
                Pair.of(StructurePoolElement.single("silentsunken:ruins/courtyard_copper", courtyardProps), 1),
                Pair.of(StructurePoolElement.single("silentsunken:ruins/courtyard_hay_bale", courtyardProps), 1)
            ),
            StructureTemplatePool.Projection.RIGID
        ));

        context.register(RUINS_WALL, new StructureTemplatePool(
            empty,
            List.of(
                Pair.of(StructurePoolElement.single("silentsunken:ruins/wall_small"), 1),
                Pair.of(StructurePoolElement.single("silentsunken:ruins/wall_big"), 1)
            ),
            StructureTemplatePool.Projection.RIGID
        ));

        context.register(RUINS_TOWER, new StructureTemplatePool(
            empty,
            List.of(
                Pair.of(StructurePoolElement.single("silentsunken:ruins/tower_small"), 1),
                Pair.of(StructurePoolElement.single("silentsunken:ruins/tower_big"), 1)
            ),
            StructureTemplatePool.Projection.RIGID
        ));
    }

    public static void structures(BootstrapContext<Structure> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<StructureTemplatePool> templates = context.lookup(Registries.TEMPLATE_POOL);

        context.register(RUINS_STRUCTURE, new JigsawStructure(
            new Structure.StructureSettings.Builder(biomes.getOrThrow(SilentTags.HAS_STRUCTURE_RUINS))
                .generationStep(GenerationStep.Decoration.SURFACE_STRUCTURES)
                .terrainAdapation(TerrainAdjustment.BEARD_BOX)
                .build(),
            templates.getOrThrow(RUINS_COURTYARD),
            Optional.empty(),
            1,
            ConstantHeight.of(VerticalAnchor.absolute(0)),
            false,
            Optional.of(Heightmap.Types.WORLD_SURFACE_WG),
            new JigsawStructure.MaxDistance(40, 20),
            List.of(),
            JigsawStructure.DEFAULT_DIMENSION_PADDING,
            JigsawStructure.DEFAULT_LIQUID_SETTINGS
        ));
    }

    public static void structureSets(BootstrapContext<StructureSet> context) {
        HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);

        context.register(RUINS_STRUCTURE_SET, new StructureSet(
            List.of(StructureSet.entry(structures.getOrThrow(RUINS_STRUCTURE))),
            new RandomSpreadStructurePlacement(
                Vec3i.ZERO,
                StructurePlacement.FrequencyReductionMethod.DEFAULT,
                0.7F,
                984712,
                Optional.empty(),
                20,
                8,
                RandomSpreadType.LINEAR
            )
        ));
    }

    public static void configuredFeatures(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneOreReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateOreReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        List<OreConfiguration.TargetBlockState> targets = List.of(
            OreConfiguration.target(stoneOreReplaceables, SilentBlocks.RESONANT_CRYSTAL_ORE.get().defaultBlockState()),
            OreConfiguration.target(deepslateOreReplaceables, SilentBlocks.RESONANT_CRYSTAL_ORE.get().defaultBlockState())
        );

        context.register(RESONANT_CRYSTAL_ORE_SINGLE, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(targets, 1, 0.0F)));
        context.register(RESONANT_CRYSTAL_ORE_CLUSTER, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(targets, 2, 0.0F)));
    }

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(SilentSunken.MODID, path);
    }
}
