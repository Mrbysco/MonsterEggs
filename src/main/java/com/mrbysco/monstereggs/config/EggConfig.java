package com.mrbysco.monstereggs.config;

import com.mrbysco.monstereggs.MonsterEggs;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

public class EggConfig {

	public static class Common {
		public final BooleanValue debugInfo;

		public final BooleanValue generateCaveSpiderEggs;
		public final BooleanValue generateCreeperEggs;
		public final BooleanValue generateEndermanEggs;
		public final BooleanValue generateSkeletonEggs;
		public final BooleanValue generateSpiderEggs;
		public final BooleanValue generateZombieEggs;

		public final BooleanValue generateHangingCaveSpiderEggs;
		public final BooleanValue generateHangingCreeperEggs;
		public final BooleanValue generateHangingEndermanEggs;
		public final BooleanValue generateHangingSkeletonEggs;
		public final BooleanValue generateHangingSpiderEggs;
		public final BooleanValue generateHangingZombieEggs;

		public final IntValue caveSpiderEggsRarity;
		public final IntValue creeperEggsRarity;
		public final IntValue endermanEggsRarity;
		public final IntValue skeletonEggsRarity;
		public final IntValue spiderEggsRarity;
		public final IntValue zombieEggsRarity;

		public final IntValue caveSpiderEggsCount;
		public final IntValue creeperEggsCount;
		public final IntValue endermanEggsCount;
		public final IntValue skeletonEggsCount;
		public final IntValue spiderEggsCount;
		public final IntValue zombieEggsCount;

		public final IntValue caveSpiderEggsTries;
		public final IntValue creeperEggsTries;
		public final IntValue endermanEggsTries;
		public final IntValue skeletonEggsTries;
		public final IntValue spiderEggsTries;
		public final IntValue zombieEggsTries;

