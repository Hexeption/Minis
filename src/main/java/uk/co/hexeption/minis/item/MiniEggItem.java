package uk.co.hexeption.minis.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.item.context.UseOnContext;
import uk.co.hexeption.minis.entity.MiniEntity;
import uk.co.hexeption.minis.init.ModEntities;

//? if >=1.21.2
import net.minecraft.world.entity.EntitySpawnReason;

/**
 * MiniEggItem
 *
 * Hatches a new Mini for whoever uses it, then asks them what to call it.
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 23/10/2026
 */
public class MiniEggItem extends Item {

	public MiniEggItem(Properties properties) {
		super(properties);
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

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Player player = context.getPlayer();
		if (player == null || !(context.getLevel() instanceof ServerLevel level)) {
			return InteractionResult.SUCCESS;
		}

		BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
		//? if >=1.21.2 {
		MiniEntity mini = ModEntities.MINI_ENTITY.get().create(level, EntitySpawnReason.SPAWN_ITEM_USE);
		//?} else
		/*MiniEntity mini = ModEntities.MINI_ENTITY.get().create(level);*/
		if (mini == null) {
			return InteractionResult.FAIL;
		}

		float yaw = player.getYRot() + 180.0F;
		//? if >=1.21.5 {
		mini.snapTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, yaw, 0.0F);
		//?} else
		/*mini.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, yaw, 0.0F);*/
		mini.setYHeadRot(yaw);
		mini.setYBodyRot(yaw);
		mini.setOwnerId(player.getUUID());
		level.addFreshEntity(mini);

		if (!player.isCreative()) {
			context.getItemInHand().shrink(1);
		}
		mini.openNameMenu(player);
		return InteractionResult.SUCCESS;
	}
}
