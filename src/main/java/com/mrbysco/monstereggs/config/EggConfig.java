package com.mrbysco.monstereggs.config;

import com.mrbysco.monstereggs.MonsterEggs;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

public class EggConfig {

	public static class Common {
		public final ModConfigSpec.BooleanValue debugInfo;

		public final ModConfigSpec.DoubleValue spawnOffset;

		Common(ModConfigSpec.Builder builder) {
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

	public static final ModConfigSpec commonSpec;
	public static final Common COMMON;

	static {
		final Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Common::new);
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
