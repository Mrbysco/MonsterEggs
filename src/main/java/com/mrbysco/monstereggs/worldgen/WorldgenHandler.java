package com.mrbysco.monstereggs.worldgen;

import com.mrbysco.monstereggs.config.EggConfig;
import com.mrbysco.monstereggs.worldgen.placement.EggPlacements;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.BiomeCategory;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WorldgenHandler {

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void biomeLoadingEvent(BiomeLoadingEvent event) {
		BiomeGenerationSettingsBuilder builder = event.getGeneration();
		Biome.BiomeCategory category = event.getCategory();
		ResourceKey<Biome> biomeKey = ResourceKey.create(Registry.BIOME_REGISTRY, event.getName());

		if (category != BiomeCategory.THEEND && category != BiomeCategory.NETHER) {
			if (BiomeDictionary.hasType(biomeKey, Type.OVERWORLD) && !BiomeDictionary.hasType(biomeKey, Type.MUSHROOM)) {
				if (EggConfig.COMMON.generateCaveSpiderEggs.get()) {
					builder.addFeature(Decoration.UNDERGROUND_DECORATION, EggPlacements.CAVE_SPIDER_EGG);
				}
				if (EggConfig.COMMON.generateHangingCaveSpiderEggs.get()) {
					builder.addFeature(Decoration.UNDERGROUND_DECORATION, EggPlacements.CAVE_SPIDER_HANGING_EGG);
				}

				if (EggConfig.COMMON.generateCreeperEggs.get()) {
					builder.addFeature(Decoration.UNDERGROUND_DECORATION, EggPlacements.CREEPER_EGG);
				}
				if (EggConfig.COMMON.generateHangingCreeperEggs.get()) {
					builder.addFeature(Decoration.UNDERGROUND_DECORATION, EggPlacements.CREEPER_HANGING_EGG);
				}

				if (EggConfig.COMMON.generateEndermanEggs.get()) {
					builder.addFeature(Decoration.UNDERGROUND_DECORATION, EggPlacements.ENDERMAN_EGG);
				}
				if (EggConfig.COMMON.generateHangingEndermanEggs.get()) {
					builder.addFeature(Decoration.UNDERGROUND_DECORATION, EggPlacements.ENDERMAN_HANGING_EGG);
				}

				if (EggConfig.COMMON.generateSkeletonEggs.get()) {
					builder.addFeature(Decoration.UNDERGROUND_DECORATION, EggPlacements.SKELETON_EGG);
				}
				if (EggConfig.COMMON.generateHangingSkeletonEggs.get()) {
					builder.addFeature(Decoration.UNDERGROUND_DECORATION, EggPlacements.SKELETON_HANGING_EGG);
				}

				if (EggConfig.COMMON.generateSpiderEggs.get()) {
					builder.addFeature(Decoration.UNDERGROUND_DECORATION, EggPlacements.SPIDER_EGG);
				}
				if (EggConfig.COMMON.generateHangingSpiderEggs.get()) {
					builder.addFeature(Decoration.UNDERGROUND_DECORATION, EggPlacements.SPIDER_HANGING_EGG);
				}

				if (EggConfig.COMMON.generateZombieEggs.get()) {
					builder.addFeature(Decoration.UNDERGROUND_DECORATION, EggPlacements.ZOMBIE_EGG);
				}
				if (EggConfig.COMMON.generateHangingZombieEggs.get()) {
					builder.addFeature(Decoration.UNDERGROUND_DECORATION, EggPlacements.ZOMBIE_HANGING_EGG);
				}
			}
		}
	}
}