		Common(ForgeConfigSpec.Builder builder) {
			builder.comment("Debug settings")
					.push("Debug");

			debugInfo = builder
					.comment("Show the mob in the tooltip of the shell [Default: false]")
					.define("debugInfo", false);

			builder.pop();

			builder.comment("Generation settings")
					.push("Generation");

			generateCaveSpiderEggs = builder
					.comment("Generate Cave Spider Eggs [Default: true]")
					.define("generateCaveSpiderEggs", true);

			generateCreeperEggs = builder
					.comment("Generate Creeper Eggs [Default: true]")
					.define("generateCreeperEggs", true);

			generateEndermanEggs = builder
					.comment("Generate Enderman Eggs [Default: true]")
					.define("generateEndermanEggs", true);

			generateSkeletonEggs = builder
					.comment("Generate Skeleton Eggs [Default: true]")
					.define("generateSkeletonEggs", true);

			generateSpiderEggs = builder
					.comment("Generate Spider Eggs [Default: true]")
					.define("generateSpiderEggs", true);

			generateZombieEggs = builder
					.comment("Generate Zombie Eggs [Default: true]")
					.define("generateZombieEggs", true);

			generateHangingCaveSpiderEggs = builder
					.comment("GenerateHanging Cave Spider Eggs [Default: true]")
					.define("generateHangingCaveSpiderEggs", true);

			generateHangingCreeperEggs = builder
					.comment("GenerateHanging Creeper Eggs [Default: true]")
					.define("generateHangingCreeperEggs", true);

			generateHangingEndermanEggs = builder
					.comment("GenerateHanging Enderman Eggs [Default: true]")
					.define("generateHangingEndermanEggs", true);

			generateHangingSkeletonEggs = builder
					.comment("GenerateHanging Skeleton Eggs [Default: true]")
					.define("generateHangingSkeletonEggs", true);

			generateHangingSpiderEggs = builder
					.comment("GenerateHanging Spider Eggs [Default: true]")
					.define("generateHangingSpiderEggs", true);

			generateHangingZombieEggs = builder
					.comment("GenerateHanging Zombie Eggs [Default: true]")
					.define("generateHangingZombieEggs", true);

			builder.pop();
			builder.comment("Rarity settings")
					.push("Rarity");

			caveSpiderEggsRarity = builder
					.comment("The rarity at which Cave Spider Eggs will generate (1 every X) [Default: 1]")
					.defineInRange("caveSpiderEggsRarity", 1, 1, Integer.MAX_VALUE);

			creeperEggsRarity = builder
					.comment("The rarity at which Creeper Eggs will generate (1 every X) [Default: 1]")
					.defineInRange("creeperEggsRarity", 1, 1, Integer.MAX_VALUE);

			endermanEggsRarity = builder
					.comment("The rarity at which Enderman Eggs will generate (1 every X) [Default: 1]")
					.defineInRange("endermanEggsRarity", 1, 1, Integer.MAX_VALUE);

			skeletonEggsRarity = builder
					.comment("The rarity at which Skeleton Eggs will generate (1 every X) [Default: 1]")
					.defineInRange("skeletonEggsRarity", 1, 1, Integer.MAX_VALUE);

			spiderEggsRarity = builder
					.comment("The rarity at which Spider Eggs will generate (1 every X) [Default: 1]")
					.defineInRange("spiderEggsRarity", 1, 1, Integer.MAX_VALUE);

			zombieEggsRarity = builder
					.comment("The rarity at which Zombie Eggs will generate (1 every X) [Default: 1]")
					.defineInRange("zombieEggsRarity", 1, 1, Integer.MAX_VALUE);
			
			builder.pop();
			builder.comment("Count settings")
					.push("Count");

			caveSpiderEggsCount = builder
					.comment("The amount of Cave Spider Eggs that it will try to generate at a time [Default: 4]")
					.defineInRange("caveSpiderEggsCount", 4, 1, Integer.MAX_VALUE);

			creeperEggsCount = builder
					.comment("The amount of Creeper Eggs that it will try to generate at a time [Default: 4]")
					.defineInRange("creeperEggsCount", 4, 1, Integer.MAX_VALUE);

			endermanEggsCount = builder
					.comment("The amount of Enderman Eggs that it will try to generate at a time [Default: 4]")
					.defineInRange("endermanEggsCount", 4, 1, Integer.MAX_VALUE);

			skeletonEggsCount = builder
					.comment("The amount of Skeleton Eggs that it will try to generate at a time [Default: 4]")
					.defineInRange("skeletonEggsCount", 4, 1, Integer.MAX_VALUE);

			spiderEggsCount = builder
					.comment("The amount of Spider Eggs that it will try to generate at a time [Default: 4]")
					.defineInRange("spiderEggsCount", 4, 1, Integer.MAX_VALUE);

			zombieEggsCount = builder
					.comment("The amount of Zombie Eggs that it will try to generate at a time [Default: 4]")
					.defineInRange("zombieEggsCount", 4, 1, Integer.MAX_VALUE);
			
			builder.pop();
			builder.comment("Tries settings")
					.push("Tries");

			caveSpiderEggsTries = builder
					.comment("The amount of times it will try to generate Cave Spider Eggs [Default: 24]")
					.defineInRange("caveSpiderEggsTries", 24, 1, Integer.MAX_VALUE);

			creeperEggsTries = builder
					.comment("The amount of times it will try to generate Creeper Eggs [Default: 24]")
					.defineInRange("creeperEggsTries", 24, 1, Integer.MAX_VALUE);

			endermanEggsTries = builder
					.comment("The amount of times it will try to generate Enderman Eggs [Default: 24]")
					.defineInRange("endermanEggsTries", 24, 1, Integer.MAX_VALUE);

			skeletonEggsTries = builder
					.comment("The amount of times it will try to generate Skeleton Eggs [Default: 24]")
					.defineInRange("skeletonEggsTries", 24, 1, Integer.MAX_VALUE);

			spiderEggsTries = builder
					.comment("The amount of times it will try to generate Spider Eggs [Default: 24]")
					.defineInRange("spiderEggsTries", 24, 1, Integer.MAX_VALUE);

			zombieEggsTries = builder
					.comment("The amount of times it will try to generate Zombie Eggs [Default: 24]")
					.defineInRange("zombieEggsTries", 24, 1, Integer.MAX_VALUE);
			
			builder.pop();
		}
	}

	public static final ForgeConfigSpec commonSpec;
	public static final Common COMMON;

	static {
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		commonSpec = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	@SubscribeEvent
	public static void onLoad(final ModConfigEvent.Loading configEvent) {
		MonsterEggs.LOGGER.debug("Loaded Monster Eggs's config file {}", configEvent.getConfig().getFileName());
	}

	@SubscribeEvent
	public static void onFileChange(final ModConfigEvent.Reloading configEvent) {
		MonsterEggs.LOGGER.debug("Monster Eggs's config just got changed on the file system!");
	}
}
