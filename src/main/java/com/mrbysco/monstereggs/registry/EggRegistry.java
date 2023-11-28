package com.mrbysco.monstereggs.registry;

import com.mrbysco.monstereggs.MonsterEggs;
import com.mrbysco.monstereggs.block.MonsterEggBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

public class EggRegistry {
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MonsterEggs.MOD_ID);
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MonsterEggs.MOD_ID);
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MonsterEggs.MOD_ID);
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, MonsterEggs.MOD_ID);

	public static final DeferredHolder<SoundEvent, SoundEvent> MONSTER_EGG_BROKEN = SOUND_EVENTS.register("monster_egg.broken", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MonsterEggs.MOD_ID, "monster_egg.broken")));

	public static final DeferredBlock<MonsterEggBlock> CAVE_SPIDER_EGG = BLOCKS.register("cave_spider_egg", () -> new MonsterEggBlock(() -> EntityType.CAVE_SPIDER, Block.Properties.copy(Blocks.TURTLE_EGG).mapColor(MapColor.COLOR_GREEN).strength(0.5F).sound(SoundType.GRASS).lightLevel((state) -> 4)));
	public static final DeferredBlock<MonsterEggBlock> CREEPER_EGG = BLOCKS.register("creeper_egg", () -> new MonsterEggBlock(() -> EntityType.CREEPER, Block.Properties.copy(Blocks.TURTLE_EGG).mapColor(MapColor.COLOR_GREEN).strength(0.5F).sound(SoundType.GRASS).lightLevel((state) -> 4)));
	public static final DeferredBlock<MonsterEggBlock> ENDERMAN_EGG = BLOCKS.register("enderman_egg", () -> new MonsterEggBlock(() -> EntityType.ENDERMAN, Block.Properties.copy(Blocks.TURTLE_EGG).mapColor(MapColor.COLOR_GREEN).strength(0.5F).sound(SoundType.GRASS).lightLevel((state) -> 4)));
	public static final DeferredBlock<MonsterEggBlock> SKELETON_EGG = BLOCKS.register("skeleton_egg", () -> new MonsterEggBlock(() -> EntityType.SKELETON, Block.Properties.copy(Blocks.TURTLE_EGG).mapColor(MapColor.COLOR_GREEN).strength(0.5F).sound(SoundType.GRASS).lightLevel((state) -> 4)));
	public static final DeferredBlock<MonsterEggBlock> SPIDER_EGG = BLOCKS.register("spider_egg", () -> new MonsterEggBlock(() -> EntityType.SPIDER, Block.Properties.copy(Blocks.TURTLE_EGG).mapColor(MapColor.COLOR_GREEN).strength(0.5F).sound(SoundType.GRASS).lightLevel((state) -> 4)));
	public static final DeferredBlock<MonsterEggBlock> ZOMBIE_EGG = BLOCKS.register("zombie_egg", () -> new MonsterEggBlock(() -> EntityType.ZOMBIE, Block.Properties.copy(Blocks.TURTLE_EGG).mapColor(MapColor.COLOR_GREEN).strength(0.5F).sound(SoundType.GRASS).lightLevel((state) -> 4)));

	public static final DeferredItem<BlockItem> CAVE_SPIDER_EGG_ITEM = ITEMS.registerSimpleBlockItem("cave_spider_egg", CAVE_SPIDER_EGG, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final DeferredItem<BlockItem> CREEPER_EGG_ITEM = ITEMS.registerSimpleBlockItem("creeper_egg", CREEPER_EGG, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final DeferredItem<BlockItem> ENDERMAN_EGG_ITEM = ITEMS.registerSimpleBlockItem("enderman_egg", ENDERMAN_EGG, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final DeferredItem<BlockItem> SKELETON_EGG_ITEM = ITEMS.registerSimpleBlockItem("skeleton_egg", SKELETON_EGG, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final DeferredItem<BlockItem> SPIDER_EGG_ITEM = ITEMS.registerSimpleBlockItem("spider_egg", SPIDER_EGG, new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final DeferredItem<BlockItem> ZOMBIE_EGG_ITEM = ITEMS.registerSimpleBlockItem("zombie_egg", ZOMBIE_EGG, new Item.Properties().rarity(Rarity.UNCOMMON));

	public static final Supplier<CreativeModeTab> EGG_TAB = CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
			.icon(() -> new ItemStack(EggRegistry.CREEPER_EGG.get()))
			.withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
			.title(Component.translatable("itemGroup.monstereggs"))
			.displayItems((displayParameters, output) -> {
				List<ItemStack> stacks = EggRegistry.BLOCKS.getEntries().stream().map(reg -> new ItemStack(reg.get())).toList();
				output.acceptAll(stacks);
			}).build());
}