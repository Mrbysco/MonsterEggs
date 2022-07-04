package com.mrbysco.monstereggs.datagen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import com.mrbysco.monstereggs.MonsterEggs;
import com.mrbysco.monstereggs.block.MonsterEggBlock;
import com.mrbysco.monstereggs.registry.EggRegistry;
import com.mrbysco.monstereggs.worldgen.modifier.AddFeaturesBlacklistBiomeModifier;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.EnvironmentScanPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.placement.SurfaceRelativeThresholdFilter;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
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
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MonsterDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		final RegistryOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, RegistryAccess.builtinCopy());
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(event.includeServer(), new Loots(generator));
			BlockTagsProvider provider;
			generator.addProvider(event.includeServer(), provider = new MonsterBlockTags(generator, helper));
			generator.addProvider(event.includeServer(), new MonsterItemTags(generator, provider, helper));

			generator.addProvider(event.includeServer(), JsonCodecProvider.forDatapackRegistry(
					generator, helper, MonsterEggs.MOD_ID, ops, Registry.CONFIGURED_FEATURE_REGISTRY, getConfiguredFeatures(ops)));

			generator.addProvider(event.includeServer(), JsonCodecProvider.forDatapackRegistry(
					generator, helper, MonsterEggs.MOD_ID, ops, Registry.PLACED_FEATURE_REGISTRY, getPlacedFeatures(ops)));

			generator.addProvider(event.includeServer(), JsonCodecProvider.forDatapackRegistry(
					generator, helper, MonsterEggs.MOD_ID, ops, ForgeRegistries.Keys.BIOME_MODIFIERS, getBiomeModifiers(ops)));
		}
		if (event.includeClient()) {
			generator.addProvider(event.includeClient(), new Language(generator));
			generator.addProvider(event.includeClient(), new SaltSoundProvider(generator, helper));
			generator.addProvider(event.includeClient(), new BlockModels(generator, helper));
			generator.addProvider(event.includeClient(), new ItemModels(generator, helper));
			generator.addProvider(event.includeClient(), new BlockStates(generator, helper));
		}
	}

	public static Map<ResourceLocation, ConfiguredFeature<?, ?>> getConfiguredFeatures(RegistryOps<JsonElement> ops) {
		Map<ResourceLocation, ConfiguredFeature<?, ?>> map = Maps.newHashMap();

		map.putAll(generateFeature("cave_spider_hanging_egg",
				EggRegistry.CAVE_SPIDER_EGG.get(), Direction.UP, 24));
		map.putAll(generateFeature("cave_spider_egg",
				EggRegistry.CAVE_SPIDER_EGG.get(), Direction.DOWN, 24));
		map.putAll(generateFeature("creeper_hanging_egg",
				EggRegistry.CREEPER_EGG.get(), Direction.UP, 24));
		map.putAll(generateFeature("creeper_egg",
				EggRegistry.CREEPER_EGG.get(), Direction.DOWN, 24));
		map.putAll(generateFeature("enderman_hanging_egg",
				EggRegistry.ENDERMAN_EGG.get(), Direction.UP, 24));
		map.putAll(generateFeature("enderman_egg",
				EggRegistry.ENDERMAN_EGG.get(), Direction.DOWN, 24));
		map.putAll(generateFeature("skeleton_hanging_egg",
				EggRegistry.SKELETON_EGG.get(), Direction.UP, 24));
		map.putAll(generateFeature("skeleton_egg",
				EggRegistry.SKELETON_EGG.get(), Direction.DOWN, 24));
		map.putAll(generateFeature("spider_hanging_egg",
				EggRegistry.SPIDER_EGG.get(), Direction.UP, 24));
		map.putAll(generateFeature("spider_egg",
				EggRegistry.SPIDER_EGG.get(), Direction.DOWN, 24));
		map.putAll(generateFeature("zombie_hanging_egg",
				EggRegistry.ZOMBIE_EGG.get(), Direction.UP, 24));
		map.putAll(generateFeature("zombie_egg",
				EggRegistry.ZOMBIE_EGG.get(), Direction.DOWN, 24));

		return map;
	}

	public static final Map<ResourceLocation, ConfiguredFeature<RandomPatchConfiguration, ?>> generateFeature(String name, Block block, Direction direction, int tries) {
		BlockState state = block.defaultBlockState();
		if (direction == Direction.UP) {
			state = state.setValue(MonsterEggBlock.HANGING, true);
		}
		net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider provider = net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider.simple(state);
		return Map.of(new ResourceLocation(MonsterEggs.MOD_ID, name), new ConfiguredFeature<>(Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK,
				new SimpleBlockConfiguration(provider), List.of(), tries)));
	}

	public static Map<ResourceLocation, PlacedFeature> getPlacedFeatures(RegistryOps<JsonElement> ops) {
		Map<ResourceLocation, PlacedFeature> map = Maps.newHashMap();

		map.putAll(makeEggFeature(ops, "cave_spider_hanging_egg", Direction.UP, 1, 4));
		map.putAll(makeEggFeature(ops, "cave_spider_egg", Direction.DOWN, 1, 4));
		map.putAll(makeEggFeature(ops, "creeper_hanging_egg", Direction.UP, 1, 4));
		map.putAll(makeEggFeature(ops, "creeper_egg", Direction.DOWN, 1, 4));
		map.putAll(makeEggFeature(ops, "enderman_hanging_egg", Direction.UP, 1, 4));
		map.putAll(makeEggFeature(ops, "enderman_egg", Direction.DOWN, 1, 4));
		map.putAll(makeEggFeature(ops, "skeleton_hanging_egg", Direction.UP, 1, 4));
		map.putAll(makeEggFeature(ops, "skeleton_egg", Direction.DOWN, 1, 4));
		map.putAll(makeEggFeature(ops, "spider_hanging_egg", Direction.UP, 1, 4));
		map.putAll(makeEggFeature(ops, "spider_egg", Direction.DOWN, 1, 4));
		map.putAll(makeEggFeature(ops, "zombie_hanging_egg", Direction.UP, 1, 4));
		map.putAll(makeEggFeature(ops, "zombie_egg", Direction.DOWN, 1, 4));

		return map;
	}

	private static Map<ResourceLocation, PlacedFeature> makeEggFeature(RegistryOps<JsonElement> ops, String configuredName,
																	   Direction direction, int rarity, int count) {
		Map<ResourceLocation, PlacedFeature> map = Maps.newHashMap();
		ResourceLocation location = new ResourceLocation(MonsterEggs.MOD_ID, configuredName);
		HolderSet.Direct<ConfiguredFeature<?, ?>> featureKeyHolder = HolderSet.direct(ops.registry(Registry.CONFIGURED_FEATURE_REGISTRY).get()
				.getOrCreateHolderOrThrow(ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, location)));
		if (featureKeyHolder != null) {
			EnvironmentScanPlacement environmentScanPlacement = EnvironmentScanPlacement.scanningFor(direction, BlockPredicate.hasSturdyFace(direction.getOpposite()),
					BlockPredicate.ONLY_IN_AIR_PREDICATE, 32);
			final PlacedFeature feature = new PlacedFeature(featureKeyHolder.get(0), List.of(RarityFilter.onAverageOnceEvery(rarity),
					CountPlacement.of(UniformInt.of(0, count)), InSquarePlacement.spread(), PlacementUtils.FULL_RANGE,
					environmentScanPlacement, SurfaceRelativeThresholdFilter.of(Heightmap.Types.OCEAN_FLOOR_WG, Integer.MIN_VALUE, -13),
					RandomOffsetPlacement.of(ConstantInt.of(-1), ConstantInt.of(-1)), BiomeFilter.biome()));

			return Map.of(location, feature);
		}
		return map;
	}

	public static Map<ResourceLocation, BiomeModifier> getBiomeModifiers(RegistryOps<JsonElement> ops) {
		Map<ResourceLocation, BiomeModifier> map = Maps.newHashMap();

		List<TagKey<Biome>> overworld = List.of(BiomeTags.IS_OVERWORLD);
		map.putAll(generateBiomeModifier(ops, new ResourceLocation(MonsterEggs.MOD_ID, "cave_spider_hanging_egg"),
				overworld, List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END), Decoration.UNDERGROUND_DECORATION));
		map.putAll(generateBiomeModifier(ops, new ResourceLocation(MonsterEggs.MOD_ID, "cave_spider_egg"),
				overworld, List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END), Decoration.UNDERGROUND_DECORATION));
		map.putAll(generateBiomeModifier(ops, new ResourceLocation(MonsterEggs.MOD_ID, "creeper_hanging_egg"),
				overworld, List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END), Decoration.UNDERGROUND_DECORATION));
		map.putAll(generateBiomeModifier(ops, new ResourceLocation(MonsterEggs.MOD_ID, "creeper_egg"),
				overworld, List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END), Decoration.UNDERGROUND_DECORATION));
		map.putAll(generateBiomeModifier(ops, new ResourceLocation(MonsterEggs.MOD_ID, "enderman_hanging_egg"),
				overworld, List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END), Decoration.UNDERGROUND_DECORATION));
		map.putAll(generateBiomeModifier(ops, new ResourceLocation(MonsterEggs.MOD_ID, "enderman_egg"),
				overworld, List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END), Decoration.UNDERGROUND_DECORATION));
		map.putAll(generateBiomeModifier(ops, new ResourceLocation(MonsterEggs.MOD_ID, "skeleton_hanging_egg"),
				overworld, List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END), Decoration.UNDERGROUND_DECORATION));
		map.putAll(generateBiomeModifier(ops, new ResourceLocation(MonsterEggs.MOD_ID, "skeleton_egg"),
				overworld, List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END), Decoration.UNDERGROUND_DECORATION));
		map.putAll(generateBiomeModifier(ops, new ResourceLocation(MonsterEggs.MOD_ID, "spider_hanging_egg"),
				overworld, List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END), Decoration.UNDERGROUND_DECORATION));
		map.putAll(generateBiomeModifier(ops, new ResourceLocation(MonsterEggs.MOD_ID, "spider_egg"),
				overworld, List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END), Decoration.UNDERGROUND_DECORATION));
		map.putAll(generateBiomeModifier(ops, new ResourceLocation(MonsterEggs.MOD_ID, "zombie_hanging_egg"),
				overworld, List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END), Decoration.UNDERGROUND_DECORATION));
		map.putAll(generateBiomeModifier(ops, new ResourceLocation(MonsterEggs.MOD_ID, "zombie_egg"),
				overworld, List.of(BiomeTags.IS_NETHER, BiomeTags.IS_END), Decoration.UNDERGROUND_DECORATION));

		return map;
	}


	private static Map<ResourceLocation, BiomeModifier> generateBiomeModifier(RegistryOps<JsonElement> ops, ResourceLocation location,
																			  @NotNull List<TagKey<Biome>> tags, @Nullable List<TagKey<Biome>> blacklistTags, Decoration decorationType) {
		final List<HolderSet<Biome>> tagHolders = tags.stream()
				.map(tag -> new HolderSet.Named<>(ops.registry(Registry.BIOME_REGISTRY).get(), tag)).collect(Collectors.toList());
		final List<HolderSet<Biome>> blacklistTagHolders = blacklistTags.isEmpty() ? List.of() : blacklistTags.stream()
				.map(tag -> new HolderSet.Named<>(ops.registry(Registry.BIOME_REGISTRY).get(), tag)).collect(Collectors.toList());
		final BiomeModifier addFeature = new AddFeaturesBlacklistBiomeModifier(
				tagHolders,
				blacklistTagHolders,
				HolderSet.direct(ops.registry(Registry.PLACED_FEATURE_REGISTRY).get().getOrCreateHolderOrThrow(ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY,
						location))),
				decorationType);
		return Map.of(location, addFeature);
	}

	private static class Loots extends LootTableProvider {
		public Loots(DataGenerator gen) {
			super(gen);
		}

		@Override
		protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, Builder>>>, LootContextParamSet>> getTables() {
			return ImmutableList.of(
					Pair.of(MonsterBlockTables::new, LootContextParamSets.BLOCK)
			);
		}

		public static class MonsterBlockTables extends BlockLoot {

			@Override
			protected void addTables() {
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
		public Language(DataGenerator gen) {
			super(gen, MonsterEggs.MOD_ID, "en_us");
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

	public static class SaltSoundProvider extends SoundDefinitionsProvider {
		public SaltSoundProvider(DataGenerator generator, ExistingFileHelper helper) {
			super(generator, MonsterEggs.MOD_ID, helper);
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
		public BlockStates(DataGenerator gen, ExistingFileHelper helper) {
			super(gen, MonsterEggs.MOD_ID, helper);
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
		public BlockModels(DataGenerator gen, ExistingFileHelper helper) {
			super(gen, MonsterEggs.MOD_ID, helper);
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
		public ItemModels(DataGenerator gen, ExistingFileHelper helper) {
			super(gen, MonsterEggs.MOD_ID, helper);
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

	public static class MonsterBlockTags extends BlockTagsProvider {
		public MonsterBlockTags(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
			super(generator, MonsterEggs.MOD_ID, existingFileHelper);
		}

		private static TagKey<Block> forgeTag(String name) {
			return BlockTags.create(new ResourceLocation("forge", name));
		}

		private static TagKey<Block> modTag(String modid, String name) {
			return BlockTags.create(new ResourceLocation(modid, name));
		}

		@Override
		protected void addTags() {

		}
	}

	public static class MonsterItemTags extends ItemTagsProvider {
		public MonsterItemTags(DataGenerator dataGenerator, BlockTagsProvider blockTagsProvider, ExistingFileHelper existingFileHelper) {
			super(dataGenerator, blockTagsProvider, MonsterEggs.MOD_ID, existingFileHelper);
		}

		@Override
		protected void addTags() {

		}

		private static TagKey<Item> forgeTag(String name) {
			return ItemTags.create(new ResourceLocation("forge", name));
		}
	}
}
