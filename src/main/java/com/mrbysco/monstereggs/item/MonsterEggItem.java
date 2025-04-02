package com.mrbysco.monstereggs.item;

import com.mrbysco.monstereggs.block.MonsterEggBlock;
import com.mrbysco.monstereggs.config.EggConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class MonsterEggItem extends BlockItem {
	private final MonsterEggBlock eggBlock;

	public MonsterEggItem(MonsterEggBlock block, Properties properties) {
		super(block, properties);
		this.eggBlock = block;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
	                            Consumer<Component> components, TooltipFlag tooltipFlag) {
		if (EggConfig.COMMON.debugInfo.get()) {
			components.accept(Component.translatable(this.eggBlock.getType().getDescriptionId()).withStyle(ChatFormatting.RED));
		}
	}
}
