package com.mrbysco.monstereggs.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.monstereggs.MonsterEggs;
import com.mrbysco.monstereggs.worldgen.modifier.AddFeaturesBlacklistBiomeModifier;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.function.Supplier;

public class EggModifiers {
	public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, MonsterEggs.MOD_ID);

	public static final Supplier<Codec<AddFeaturesBlacklistBiomeModifier>> ADD_FEATURES_BLACKLIST_BIOME_MODIFIER_TYPE = BIOME_MODIFIER_SERIALIZERS.register("add_features_with_blacklist", () ->
			RecordCodecBuilder.create(builder -> builder.group(
					Biome.LIST_CODEC.listOf().fieldOf("whitelist").forGetter(AddFeaturesBlacklistBiomeModifier::biomes),
					Biome.LIST_CODEC.listOf().fieldOf("blacklist").orElse(new ArrayList<>()).forGetter(AddFeaturesBlacklistBiomeModifier::blacklistBiomes),
					PlacedFeature.LIST_CODEC.fieldOf("features").forGetter(AddFeaturesBlacklistBiomeModifier::features),
					Decoration.CODEC.fieldOf("step").forGetter(AddFeaturesBlacklistBiomeModifier::step)
			).apply(builder, AddFeaturesBlacklistBiomeModifier::new))
	);
}
