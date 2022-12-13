package com.mrbysco.monstereggs;

import com.mrbysco.monstereggs.config.EggConfig;
import com.mrbysco.monstereggs.registry.EggModifiers;
import com.mrbysco.monstereggs.registry.EggRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Mod(MonsterEggs.MOD_ID)
public class MonsterEggs {
	public static final String MOD_ID = "monstereggs";
	public static final Logger LOGGER = LogManager.getLogger();

	public MonsterEggs() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext.get().registerConfig(Type.COMMON, EggConfig.commonSpec);
		eventBus.register(EggConfig.class);

		eventBus.addListener(this::registerCreativeTabs);

		EggRegistry.BLOCKS.register(eventBus);
		EggRegistry.ITEMS.register(eventBus);
		EggRegistry.SOUND_EVENTS.register(eventBus);
		EggModifiers.BIOME_MODIFIER_SERIALIZERS.register(eventBus);
	}

	private static CreativeModeTab TAB_EGGS;

	private void registerCreativeTabs(final CreativeModeTabEvent.Register event) {
		TAB_EGGS = event.registerCreativeModeTab(new ResourceLocation(MOD_ID, "eggs"), builder ->
				builder.icon(() -> new ItemStack(EggRegistry.CREEPER_EGG.get()))
						.title(Component.translatable("itemGroup.monstereggs"))
						.displayItems((features, output, hasPermissions) -> {
							List<ItemStack> stacks = EggRegistry.BLOCKS.getEntries().stream().map(reg -> new ItemStack(reg.get())).toList();
							output.acceptAll(stacks);
						}));
	}
}
