package com.mrbysco.monstereggs.registry;

import com.mrbysco.monstereggs.MonsterEggs;
import com.mrbysco.monstereggs.block.MonsterEggBlock;
import net.minecraft.core.Direction;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.List;

public class EggConfiguredFeatures {
	public static final ResourceKey<ConfiguredFeature<?, ?>> CAVE_SPIDER_HANGING_EGG = FeatureUtils.createKey(new ResourceLocation(MonsterEggs.MOD_ID, "cave_spider_hanging_egg").toString());
	public static final ResourceKey<ConfiguredFeature<?, ?>> CAVE_SPIDER_EGG = FeatureUtils.createKey(new ResourceLocation(MonsterEggs.MOD_ID, "cave_spider_egg").toString());
	public static final ResourceKey<ConfiguredFeature<?, ?>> CREEPER_HANGING_EGG = FeatureUtils.createKey(new ResourceLocation(MonsterEggs.MOD_ID, "creeper_hanging_egg").toString());
	public static final ResourceKey<ConfiguredFeature<?, ?>> CREEPER_EGG = FeatureUtils.createKey(new ResourceLocation(MonsterEggs.MOD_ID, "creeper_egg").toString());
	public static final ResourceKey<ConfiguredFeature<?, ?>> ENDERMAN_HANGING_EGG = FeatureUtils.createKey(new ResourceLocation(MonsterEggs.MOD_ID, "enderman_hanging_egg").toString());
	public static final ResourceKey<ConfiguredFeature<?, ?>> ENDERMAN_EGG = FeatureUtils.createKey(new ResourceLocation(MonsterEggs.MOD_ID, "enderman_egg").toString());
	public static final ResourceKey<ConfiguredFeature<?, ?>> SKELETON_HANGING_EGG = FeatureUtils.createKey(new ResourceLocation(MonsterEggs.MOD_ID, "skeleton_hanging_egg").toString());
	public static final ResourceKey<ConfiguredFeature<?, ?>> SKELETON_EGG = FeatureUtils.createKey(new ResourceLocation(MonsterEggs.MOD_ID, "skeleton_egg").toString());
	public static final ResourceKey<ConfiguredFeature<?, ?>> SPIDER_HANGING_EGG = FeatureUtils.createKey(new ResourceLocation(MonsterEggs.MOD_ID, "spider_hanging_egg").toString());
	public static final ResourceKey<ConfiguredFeature<?, ?>> SPIDER_EGG = FeatureUtils.createKey(new ResourceLocation(MonsterEggs.MOD_ID, "spider_egg").toString());
	public static final ResourceKey<ConfiguredFeature<?, ?>> ZOMBIE_HANGING_EGG = FeatureUtils.createKey(new ResourceLocation(MonsterEggs.MOD_ID, "zombie_hanging_egg").toString());
	public static final ResourceKey<ConfiguredFeature<?, ?>> ZOMBIE_EGG = FeatureUtils.createKey(new ResourceLocation(MonsterEggs.MOD_ID, "zombie_egg").toString());

	public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
		FeatureUtils.register(context, CAVE_SPIDER_HANGING_EGG, Feature.RANDOM_PATCH, getConfiguredEgg(EggRegistry.CAVE_SPIDER_EGG.get(), Direction.UP, 24));
		FeatureUtils.register(context, CAVE_SPIDER_EGG, Feature.RANDOM_PATCH, getConfiguredEgg(EggRegistry.CAVE_SPIDER_EGG.get(), Direction.DOWN, 24));
		FeatureUtils.register(context, CREEPER_HANGING_EGG, Feature.RANDOM_PATCH, getConfiguredEgg(EggRegistry.CREEPER_EGG.get(), Direction.UP, 24));
		FeatureUtils.register(context, CREEPER_EGG, Feature.RANDOM_PATCH, getConfiguredEgg(EggRegistry.CREEPER_EGG.get(), Direction.DOWN, 24));
		FeatureUtils.register(context, ENDERMAN_HANGING_EGG, Feature.RANDOM_PATCH, getConfiguredEgg(EggRegistry.ENDERMAN_EGG.get(), Direction.UP, 24));
		FeatureUtils.register(context, ENDERMAN_EGG, Feature.RANDOM_PATCH, getConfiguredEgg(EggRegistry.ENDERMAN_EGG.get(), Direction.DOWN, 24));
		FeatureUtils.register(context, SKELETON_HANGING_EGG, Feature.RANDOM_PATCH, getConfiguredEgg(EggRegistry.SKELETON_EGG.get(), Direction.UP, 24));
		FeatureUtils.register(context, SKELETON_EGG, Feature.RANDOM_PATCH, getConfiguredEgg(EggRegistry.SKELETON_EGG.get(), Direction.DOWN, 24));
		FeatureUtils.register(context, SPIDER_HANGING_EGG, Feature.RANDOM_PATCH, getConfiguredEgg(EggRegistry.SPIDER_EGG.get(), Direction.UP, 24));
		FeatureUtils.register(context, SPIDER_EGG, Feature.RANDOM_PATCH, getConfiguredEgg(EggRegistry.SPIDER_EGG.get(), Direction.DOWN, 24));
		FeatureUtils.register(context, ZOMBIE_HANGING_EGG, Feature.RANDOM_PATCH, getConfiguredEgg(EggRegistry.ZOMBIE_EGG.get(), Direction.UP, 24));
		FeatureUtils.register(context, ZOMBIE_EGG, Feature.RANDOM_PATCH, getConfiguredEgg(EggRegistry.ZOMBIE_EGG.get(), Direction.DOWN, 24));
	}

	public static RandomPatchConfiguration getConfiguredEgg(Block block, Direction direction, int tries) {
		BlockState state = block.defaultBlockState();
		if (direction == Direction.UP) {
			state = state.setValue(MonsterEggBlock.HANGING, true);
		}
		BlockStateProvider provider = BlockStateProvider.simple(state);
		return FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(provider), List.of(), tries);
	}
}
