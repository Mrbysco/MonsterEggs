package com.mrbysco.monstereggs;

import com.mrbysco.monstereggs.registry.EggRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(MonsterEggs.MOD_ID)
public class MonsterEggs {
    public static final String MOD_ID = "monstereggs";
    public static final Logger LOGGER = LogManager.getLogger();

    public static final CreativeModeTab TAB_EGGS = new CreativeModeTab(MOD_ID) {
        public ItemStack makeIcon() {
            return new ItemStack(EggRegistry.CREEPER_EGG.get());
        }
    };

    public MonsterEggs() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        EggRegistry.BLOCKS.register(eventBus);
        EggRegistry.ITEMS.register(eventBus);
        EggRegistry.SOUND_EVENTS.register(eventBus);
    }
}
