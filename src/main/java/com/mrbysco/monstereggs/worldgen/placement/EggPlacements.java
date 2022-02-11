package com.mrbysco.monstereggs.worldgen.placement;

import com.mrbysco.monstereggs.block.MonsterEggBlock;
import com.mrbysco.monstereggs.config.EggConfig;
import com.mrbysco.monstereggs.registry.EggRegistry;
import net.minecraft.core.Direction;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.placement.SurfaceRelativeThresholdFilter;

import java.util.List;
import java.util.function.Supplier;

public class EggPlacements {
	public static void initialize() {

	}

	public static final PlacedFeature CAVE_SPIDER_HANGING_EGG = makeEggFeature("cave_spider_hanging_egg", 
			EggRegistry.CAVE_SPIDER_EGG.get(), 
			EggConfig.COMMON.caveSpiderEggsTries::get, EggConfig.COMMON.caveSpiderEggsRarity::get, EggConfig.COMMON.caveSpiderEggsCount::get, Direction.UP);
	public static final PlacedFeature CAVE_SPIDER_EGG = makeEggFeature("cave_spider_egg", 
			EggRegistry.CAVE_SPIDER_EGG.get(),
			EggConfig.COMMON.caveSpiderEggsTries::get, EggConfig.COMMON.caveSpiderEggsRarity::get, EggConfig.COMMON.caveSpiderEggsCount::get, Direction.DOWN);
	
	public static final PlacedFeature CREEPER_HANGING_EGG = makeEggFeature("creeper_hanging_egg", 
			EggRegistry.CREEPER_EGG.get(), 
			EggConfig.COMMON.creeperEggsTries::get, EggConfig.COMMON.creeperEggsRarity::get, EggConfig.COMMON.creeperEggsCount::get, Direction.UP);
	public static final PlacedFeature CREEPER_EGG = makeEggFeature("creeper_egg", 
			EggRegistry.CREEPER_EGG.get(), 
			EggConfig.COMMON.creeperEggsTries::get, EggConfig.COMMON.creeperEggsRarity::get, EggConfig.COMMON.creeperEggsCount::get, Direction.DOWN);
	
	public static final PlacedFeature ENDERMAN_HANGING_EGG = makeEggFeature("enderman_hanging_egg", 
			EggRegistry.ENDERMAN_EGG.get(), 
			EggConfig.COMMON.endermanEggsTries::get, EggConfig.COMMON.endermanEggsRarity::get, EggConfig.COMMON.endermanEggsCount::get, Direction.UP);
	public static final PlacedFeature ENDERMAN_EGG = makeEggFeature("enderman_egg", 
			EggRegistry.ENDERMAN_EGG.get(), 
			EggConfig.COMMON.endermanEggsTries::get, EggConfig.COMMON.endermanEggsRarity::get, EggConfig.COMMON.endermanEggsCount::get, Direction.DOWN);
	
	public static final PlacedFeature SKELETON_HANGING_EGG = makeEggFeature("skeleton_hanging_egg", 
			EggRegistry.SKELETON_EGG.get(), 
			EggConfig.COMMON.skeletonEggsTries::get, EggConfig.COMMON.skeletonEggsRarity::get, EggConfig.COMMON.skeletonEggsCount::get, Direction.UP);
	public static final PlacedFeature SKELETON_EGG = makeEggFeature("skeleton_egg", 
			EggRegistry.SKELETON_EGG.get(), 
			EggConfig.COMMON.skeletonEggsTries::get, EggConfig.COMMON.skeletonEggsRarity::get, EggConfig.COMMON.skeletonEggsCount::get, Direction.DOWN);
	
	public static final PlacedFeature SPIDER_HANGING_EGG = makeEggFeature("spider_hanging_egg", 
			EggRegistry.SPIDER_EGG.get(), 
			EggConfig.COMMON.spiderEggsTries::get, EggConfig.COMMON.spiderEggsRarity::get, EggConfig.COMMON.spiderEggsCount::get, Direction.UP);
	public static final PlacedFeature SPIDER_EGG = makeEggFeature("spider_egg", 
			EggRegistry.SPIDER_EGG.get(), 
			EggConfig.COMMON.spiderEggsTries::get, EggConfig.COMMON.spiderEggsRarity::get, EggConfig.COMMON.spiderEggsCount::get, Direction.DOWN);
	
	public static final PlacedFeature ZOMBIE_HANGING_EGG = makeEggFeature("zombie_hanging_egg", 
			EggRegistry.ZOMBIE_EGG.get(), 
			EggConfig.COMMON.zombieEggsTries::get, EggConfig.COMMON.zombieEggsRarity::get, EggConfig.COMMON.zombieEggsCount::get, Direction.UP);
	public static final PlacedFeature ZOMBIE_EGG = makeEggFeature("zombie_egg", 
			EggRegistry.ZOMBIE_EGG.get(), 
			EggConfig.COMMON.zombieEggsTries::get, EggConfig.COMMON.zombieEggsRarity::get, EggConfig.COMMON.zombieEggsCount::get, Direction.DOWN);

	public static final PlacedFeature makeEggFeature(String name, Block block, Supplier<Integer> tries, Supplier<Integer> rarity, Supplier<Integer> count, Direction direction) {
		BlockState state = block.defaultBlockState();
		if(direction == Direction.UP) {
			state = state.setValue(MonsterEggBlock.HANGING, true);
		}
		BlockStateProvider provider = BlockStateProvider.simple(state);
		EnvironmentScanPlacement environmentScanPlacement = EnvironmentScanPlacement.scanningFor(direction, BlockPredicate.hasSturdyFace(direction.getOpposite()),
				BlockPredicate.ONLY_IN_AIR_PREDICATE, 32;
		return PlacementUtils.register(name, Feature.RANDOM_PATCH.configured(
						FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK.configured(
								new SimpleBlockConfiguration(provider)), List.of(), tries.get()))
				.placed(RarityFilter.onAverageOnceEvery(rarity.get()), CountPlacement.of(UniformInt.of(0, count.get())), InSquarePlacement.spread(), PlacementUtils.FULL_RANGE,
						environmentScanPlacement, SurfaceRelativeThresholdFilter.of(Heightmap.Types.OCEAN_FLOOR_WG, Integer.MIN_VALUE, -13),
						RandomOffsetPlacement.of(ConstantInt.of(-1), ConstantInt.of(-1)), BiomeFilter.biome()));
	}
}
