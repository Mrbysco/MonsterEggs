package com.mrbysco.monstereggs.registry;

import com.mrbysco.monstereggs.MonsterEggs;
import com.mrbysco.monstereggs.block.MonsterEggBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EggRegistry {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MonsterEggs.MOD_ID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MonsterEggs.MOD_ID);
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MonsterEggs.MOD_ID);

	public static final RegistryObject<SoundEvent> MONSTER_EGG_BROKEN = SOUND_EVENTS.register("monster_egg.broken", () -> new SoundEvent(new ResourceLocation(MonsterEggs.MOD_ID, "monster_egg.broken")));

	public static final RegistryObject<Block> CAVE_SPIDER_EGG = BLOCKS.register("cave_spider_egg", () -> new MonsterEggBlock(() -> EntityType.CAVE_SPIDER, Block.Properties.of(Material.EGG, MaterialColor.COLOR_GREEN).strength(0.5F).sound(SoundType.GRASS).lightLevel((state) -> 4)));
	public static final RegistryObject<Block> CREEPER_EGG = BLOCKS.register("creeper_egg", () -> new MonsterEggBlock(() -> EntityType.CREEPER, Block.Properties.of(Material.EGG, MaterialColor.COLOR_GREEN).strength(0.5F).sound(SoundType.GRASS).lightLevel((state) -> 4)));
	public static final RegistryObject<Block> ENDERMAN_EGG = BLOCKS.register("enderman_egg", () -> new MonsterEggBlock(() -> EntityType.ENDERMAN, Block.Properties.of(Material.EGG, MaterialColor.COLOR_GREEN).strength(0.5F).sound(SoundType.GRASS).lightLevel((state) -> 4)));
	public static final RegistryObject<Block> SKELETON_EGG = BLOCKS.register("skeleton_egg", () -> new MonsterEggBlock(() -> EntityType.SKELETON, Block.Properties.of(Material.EGG, MaterialColor.COLOR_GREEN).strength(0.5F).sound(SoundType.GRASS).lightLevel((state) -> 4)));
	public static final RegistryObject<Block> SPIDER_EGG = BLOCKS.register("spider_egg", () -> new MonsterEggBlock(() -> EntityType.SPIDER, Block.Properties.of(Material.EGG, MaterialColor.COLOR_GREEN).strength(0.5F).sound(SoundType.GRASS).lightLevel((state) -> 4)));
	public static final RegistryObject<Block> ZOMBIE_EGG = BLOCKS.register("zombie_egg", () -> new MonsterEggBlock(() -> EntityType.ZOMBIE, Block.Properties.of(Material.EGG, MaterialColor.COLOR_GREEN).strength(0.5F).sound(SoundType.GRASS).lightLevel((state) -> 4)));

	public static final RegistryObject<Item> CAVE_SPIDER_EGG_ITEM = ITEMS.register("cave_spider_egg", () -> new BlockItem(CAVE_SPIDER_EGG.get(), new Item.Properties().tab(MonsterEggs.TAB_EGGS)));
	public static final RegistryObject<Item> CREEPER_EGG_ITEM = ITEMS.register("creeper_egg", () -> new BlockItem(CREEPER_EGG.get(), new Item.Properties().tab(MonsterEggs.TAB_EGGS)));
	public static final RegistryObject<Item> ENDERMAN_EGG_ITEM = ITEMS.register("enderman_egg", () -> new BlockItem(ENDERMAN_EGG.get(), new Item.Properties().tab(MonsterEggs.TAB_EGGS)));
	public static final RegistryObject<Item> SKELETON_EGG_ITEM = ITEMS.register("skeleton_egg", () -> new BlockItem(SKELETON_EGG.get(), new Item.Properties().tab(MonsterEggs.TAB_EGGS)));
	public static final RegistryObject<Item> SPIDER_EGG_ITEM = ITEMS.register("spider_egg", () -> new BlockItem(SPIDER_EGG.get(), new Item.Properties().tab(MonsterEggs.TAB_EGGS)));
	public static final RegistryObject<Item> ZOMBIE_EGG_ITEM = ITEMS.register("zombie_egg", () -> new BlockItem(ZOMBIE_EGG.get(), new Item.Properties().tab(MonsterEggs.TAB_EGGS)));
}