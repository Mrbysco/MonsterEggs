package com.mrbysco.monstereggs;

import com.mrbysco.monstereggs.config.EggConfig;
import com.mrbysco.monstereggs.registry.EggModifiers;
import com.mrbysco.monstereggs.registry.EggRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(MonsterEggs.MOD_ID)
public class MonsterEggs {
	public static final String MOD_ID = "monstereggs";
	public static final Logger LOGGER = LogManager.getLogger();

	public MonsterEggs(IEventBus eventBus) {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EggConfig.commonSpec);
		eventBus.register(EggConfig.class);

		EggRegistry.BLOCKS.register(eventBus);
		EggRegistry.ITEMS.register(eventBus);
		EggRegistry.CREATIVE_MODE_TABS.register(eventBus);
		EggRegistry.SOUND_EVENTS.register(eventBus);
		EggModifiers.BIOME_MODIFIER_SERIALIZERS.register(eventBus);
	}
}
