package com.mrbysco.monstereggs.config;

import com.mrbysco.monstereggs.MonsterEggs;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

public class EggConfig {

	public static class Common {
		public final BooleanValue debugInfo;

		public final DoubleValue spawnOffset;

		Common(ForgeConfigSpec.Builder builder) {
			builder.comment("General settings")
					.push("General");

			spawnOffset = builder
					.comment("dictates the Y offset of the mob spawned from the egg [Default: 0.5]")
					.defineInRange("spawnOffset", 0.5, Integer.MIN_VALUE, Integer.MAX_VALUE);

			builder.pop();

			builder.comment("Debug settings")
					.push("Debug");

			debugInfo = builder
					.comment("Show the mob in the tooltip of the shell [Default: false]")
					.define("debugInfo", false);

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
