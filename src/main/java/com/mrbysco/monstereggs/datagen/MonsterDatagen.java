package com.mrbysco.monstereggs.datagen;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.mrbysco.monstereggs.MonsterEggs;
import com.mrbysco.monstereggs.registry.EggConfiguredFeatures;
import com.mrbysco.monstereggs.registry.EggPlacedFeatures;
import com.mrbysco.monstereggs.registry.EggRegistry;
import com.mrbysco.monstereggs.worldgen.modifier.AddFeaturesBlacklistBiomeModifier;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MonsterDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		HolderLookup.Provider provider = getProvider();
		final RegistryOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, provider);
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(event.includeServer(), new Loots(packOutput));

			generator.addProvider(event.includeServer(), JsonCodecProvider.forDatapackRegistry(
					packOutput, helper, MonsterEggs.MOD_ID, ops, Registries.CONFIGURED_FEATURE, getConfiguredFeatures(provider)));

			generator.addProvider(event.includeServer(), JsonCodecProvider.forDatapackRegistry(
					packOutput, helper, MonsterEggs.MOD_ID, ops, Registries.PLACED_FEATURE, getPlacedFeatures(provider)));

			generator.addProvider(event.includeServer(), JsonCodecProvider.forDatapackRegistry(
					packOutput, helper, MonsterEggs.MOD_ID, ops, ForgeRegistries.Keys.BIOME_MODIFIERS, getBiomeModifiers(provider, ops)));
		}
		if (event.includeClient()) {
			generator.addProvider(event.includeClient(), new Language(packOutput));
			generator.addProvider(event.includeClient(), new MonsterSoundProvider(packOutput, helper));
			generator.addProvider(event.includeClient(), new BlockModels(packOutput, helper));
			generator.addProvider(event.includeClient(), new ItemModels(packOutput, helper));
			generator.addProvider(event.includeClient(), new BlockStates(packOutput, helper));
		}
	}

	private static HolderLookup.Provider getProvider() {
		final RegistrySetBuilder registryBuilder = new RegistrySetBuilder();
		registryBuilder.add(Registries.CONFIGURED_FEATURE, EggConfiguredFeatures::bootstrap);
		registryBuilder.add(Registries.PLACED_FEATURE, EggPlacedFeatures::bootstrap);
		// We need the BIOME registry to be present so we can use a biome tag, doesn't matter that it's empty
		registryBuilder.add(Registries.BIOME, context -> {
		});
		RegistryAccess.Frozen regAccess = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
		return registryBuilder.buildPatch(regAccess, VanillaRegistries.createLookup());
	}

	public static Map<ResourceLocation, ConfiguredFeature<?, ?>> getConfiguredFeatures(HolderLookup.Provider provider) {

		Map<ResourceLocation, ConfiguredFeature<?, ?>> map = Maps.newHashMap();
		fillConfiguredMap(map, provider, EggConfiguredFeatures.CAVE_SPIDER_HANGING_EGG);
		fillConfiguredMap(map, provider, EggConfiguredFeatures.CAVE_SPIDER_EGG);
		fillConfiguredMap(map, provider, EggConfiguredFeatures.CREEPER_HANGING_EGG);
		fillConfiguredMap(map, provider, EggConfiguredFeatures.CREEPER_EGG);
		fillConfiguredMap(map, provider, EggConfiguredFeatures.ENDERMAN_HANGING_EGG);
		fillConfiguredMap(map, provider, EggConfiguredFeatures.ENDERMAN_EGG);
		fillConfiguredMap(map, provider, EggConfiguredFeatures.SKELETON_HANGING_EGG);
		fillConfiguredMap(map, provider, EggConfiguredFeatures.SKELETON_EGG);
		fillConfiguredMap(map, provider, EggConfiguredFeatures.SPIDER_HANGING_EGG);
		fillConfiguredMap(map, provider, EggConfiguredFeatures.SPIDER_EGG);
		fillConfiguredMap(map, provider, EggConfiguredFeatures.ZOMBIE_HANGING_EGG);
		fillConfiguredMap(map, provider, EggConfiguredFeatures.ZOMBIE_EGG);

		return map;
	}

	public static void fillConfiguredMap(Map<ResourceLocation, ConfiguredFeature<?, ?>> map, HolderLookup.Provider provider, ResourceKey<ConfiguredFeature<?, ?>> featureKey) {
		final HolderLookup.RegistryLookup<ConfiguredFeature<?, ?>> configuredReg = provider.lookupOrThrow(Registries.CONFIGURED_FEATURE);
		map.put(featureKey.location(), configuredReg.getOrThrow(featureKey).value());
	}

	public static Map<ResourceLocation, PlacedFeature> getPlacedFeatures(HolderLookup.Provider provider) {
		Map<ResourceLocation, PlacedFeature> map = Maps.newHashMap();

		fillPlacedMap(map, provider, EggPlacedFeatures.CAVE_SPIDER_HANGING_EGG);
		fillPlacedMap(map, provider, EggPlacedFeatures.CAVE_SPIDER_EGG);
		fillPlacedMap(map, provider, EggPlacedFeatures.CREEPER_HANGING_EGG);
		fillPlacedMap(map, provider, EggPlacedFeatures.CREEPER_EGG);
		fillPlacedMap(map, provider, EggPlacedFeatures.ENDERMAN_HANGING_EGG);
		fillPlacedMap(map, provider, EggPlacedFeatures.ENDERMAN_EGG);
		fillPlacedMap(map, provider, EggPlacedFeatures.SKELETON_HANGING_EGG);
		fillPlacedMap(map, provider, EggPlacedFeatures.SKELETON_EGG);
		fillPlacedMap(map, provider, EggPlacedFeatures.SPIDER_HANGING_EGG);
		fillPlacedMap(map, provider, EggPlacedFeatures.SPIDER_EGG);
		fillPlacedMap(map, provider, EggPlacedFeatures.ZOMBIE_HANGING_EGG);
		fillPlacedMap(map, provider, EggPlacedFeatures.ZOMBIE_EGG);

		return map;
	}

	public static void fillPlacedMap(Map<ResourceLocation, PlacedFeature> map, HolderLookup.Provider provider, ResourceKey<PlacedFeature> featureKey) {
		final HolderLookup.RegistryLookup<PlacedFeature> configuredReg = provider.lookupOrThrow(Registries.PLACED_FEATURE);
		map.put(featureKey.location(), configuredReg.getOrThrow(featureKey).value());
	}

	public static Map<ResourceLocation, BiomeModifier> getBiomeModifiers(HolderLookup.Provider provider, RegistryOps<JsonElement> ops) {
		Map<ResourceLocation, BiomeModifier> map = Maps.newHashMap();

		List<TagKey<Biome>> overworld = List.of(BiomeTags.IS_OVERWORLD);
		map.putAll(generateBiomeModifier(provider, ops, new ResourceLocation(MonsterEggs.MOD_ID, "cave_spider_hanging_egg"),
				overworld, List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END), Decoration.UNDERGROUND_DECORATION));
		map.putAll(generateBiomeModifier(provider, ops, new ResourceLocation(MonsterEggs.MOD_ID, "cave_spider_egg"),
				overworld, List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END), Decoration.UNDERGROUND_DECORATION));
		map.putAll(generateBiomeModifier(provider, ops, new ResourceLocation(MonsterEggs.MOD_ID, "creeper_hanging_egg"),
				overworld, List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END), Decoration.UNDERGROUND_DECORATION));
		map.putAll(generateBiomeModifier(provider, ops, new ResourceLocation(MonsterEggs.MOD_ID, "creeper_egg"),
				overworld, List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END), Decoration.UNDERGROUND_DECORATION));
		map.putAll(generateBiomeModifier(provider, ops, new ResourceLocation(MonsterEggs.MOD_ID, "enderman_hanging_egg"),
				overworld, List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END), Decoration.UNDERGROUND_DECORATION));
		map.putAll(generateBiomeModifier(provider, ops, new ResourceLocation(MonsterEggs.MOD_ID, "enderman_egg"),
				overworld, List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END), Decoration.UNDERGROUND_DECORATION));
		map.putAll(generateBiomeModifier(provider, ops, new ResourceLocation(MonsterEggs.MOD_ID, "skeleton_hanging_egg"),
				overworld, List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END), Decoration.UNDERGROUND_DECORATION));
		map.putAll(generateBiomeModifier(provider, ops, new ResourceLocation(MonsterEggs.MOD_ID, "skeleton_egg"),
				overworld, List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END), Decoration.UNDERGROUND_DECORATION));
		map.putAll(generateBiomeModifier(provider, ops, new ResourceLocation(MonsterEggs.MOD_ID, "spider_hanging_egg"),
				overworld, List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END), Decoration.UNDERGROUND_DECORATION));
		map.putAll(generateBiomeModifier(provider, ops, new ResourceLocation(MonsterEggs.MOD_ID, "spider_egg"),
				overworld, List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END), Decoration.UNDERGROUND_DECORATION));
		map.putAll(generateBiomeModifier(provider, ops, new ResourceLocation(MonsterEggs.MOD_ID, "zombie_hanging_egg"),
				overworld, List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END), Decoration.UNDERGROUND_DECORATION));
		map.putAll(generateBiomeModifier(provider, ops, new ResourceLocation(MonsterEggs.MOD_ID, "zombie_egg"),
				overworld, List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END), Decoration.UNDERGROUND_DECORATION));

		return map;
	}


	private static Map<ResourceLocation, BiomeModifier> generateBiomeModifier(HolderLookup.Provider provider, RegistryOps<JsonElement> ops, ResourceLocation location,
																			  @NotNull List<TagKey<Biome>> tags, @Nullable List<TagKey<Biome>> blacklistTags, Decoration decorationType) {
		final HolderLookup.RegistryLookup<Biome> biomeReg = provider.lookupOrThrow(Registries.BIOME);
		final HolderLookup.RegistryLookup<PlacedFeature> placedReg = provider.lookupOrThrow(Registries.PLACED_FEATURE);

		final List<HolderSet<Biome>> tagHolders = tags.stream()
				.map(tag -> HolderSet.emptyNamed(biomeReg, tag)).collect(Collectors.toList());
		final List<HolderSet<Biome>> blacklistTagHolders = blacklistTags.isEmpty() ? List.of() : blacklistTags.stream()
				.map(tag -> HolderSet.emptyNamed(biomeReg, tag)).collect(Collectors.toList());
		final BiomeModifier addFeature = new AddFeaturesBlacklistBiomeModifier(
				tagHolders,
				blacklistTagHolders,
				HolderSet.direct(placedReg.get(ResourceKey.create(Registries.PLACED_FEATURE, location)).orElseThrow()),
				decorationType);
		return Map.of(location, addFeature);
	}

	private static class Loots extends LootTableProvider {
		public Loots(PackOutput packOutput) {
			super(packOutput, Set.of(),
					List.of(new SubProviderEntry(MonsterBlockTables::new, LootContextParamSets.BLOCK))
			);
		}

		public static class MonsterBlockTables extends BlockLootSubProvider {

			protected MonsterBlockTables() {
				super(Set.of(), FeatureFlags.REGISTRY.allFlags());
			}

			@Override
			protected void generate() {
				this.add(EggRegistry.CAVE_SPIDER_EGG.get(), noDrop());
				this.add(EggRegistry.CREEPER_EGG.get(), noDrop());
				this.add(EggRegistry.ENDERMAN_EGG.get(), noDrop());
				this.add(EggRegistry.SKELETON_EGG.get(), noDrop());
				this.add(EggRegistry.SPIDER_EGG.get(), noDrop());
				this.add(EggRegistry.ZOMBIE_EGG.get(), noDrop());
			}

			@Override
			protected Iterable<Block> getKnownBlocks() {
				return (Iterable<Block>) EggRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
			}
		}

		@Override
		protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationContext) {
			map.forEach((name, table) -> LootTables.validate(validationContext, name, table));
		}
	}

	private static class Language extends LanguageProvider {
		public Language(PackOutput packOutput) {
			super(packOutput, MonsterEggs.MOD_ID, "en_us");
		}

		@Override
		protected void addTranslations() {
			add("itemGroup.monstereggs", "Monster Eggs");

			addSubtitle(EggRegistry.MONSTER_EGG_BROKEN, "Monster Egg Hatching");

			addBlock(EggRegistry.CAVE_SPIDER_EGG, "Mysterious Shell");
			addBlock(EggRegistry.CREEPER_EGG, "Mysterious Shell");
			addBlock(EggRegistry.ENDERMAN_EGG, "Mysterious Shell");
			addBlock(EggRegistry.SKELETON_EGG, "Mysterious Shell");
			addBlock(EggRegistry.SPIDER_EGG, "Mysterious Shell");
			addBlock(EggRegistry.ZOMBIE_EGG, "Mysterious Shell");
		}

		public void addSubtitle(RegistryObject<SoundEvent> sound, String name) {
			String path = MonsterEggs.MOD_ID + ".subtitle." + sound.getId().getPath();
			this.add(path, name);
		}
	}

	public static class MonsterSoundProvider extends SoundDefinitionsProvider {
		public MonsterSoundProvider(PackOutput packOutput, ExistingFileHelper helper) {
			super(packOutput, MonsterEggs.MOD_ID, helper);
		}

		@Override
		public void registerSounds() {
			this.add(EggRegistry.MONSTER_EGG_BROKEN, definition()
					.subtitle(modSubtitle(EggRegistry.MONSTER_EGG_BROKEN.getId()))
					.with(sound(modLoc("monster_egg_break"))));
		}

		public String modSubtitle(ResourceLocation id) {
			return MonsterEggs.MOD_ID + ".subtitle." + id.getPath();
		}

		public ResourceLocation modLoc(String name) {
			return new ResourceLocation(MonsterEggs.MOD_ID, name);
		}
	}

	private static class BlockStates extends BlockStateProvider {
		public BlockStates(PackOutput packOutput, ExistingFileHelper helper) {
			super(packOutput, MonsterEggs.MOD_ID, helper);
		}

		@Override
		protected void registerStatesAndModels() {
			makeEgg(EggRegistry.CAVE_SPIDER_EGG.get());
			makeEgg(EggRegistry.CREEPER_EGG.get());
			makeEgg(EggRegistry.ENDERMAN_EGG.get());
			makeEgg(EggRegistry.SKELETON_EGG.get());
			makeEgg(EggRegistry.SPIDER_EGG.get());
			makeEgg(EggRegistry.ZOMBIE_EGG.get());
		}

		private void makeEgg(Block block) {
			ModelFile model = models().getExistingFile(modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block).getPath()));
			ModelFile model2 = models().getExistingFile(modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block).getPath() + "_hanging"));
			getVariantBuilder(block)
					.partialState().with(BlockStateProperties.HANGING, false)
					.modelForState().modelFile(model).addModel()
					.partialState().with(BlockStateProperties.HANGING, true)
					.modelForState().modelFile(model2).addModel();
		}
	}

	private static class BlockModels extends BlockModelProvider {
		public BlockModels(PackOutput packOutput, ExistingFileHelper helper) {
			super(packOutput, MonsterEggs.MOD_ID, helper);
		}

		@Override
		protected void registerModels() {
			makeEgg(EggRegistry.CAVE_SPIDER_EGG.get());
			makeEgg(EggRegistry.CREEPER_EGG.get());
			makeEgg(EggRegistry.ENDERMAN_EGG.get());
			makeEgg(EggRegistry.SKELETON_EGG.get());
			makeEgg(EggRegistry.SPIDER_EGG.get());
			makeEgg(EggRegistry.ZOMBIE_EGG.get());
		}

		private void makeEgg(Block block) {
			ResourceLocation location = ForgeRegistries.BLOCKS.getKey(block);
			withExistingParent(location.getPath(), modLoc("block/monster_egg"))
					.texture("particle", "block/" + location.getPath())
					.texture("side", "block/" + location.getPath())
					.texture("top", "block/" + location.getPath() + "_top")
					.texture("bottom", "block/" + location.getPath() + "_bottom");
			withExistingParent(location.getPath() + "_hanging", modLoc("block/monster_egg_hanging"))
					.texture("particle", "block/" + location.getPath())
					.texture("side", "block/" + location.getPath())
					.texture("top", "block/" + location.getPath() + "_top")
					.texture("bottom", "block/" + location.getPath() + "_bottom");
		}
	}

	private static class ItemModels extends ItemModelProvider {
		public ItemModels(PackOutput packOutput, ExistingFileHelper helper) {
			super(packOutput, MonsterEggs.MOD_ID, helper);
		}

		@Override
		protected void registerModels() {
			makeEgg(EggRegistry.CAVE_SPIDER_EGG.get());
			makeEgg(EggRegistry.CREEPER_EGG.get());
			makeEgg(EggRegistry.ENDERMAN_EGG.get());
			makeEgg(EggRegistry.SKELETON_EGG.get());
			makeEgg(EggRegistry.SPIDER_EGG.get());
			makeEgg(EggRegistry.ZOMBIE_EGG.get());
		}

		private void makeEgg(Block block) {
			withExistingParent(ForgeRegistries.BLOCKS.getKey(block).getPath(), modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block).getPath()));
		}
	}
}
