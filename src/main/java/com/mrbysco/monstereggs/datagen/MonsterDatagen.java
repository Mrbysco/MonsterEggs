package com.mrbysco.monstereggs.datagen;

import com.mrbysco.monstereggs.MonsterEggs;
import com.mrbysco.monstereggs.block.MonsterEggBlock;
import com.mrbysco.monstereggs.registry.EggConfiguredFeatures;
import com.mrbysco.monstereggs.registry.EggPlacedFeatures;
import com.mrbysco.monstereggs.registry.EggRegistry;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.core.Cloner;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class MonsterDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent.Client event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		generator.addProvider(true, new Loots(packOutput, lookupProvider));

		generator.addProvider(true, new DatapackBuiltinEntriesProvider(
				packOutput, CompletableFuture.supplyAsync(MonsterDatagen::getProvider), Set.of(MonsterEggs.MOD_ID)));

		generator.addProvider(true, new Language(packOutput));
		generator.addProvider(true, new MonsterSoundProvider(packOutput));

		generator.addProvider(true, new MonsterModels(packOutput));
	}

	private static RegistrySetBuilder.PatchedRegistries getProvider() {
		final RegistrySetBuilder registryBuilder = new RegistrySetBuilder();
		registryBuilder.add(Registries.CONFIGURED_FEATURE, EggConfiguredFeatures::bootstrap);
		registryBuilder.add(Registries.PLACED_FEATURE, EggPlacedFeatures::bootstrap);
		registryBuilder.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, MonsterBiomeModifiers::bootstrap);
		// We need the BIOME registry to be present, so we can use a biome tag, doesn't matter that it's empty
		registryBuilder.add(Registries.BIOME, $ -> {
		});
		RegistryAccess.Frozen regAccess = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
		Cloner.Factory cloner$factory = new Cloner.Factory();
		net.neoforged.neoforge.registries.DataPackRegistriesHooks.getDataPackRegistriesWithDimensions().forEach(data -> data.runWithArguments(cloner$factory::addCodec));
		return registryBuilder.buildPatch(regAccess, VanillaRegistries.createLookup(), cloner$factory);
	}

	private static class Loots extends LootTableProvider {
		public Loots(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
			super(packOutput, Set.of(),
					List.of(new SubProviderEntry(MonsterBlockTables::new, LootContextParamSets.BLOCK))
					, lookupProvider);
		}

		public static class MonsterBlockTables extends BlockLootSubProvider {

			protected MonsterBlockTables(HolderLookup.Provider provider) {
				super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
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
				return (Iterable<Block>) EggRegistry.BLOCKS.getEntries().stream().map(holder -> (Block) holder.get())::iterator;
			}
		}

		@Override
		protected void validate(WritableRegistry<LootTable> writableregistry, ValidationContext validationcontext, ProblemReporter.Collector problemreporter$collector) {
			super.validate(writableregistry, validationcontext, problemreporter$collector);
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

		public void addSubtitle(Supplier<SoundEvent> sound, String name) {
			this.addSubtitle(sound.get(), name);
		}

		public void addSubtitle(SoundEvent sound, String name) {
			String path = MonsterEggs.MOD_ID + ".subtitle." + sound.location().getPath();
			this.add(path, name);
		}
	}

	public static class MonsterSoundProvider extends SoundDefinitionsProvider {
		public MonsterSoundProvider(PackOutput packOutput) {
			super(packOutput, MonsterEggs.MOD_ID);
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
			return ResourceLocation.fromNamespaceAndPath(MonsterEggs.MOD_ID, name);
		}
	}

	private static class MonsterModels extends ModelProvider {
		public static final ModelTemplate EGG = ModelTemplates.create("monstereggs:monster_egg", TextureSlot.PARTICLE, TextureSlot.SIDE, TextureSlot.BOTTOM, TextureSlot.TOP);
		public static final ModelTemplate HANGING_EGG = ModelTemplates.create("monstereggs:monster_egg_hanging", "_hanging", TextureSlot.PARTICLE, TextureSlot.SIDE, TextureSlot.BOTTOM, TextureSlot.TOP);

		public static final TexturedModel.Provider EGG_MODEL = TexturedModel.createDefault(MonsterModels::egg, EGG);
		public static final TexturedModel.Provider HANGING_EGG_MODEL = TexturedModel.createDefault(MonsterModels::egg, HANGING_EGG);

		public MonsterModels(PackOutput output) {
			super(output, MonsterEggs.MOD_ID);
		}

		@Override
		protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
			makeEgg(EggRegistry.CAVE_SPIDER_EGG, blockModels);
			makeEgg(EggRegistry.CREEPER_EGG, blockModels);
			makeEgg(EggRegistry.ENDERMAN_EGG, blockModels);
			makeEgg(EggRegistry.SKELETON_EGG, blockModels);
			makeEgg(EggRegistry.SPIDER_EGG, blockModels);
			makeEgg(EggRegistry.ZOMBIE_EGG, blockModels);

		}

		private void makeEgg(DeferredBlock<MonsterEggBlock> deferredBlock, BlockModelGenerators blockModels) {
			ResourceLocation eggModel = EGG_MODEL.create(deferredBlock.get(), blockModels.modelOutput);
			ResourceLocation hangingEggModel = HANGING_EGG_MODEL.create(deferredBlock.get(), blockModels.modelOutput);
			blockModels.registerSimpleItemModel(deferredBlock.get(), deferredBlock.getId().withPrefix("block/"));
			blockModels.blockStateOutput
					.accept(
							MultiVariantGenerator.multiVariant(deferredBlock.get())
									.with(
											BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.HANGING,
													hangingEggModel, eggModel
											)
									)
					);
		}

		public static TextureMapping egg(Block block) {
			return new TextureMapping()
					.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block))
					.put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(block, "_bottom"))
					.put(TextureSlot.TOP, TextureMapping.getBlockTexture(block, "_top"))
					.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block));
		}
	}
}
