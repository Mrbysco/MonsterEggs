package com.mrbysco.monstereggs.datagen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mrbysco.monstereggs.MonsterEggs;
import com.mrbysco.monstereggs.registry.EggRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
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
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MonsterDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(new Loots(generator));
			BlockTagsProvider provider;
			generator.addProvider(provider = new MonsterBlockTags(generator, helper));
			generator.addProvider(new MonsterItemTags(generator, provider, helper));
		}
		if (event.includeClient()) {
			generator.addProvider(new Language(generator));
			generator.addProvider(new SaltSoundProvider(generator, helper));
			generator.addProvider(new BlockModels(generator, helper));
			generator.addProvider(new ItemModels(generator, helper));
			generator.addProvider(new BlockStates(generator, helper));
		}
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

			addBlock(EggRegistry.CAVE_SPIDER_EGG, "Cave Spider Egg Sack");
			addBlock(EggRegistry.CREEPER_EGG, "Creeper Egg Sack");
			addBlock(EggRegistry.ENDERMAN_EGG, "Enderman Egg Sack");
			addBlock(EggRegistry.SKELETON_EGG, "Skeleton Egg Sack");
			addBlock(EggRegistry.SPIDER_EGG, "Spider Egg Sack");
			addBlock(EggRegistry.ZOMBIE_EGG, "Zombie Egg Sack");
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
			ModelFile model = models().getExistingFile(modLoc("block/" + block.getRegistryName().getPath()));
			getVariantBuilder(block)
					.partialState().modelForState().modelFile(model).addModel();
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
			ResourceLocation location = block.getRegistryName();
			withExistingParent(location.getPath(), modLoc("block/monster_egg"))
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
			withExistingParent(block.getRegistryName().getPath(), modLoc("block/" + block.getRegistryName().getPath()));
		}
	}

	public static class MonsterBlockTags extends BlockTagsProvider {
		public MonsterBlockTags(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
			super(generator, MonsterEggs.MOD_ID, existingFileHelper);
		}

		private static Tag.Named<Block> forgeTag(String name) {
			return BlockTags.bind(new ResourceLocation("forge", name).toString());
		}

		private static Tags.IOptionalNamedTag<Block> optionalTag(String modid, String name) {
			return BlockTags.createOptional(new ResourceLocation(modid, name));
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

		private static Tag.Named<Item> forgeTag(String name) {
			return ItemTags.bind(new ResourceLocation("forge", name).toString());
		}
	}
}
