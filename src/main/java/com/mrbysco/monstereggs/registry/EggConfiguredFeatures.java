package com.mrbysco.monstereggs.registry;

import com.mrbysco.monstereggs.MonsterEggs;
import com.mrbysco.monstereggs.block.MonsterEggBlock;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.SimpleBlockFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class EggConfiguredFeatures {
	public static final ResourceKey<Feature> CAVE_SPIDER_HANGING_EGG = createKey("cave_spider_hanging_egg");
	public static final ResourceKey<Feature> CAVE_SPIDER_EGG = createKey("cave_spider_egg");
	public static final ResourceKey<Feature> CREEPER_HANGING_EGG = createKey("creeper_hanging_egg");
	public static final ResourceKey<Feature> CREEPER_EGG = createKey("creeper_egg");
	public static final ResourceKey<Feature> ENDERMAN_HANGING_EGG = createKey("enderman_hanging_egg");
	public static final ResourceKey<Feature> ENDERMAN_EGG = createKey("enderman_egg");
	public static final ResourceKey<Feature> SKELETON_HANGING_EGG = createKey("skeleton_hanging_egg");
	public static final ResourceKey<Feature> SKELETON_EGG = createKey("skeleton_egg");
	public static final ResourceKey<Feature> SPIDER_HANGING_EGG = createKey("spider_hanging_egg");
	public static final ResourceKey<Feature> SPIDER_EGG = createKey("spider_egg");
	public static final ResourceKey<Feature> ZOMBIE_HANGING_EGG = createKey("zombie_hanging_egg");
	public static final ResourceKey<Feature> ZOMBIE_EGG = createKey("zombie_egg");

	public static ResourceKey<Feature> createKey(String pName) {
		return ResourceKey.create(Registries.FEATURE, Identifier.fromNamespaceAndPath(MonsterEggs.MOD_ID, pName));
	}

	public static void bootstrap(BootstrapContext<Feature> context) {
		context.register(CAVE_SPIDER_HANGING_EGG, getConfiguredEgg(EggRegistry.CAVE_SPIDER_EGG.get(), Direction.UP));
		context.register(CAVE_SPIDER_EGG, getConfiguredEgg(EggRegistry.CAVE_SPIDER_EGG.get(), Direction.DOWN));
		context.register(CREEPER_HANGING_EGG, getConfiguredEgg(EggRegistry.CREEPER_EGG.get(), Direction.UP));
		context.register(CREEPER_EGG, getConfiguredEgg(EggRegistry.CREEPER_EGG.get(), Direction.DOWN));
		context.register(ENDERMAN_HANGING_EGG, getConfiguredEgg(EggRegistry.ENDERMAN_EGG.get(), Direction.UP));
		context.register(ENDERMAN_EGG, getConfiguredEgg(EggRegistry.ENDERMAN_EGG.get(), Direction.DOWN));
		context.register(SKELETON_HANGING_EGG, getConfiguredEgg(EggRegistry.SKELETON_EGG.get(), Direction.UP));
		context.register(SKELETON_EGG, getConfiguredEgg(EggRegistry.SKELETON_EGG.get(), Direction.DOWN));
		context.register(SPIDER_HANGING_EGG, getConfiguredEgg(EggRegistry.SPIDER_EGG.get(), Direction.UP));
		context.register(SPIDER_EGG, getConfiguredEgg(EggRegistry.SPIDER_EGG.get(), Direction.DOWN));
		context.register(ZOMBIE_HANGING_EGG, getConfiguredEgg(EggRegistry.ZOMBIE_EGG.get(), Direction.UP));
		context.register(ZOMBIE_EGG, getConfiguredEgg(EggRegistry.ZOMBIE_EGG.get(), Direction.DOWN));
	}

	public static SimpleBlockFeature getConfiguredEgg(Block block, Direction direction) {
		BlockState state = block.defaultBlockState();

		if (direction == Direction.UP) {
			state = state.setValue(MonsterEggBlock.HANGING, true);
		}
		return new SimpleBlockFeature(BlockStateProvider.of(state));
	}
}
