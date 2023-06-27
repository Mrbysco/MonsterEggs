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
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class EggRegistry {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MonsterEggs.MOD_ID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MonsterEggs.MOD_ID);
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MonsterEggs.MOD_ID);
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MonsterEggs.MOD_ID);

	public static final RegistryObject<SoundEvent> MONSTER_EGG_BROKEN = SOUND_EVENTS.register("monster_egg.broken", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MonsterEggs.MOD_ID, "monster_egg.broken")));

	public static final RegistryObject<Block> CAVE_SPIDER_EGG = BLOCKS.register("cave_spider_egg", () -> new MonsterEggBlock(() -> EntityType.CAVE_SPIDER, Block.Properties.copy(Blocks.TURTLE_EGG).mapColor(MapColor.COLOR_GREEN).strength(0.5F).sound(SoundType.GRASS).lightLevel((state) -> 4)));
	public static final RegistryObject<Block> CREEPER_EGG = BLOCKS.register("creeper_egg", () -> new MonsterEggBlock(() -> EntityType.CREEPER, Block.Properties.copy(Blocks.TURTLE_EGG).mapColor(MapColor.COLOR_GREEN).strength(0.5F).sound(SoundType.GRASS).lightLevel((state) -> 4)));
	public static final RegistryObject<Block> ENDERMAN_EGG = BLOCKS.register("enderman_egg", () -> new MonsterEggBlock(() -> EntityType.ENDERMAN, Block.Properties.copy(Blocks.TURTLE_EGG).mapColor(MapColor.COLOR_GREEN).strength(0.5F).sound(SoundType.GRASS).lightLevel((state) -> 4)));
	public static final RegistryObject<Block> SKELETON_EGG = BLOCKS.register("skeleton_egg", () -> new MonsterEggBlock(() -> EntityType.SKELETON, Block.Properties.copy(Blocks.TURTLE_EGG).mapColor(MapColor.COLOR_GREEN).strength(0.5F).sound(SoundType.GRASS).lightLevel((state) -> 4)));
	public static final RegistryObject<Block> SPIDER_EGG = BLOCKS.register("spider_egg", () -> new MonsterEggBlock(() -> EntityType.SPIDER, Block.Properties.copy(Blocks.TURTLE_EGG).mapColor(MapColor.COLOR_GREEN).strength(0.5F).sound(SoundType.GRASS).lightLevel((state) -> 4)));
	public static final RegistryObject<Block> ZOMBIE_EGG = BLOCKS.register("zombie_egg", () -> new MonsterEggBlock(() -> EntityType.ZOMBIE, Block.Properties.copy(Blocks.TURTLE_EGG).mapColor(MapColor.COLOR_GREEN).strength(0.5F).sound(SoundType.GRASS).lightLevel((state) -> 4)));

	public static final RegistryObject<Item> CAVE_SPIDER_EGG_ITEM = ITEMS.register("cave_spider_egg", () -> new BlockItem(CAVE_SPIDER_EGG.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> CREEPER_EGG_ITEM = ITEMS.register("creeper_egg", () -> new BlockItem(CREEPER_EGG.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ENDERMAN_EGG_ITEM = ITEMS.register("enderman_egg", () -> new BlockItem(ENDERMAN_EGG.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> SKELETON_EGG_ITEM = ITEMS.register("skeleton_egg", () -> new BlockItem(SKELETON_EGG.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> SPIDER_EGG_ITEM = ITEMS.register("spider_egg", () -> new BlockItem(SPIDER_EGG.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ZOMBIE_EGG_ITEM = ITEMS.register("zombie_egg", () -> new BlockItem(ZOMBIE_EGG.get(), new Item.Properties().rarity(Rarity.UNCOMMON)));

	public static final RegistryObject<CreativeModeTab> EGG_TAB = CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
			.icon(() -> new ItemStack(EggRegistry.CREEPER_EGG.get()))
			.withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
			.title(Component.translatable("itemGroup.monstereggs"))
			.displayItems((displayParameters, output) -> {
				List<ItemStack> stacks = EggRegistry.BLOCKS.getEntries().stream().map(reg -> new ItemStack(reg.get())).toList();
				output.acceptAll(stacks);
			}).build());
}