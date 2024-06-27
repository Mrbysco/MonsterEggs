package com.mrbysco.monstereggs.registry;

import com.mrbysco.monstereggs.MonsterEggs;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.placement.SurfaceRelativeThresholdFilter;

import java.util.List;

public class EggPlacedFeatures {
	public static final ResourceKey<PlacedFeature> CAVE_SPIDER_HANGING_EGG = createKey("cave_spider_hanging_egg");
	public static final ResourceKey<PlacedFeature> CAVE_SPIDER_EGG = createKey("cave_spider_egg");
	public static final ResourceKey<PlacedFeature> CREEPER_HANGING_EGG = createKey("creeper_hanging_egg");
	public static final ResourceKey<PlacedFeature> CREEPER_EGG = createKey("creeper_egg");
	public static final ResourceKey<PlacedFeature> ENDERMAN_HANGING_EGG = createKey("enderman_hanging_egg");
	public static final ResourceKey<PlacedFeature> ENDERMAN_EGG = createKey("enderman_egg");
	public static final ResourceKey<PlacedFeature> SKELETON_HANGING_EGG = createKey("skeleton_hanging_egg");
	public static final ResourceKey<PlacedFeature> SKELETON_EGG = createKey("skeleton_egg");
	public static final ResourceKey<PlacedFeature> SPIDER_HANGING_EGG = createKey("spider_hanging_egg");
	public static final ResourceKey<PlacedFeature> SPIDER_EGG = createKey("spider_egg");
	public static final ResourceKey<PlacedFeature> ZOMBIE_HANGING_EGG = createKey("zombie_hanging_egg");
	public static final ResourceKey<PlacedFeature> ZOMBIE_EGG = createKey("zombie_egg");
	
	public static ResourceKey<PlacedFeature> createKey(String pName) {
		return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(MonsterEggs.MOD_ID, pName));
	}
	
	public static void bootstrap(BootstrapContext<PlacedFeature> context) {
		HolderGetter<ConfiguredFeature<?, ?>> holdergetter = context.lookup(Registries.CONFIGURED_FEATURE);

		PlacementUtils.register(context, CAVE_SPIDER_HANGING_EGG, holdergetter.getOrThrow(EggConfiguredFeatures.CAVE_SPIDER_HANGING_EGG), getEggModifiers(Direction.UP, 1, 4));
		PlacementUtils.register(context, CAVE_SPIDER_EGG, holdergetter.getOrThrow(EggConfiguredFeatures.CAVE_SPIDER_EGG), getEggModifiers(Direction.DOWN, 1, 4));
		PlacementUtils.register(context, CREEPER_HANGING_EGG, holdergetter.getOrThrow(EggConfiguredFeatures.CREEPER_HANGING_EGG), getEggModifiers(Direction.UP, 1, 4));
		PlacementUtils.register(context, CREEPER_EGG, holdergetter.getOrThrow(EggConfiguredFeatures.CREEPER_EGG), getEggModifiers(Direction.DOWN, 1, 4));
		PlacementUtils.register(context, ENDERMAN_HANGING_EGG, holdergetter.getOrThrow(EggConfiguredFeatures.ENDERMAN_HANGING_EGG), getEggModifiers(Direction.UP, 1, 4));
		PlacementUtils.register(context, ENDERMAN_EGG, holdergetter.getOrThrow(EggConfiguredFeatures.ENDERMAN_EGG), getEggModifiers(Direction.DOWN, 1, 4));
		PlacementUtils.register(context, SKELETON_HANGING_EGG, holdergetter.getOrThrow(EggConfiguredFeatures.SKELETON_HANGING_EGG), getEggModifiers(Direction.UP, 1, 4));
		PlacementUtils.register(context, SKELETON_EGG, holdergetter.getOrThrow(EggConfiguredFeatures.SKELETON_EGG), getEggModifiers(Direction.DOWN, 1, 4));
		PlacementUtils.register(context, SPIDER_HANGING_EGG, holdergetter.getOrThrow(EggConfiguredFeatures.SPIDER_HANGING_EGG), getEggModifiers(Direction.UP, 1, 4));
		PlacementUtils.register(context, SPIDER_EGG, holdergetter.getOrThrow(EggConfiguredFeatures.SPIDER_EGG), getEggModifiers(Direction.DOWN, 1, 4));
		PlacementUtils.register(context, ZOMBIE_HANGING_EGG, holdergetter.getOrThrow(EggConfiguredFeatures.ZOMBIE_HANGING_EGG), getEggModifiers(Direction.UP, 1, 4));
		PlacementUtils.register(context, ZOMBIE_EGG, holdergetter.getOrThrow(EggConfiguredFeatures.ZOMBIE_EGG), getEggModifiers(Direction.DOWN, 1, 4));
	}

	private static List<PlacementModifier> getEggModifiers(Direction direction, int rarity, int count) {
		EnvironmentScanPlacement environmentScanPlacement = EnvironmentScanPlacement.scanningFor(direction, BlockPredicate.hasSturdyFace(direction.getOpposite()),
				BlockPredicate.ONLY_IN_AIR_PREDICATE, 32);
		return List.of(RarityFilter.onAverageOnceEvery(rarity),
				CountPlacement.of(UniformInt.of(0, count)), InSquarePlacement.spread(), PlacementUtils.FULL_RANGE,
				environmentScanPlacement, SurfaceRelativeThresholdFilter.of(Heightmap.Types.OCEAN_FLOOR_WG, Integer.MIN_VALUE, -13),
				RandomOffsetPlacement.of(ConstantInt.of(-1), ConstantInt.of(-1)), BiomeFilter.biome());
	}
}
