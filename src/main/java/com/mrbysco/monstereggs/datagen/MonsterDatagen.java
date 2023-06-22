package com.mrbysco.monstereggs.datagen;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.mrbysco.monstereggs.MonsterEggs;
import com.mrbysco.monstereggs.registry.EggConfiguredFeatures;
import com.mrbysco.monstereggs.registry.EggPlacedFeatures;
import com.mrbysco.monstereggs.registry.EggRegistry;
import net.minecraft.core.HolderLookup;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

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

			generator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(
					packOutput, CompletableFuture.supplyAsync(MonsterDatagen::getProvider), Set.of(MonsterEggs.MOD_ID)));
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
		registryBuilder.add(ForgeRegistries.Keys.BIOME_MODIFIERS, MonsterBiomeModifiers::bootstrap);
		// We need the BIOME registry to be present so we can use a biome tag, doesn't matter that it's empty
		registryBuilder.add(Registries.BIOME, context -> {
		});
		RegistryAccess.Frozen regAccess = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
		return registryBuilder.buildPatch(regAccess, VanillaRegistries.createLookup());
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
			map.forEach((name, table) -> table.validate(validationContext));
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
