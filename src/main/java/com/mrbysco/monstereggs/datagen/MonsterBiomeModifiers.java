package com.mrbysco.monstereggs.datagen;

import com.mrbysco.monstereggs.MonsterEggs;
import com.mrbysco.monstereggs.registry.EggPlacedFeatures;
import com.mrbysco.monstereggs.worldgen.modifier.AddFeaturesBlacklistBiomeModifier;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class MonsterBiomeModifiers {

	public static final ResourceKey<BiomeModifier> CAVE_SPIDER_HANGING_EGG = createKey("cave_spider_hanging_egg");
	public static final ResourceKey<BiomeModifier> CAVE_SPIDER_EGG = createKey("cave_spider_egg");
	public static final ResourceKey<BiomeModifier> CREEPER_HANGING_EGG = createKey("creeper_hanging_egg");
	public static final ResourceKey<BiomeModifier> CREEPER_EGG = createKey("creeper_egg");
	public static final ResourceKey<BiomeModifier> ENDERMAN_HANGING_EGG = createKey("enderman_hanging_egg");
	public static final ResourceKey<BiomeModifier> ENDERMAN_EGG = createKey("enderman_egg");
	public static final ResourceKey<BiomeModifier> SKELETON_HANGING_EGG = createKey("skeleton_hanging_egg");
	public static final ResourceKey<BiomeModifier> SKELETON_EGG = createKey("skeleton_egg");
	public static final ResourceKey<BiomeModifier> SPIDER_HANGING_EGG = createKey("spider_hanging_egg");
	public static final ResourceKey<BiomeModifier> SPIDER_EGG = createKey("spider_egg");
	public static final ResourceKey<BiomeModifier> ZOMBIE_HANGING_EGG = createKey("zombie_hanging_egg");
	public static final ResourceKey<BiomeModifier> ZOMBIE_EGG = createKey("zombie_egg");

	public static void bootstrap(BootstapContext<BiomeModifier> context) {
		HolderGetter<Biome> biomeGetter = context.lookup(Registries.BIOME);
		HolderGetter<PlacedFeature> placedGetter = context.lookup(Registries.PLACED_FEATURE);

		List<TagKey<Biome>> overworld = List.of(BiomeTags.IS_OVERWORLD);
		generateBiomeModifier(context, biomeGetter, placedGetter, CAVE_SPIDER_HANGING_EGG, EggPlacedFeatures.CAVE_SPIDER_HANGING_EGG,
				overworld, List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END), GenerationStep.Decoration.UNDERGROUND_DECORATION);
		generateBiomeModifier(context, biomeGetter, placedGetter, CAVE_SPIDER_EGG, EggPlacedFeatures.CAVE_SPIDER_EGG,
				overworld, List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END), GenerationStep.Decoration.UNDERGROUND_DECORATION);
		generateBiomeModifier(context, biomeGetter, placedGetter, CREEPER_HANGING_EGG, EggPlacedFeatures.CREEPER_HANGING_EGG,
				overworld, List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END), GenerationStep.Decoration.UNDERGROUND_DECORATION);
		generateBiomeModifier(context, biomeGetter, placedGetter, CREEPER_EGG, EggPlacedFeatures.CREEPER_EGG,
				overworld, List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END), GenerationStep.Decoration.UNDERGROUND_DECORATION);
		generateBiomeModifier(context, biomeGetter, placedGetter, ENDERMAN_HANGING_EGG, EggPlacedFeatures.ENDERMAN_HANGING_EGG,
				overworld, List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END), GenerationStep.Decoration.UNDERGROUND_DECORATION);
		generateBiomeModifier(context, biomeGetter, placedGetter, ENDERMAN_EGG, EggPlacedFeatures.ENDERMAN_EGG,
				overworld, List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END), GenerationStep.Decoration.UNDERGROUND_DECORATION);
		generateBiomeModifier(context, biomeGetter, placedGetter, SKELETON_HANGING_EGG, EggPlacedFeatures.SKELETON_HANGING_EGG,
				overworld, List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END), GenerationStep.Decoration.UNDERGROUND_DECORATION);
		generateBiomeModifier(context, biomeGetter, placedGetter, SKELETON_EGG, EggPlacedFeatures.SKELETON_EGG,
				overworld, List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END), GenerationStep.Decoration.UNDERGROUND_DECORATION);
		generateBiomeModifier(context, biomeGetter, placedGetter, SPIDER_HANGING_EGG, EggPlacedFeatures.SPIDER_HANGING_EGG,
				overworld, List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END), GenerationStep.Decoration.UNDERGROUND_DECORATION);
		generateBiomeModifier(context, biomeGetter, placedGetter, SPIDER_EGG, EggPlacedFeatures.SPIDER_EGG,
				overworld, List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END), GenerationStep.Decoration.UNDERGROUND_DECORATION);
		generateBiomeModifier(context, biomeGetter, placedGetter, ZOMBIE_HANGING_EGG, EggPlacedFeatures.ZOMBIE_HANGING_EGG,
				overworld, List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END), GenerationStep.Decoration.UNDERGROUND_DECORATION);
		generateBiomeModifier(context, biomeGetter, placedGetter, ZOMBIE_EGG, EggPlacedFeatures.ZOMBIE_EGG,
				overworld, List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END), GenerationStep.Decoration.UNDERGROUND_DECORATION);
	}


	private static void generateBiomeModifier(BootstapContext<BiomeModifier> context,
											  HolderGetter<Biome> biomeGetter,
											  HolderGetter<PlacedFeature> placedGetter,
											  ResourceKey<BiomeModifier> modifierKey,
											  ResourceKey<PlacedFeature> placedKey,
											  @NotNull List<TagKey<Biome>> tags,
											  @Nullable List<TagKey<Biome>> blacklistTags,
											  GenerationStep.Decoration decorationType) {
		final List<HolderSet<Biome>> tagHolders = tags.stream()
				.map(tag -> biomeGetter.getOrThrow(tag)).collect(Collectors.toList());
		final List<HolderSet<Biome>> blacklistTagHolders = blacklistTags.isEmpty() ? List.of() : blacklistTags.stream()
				.map(tag -> biomeGetter.getOrThrow(tag)).collect(Collectors.toList());
		final BiomeModifier addFeature = new AddFeaturesBlacklistBiomeModifier(
				tagHolders,
				blacklistTagHolders,
				HolderSet.direct(placedGetter.getOrThrow(placedKey)),
				decorationType);
		context.register(modifierKey, addFeature);
	}

	private static ResourceKey<BiomeModifier> createKey(String path) {
		return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(MonsterEggs.MOD_ID, path));
	}
}
