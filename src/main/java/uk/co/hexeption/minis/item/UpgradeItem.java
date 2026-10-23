package uk.co.hexeption.minis.item;

import net.minecraft.world.item.Item;
//? if >=1.21.5 {
import java.util.function.Consumer;
import net.minecraft.world.item.component.TooltipDisplay;
//?} else
/*import java.util.List;*/
//? if <1.20.5
/*import net.minecraft.world.level.Level;*/
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import uk.co.hexeption.minis.entity.MiniUpgrade;

/**
 * UpgradeItem
 *
 * Goes in one of a Mini's upgrade slots.
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 23/10/2026
 */
public class UpgradeItem extends Item {

	private final MiniUpgrade upgrade;

	public UpgradeItem(MiniUpgrade upgrade, Properties properties) {
		super(properties.stacksTo(1));
		this.upgrade = upgrade;
	}

	public MiniUpgrade getUpgrade() {
		return this.upgrade;
	}

	//? if >=1.21.5 {
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
		tooltip.accept(Component.translatable(this.getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
	}
	//?} else if >=1.20.5 {
	/*@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.translatable(this.getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
	}
	*///?} else {
	/*@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.translatable(this.getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
	}
	*///?}
}
