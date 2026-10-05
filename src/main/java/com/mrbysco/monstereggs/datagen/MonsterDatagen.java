package com.mrbysco.monstereggs.datagen;

import com.mrbysco.monstereggs.MonsterEggs;
import com.mrbysco.monstereggs.block.MonsterEggBlock;
import com.mrbysco.monstereggs.registry.EggConfiguredFeatures;
import com.mrbysco.monstereggs.registry.EggPlacedFeatures;
import com.mrbysco.monstereggs.registry.EggRegistry;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

@EventBusSubscriber
public class MonsterDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent.Client event) {
		event.createProvider(Language::new);
		event.createProvider(MonsterSoundProvider::new);
		event.createProvider(MonsterModels::new);

		RegistrySetBuilder worldBuilder = new RegistrySetBuilder()
				.add(Registries.FEATURE, EggConfiguredFeatures::bootstrap)
				.add(Registries.PLACED_FEATURE, EggPlacedFeatures::bootstrap)
				.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, MonsterBiomeModifiers::bootstrap);

		event.createWorldRegistryObjects(worldBuilder, Set.of(MonsterEggs.MOD_ID));

		RegistrySetBuilder reloadableBuilder = new RegistrySetBuilder()
				.add(Registries.LOOT_TABLE, new LootTableProvider(
						Set.of(),
						List.of(new LootTableProvider.SubProviderEntry(MonsterBlockTables::new,
								LootContextParamSets.BLOCK))));

		event.createReloadableRegistryObjects(reloadableBuilder, Set.of(MonsterEggs.MOD_ID));
	}

	public static class MonsterBlockTables extends BlockLootSubProvider {
		public MonsterBlockTables(LootTableSubProvider.Context context) {
			super(Set.of(), FeatureFlags.REGISTRY.allFlags(), context);
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
		protected @NonNull Iterable<Block> getKnownBlocks() {
			return EggRegistry.BLOCKS.getEntries()
					.stream()
					.map(holder -> (Block) holder.get())::iterator;
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

			addConfig("general", "General", "General settings");
			addConfig("spawnOffset", "Spawn Offset", "Dictates the Y offset of the mob spawned from the egg [Default: 0.5]");
			addConfig("debug", "Debug", "Debug settings");
			addConfig("debugInfo", "Debug Info", "Show the mob in the tooltip of the shell [Default: false]");
		}

		public void addSubtitle(Supplier<SoundEvent> sound, String name) {
			this.addSubtitle(sound.get(), name);
		}

		public void addSubtitle(SoundEvent sound, String name) {
			String path = MonsterEggs.MOD_ID + ".subtitle." + sound.location().getPath();
			this.add(path, name);
		}

		/**
		 * Add the translation for a config entry
		 *
		 * @param path        The path of the config entry
		 * @param name        The name of the config entry
		 * @param description The description of the config entry (optional in case of targeting "title" or similar entries that have no tooltip)
		 */
		private void addConfig(String path, String name, @Nullable String description) {
			this.add("monstereggs.configuration." + path, name);
			if (description != null && !description.isEmpty())
				this.add("monstereggs.configuration." + path + ".tooltip", description);
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

		public String modSubtitle(Identifier id) {
			return MonsterEggs.MOD_ID + ".subtitle." + id.getPath();
		}

		public Identifier modLoc(String name) {
			return Identifier.fromNamespaceAndPath(MonsterEggs.MOD_ID, name);
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
		protected void registerModels(@NonNull BlockModelGenerators blockModels, @NonNull ItemModelGenerators itemModels) {
			makeEgg(EggRegistry.CAVE_SPIDER_EGG, blockModels);
			makeEgg(EggRegistry.CREEPER_EGG, blockModels);
			makeEgg(EggRegistry.ENDERMAN_EGG, blockModels);
			makeEgg(EggRegistry.SKELETON_EGG, blockModels);
			makeEgg(EggRegistry.SPIDER_EGG, blockModels);
			makeEgg(EggRegistry.ZOMBIE_EGG, blockModels);

		}

		private void makeEgg(DeferredBlock<MonsterEggBlock> deferredBlock, BlockModelGenerators blockModels) {
			MultiVariant eggVariant = BlockModelGenerators.plainVariant(EGG_MODEL.create(deferredBlock.get(), blockModels.modelOutput));
			MultiVariant hangingEggVariant = BlockModelGenerators.plainVariant(HANGING_EGG_MODEL.create(deferredBlock.get(), blockModels.modelOutput));
			blockModels.registerSimpleItemModel(deferredBlock.get(), deferredBlock.getId().withPrefix("block/"));
			blockModels.blockStateOutput
					.accept(MultiVariantGenerator.dispatch(deferredBlock.get())
							.with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.HANGING, hangingEggVariant, eggVariant)));
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
