package uk.co.hexeption.minis.item;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.Nullable;
import uk.co.hexeption.minis.entity.MiniEntity;
import uk.co.hexeption.minis.init.ModEntities;
import uk.co.hexeption.minis.init.ModItems;

//? if >=1.20.5 {
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
//?}
//? if >=1.21.2
import net.minecraft.world.entity.EntitySpawnReason;

/**
 * MiniItem
 *
 * A picked up Mini. The item carries everything the Mini had, and using it on a block puts the Mini back.
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 23/10/2026
 */
public class MiniItem extends Item {

	private static final String NAME = "MiniName";

	public MiniItem(Properties properties) {
		super(properties.stacksTo(1));
	}

	public static ItemStack of(MiniEntity mini) {
		ItemStack stack = new ItemStack(ModItems.MINI.get());
		CompoundTag data = mini.saveForItem();
		data.putString(NAME, mini.getName().getString());
		//? if >=1.20.5 {
		CustomData.set(DataComponents.CUSTOM_DATA, stack, data);
		//?} else
		/*stack.getOrCreateTag().put("Mini", data);*/
		return stack;
	}

	@Nullable
	public static CompoundTag data(ItemStack stack) {
		//? if >=1.20.5 {
		CustomData data = stack.get(DataComponents.CUSTOM_DATA);
		return data == null ? null : data.copyTag();
		//?} else
		/*return stack.getTagElement("Mini");*/
	}

	@Override
	public Component getName(ItemStack stack) {
		CompoundTag data = data(stack);
		//? if >=1.21.5 {
		String name = data == null ? "" : data.getStringOr(NAME, "");
		//?} else
		/*String name = data == null ? "" : data.getString(NAME);*/
		return name.isEmpty() ? super.getName(stack) : Component.translatable("item.minis.mini.named", name);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		CompoundTag data = data(context.getItemInHand());
		if (data == null || !(context.getLevel() instanceof ServerLevel level)) {
			return data == null ? InteractionResult.PASS : InteractionResult.SUCCESS;
		}

		//? if >=1.21.2 {
		MiniEntity mini = ModEntities.MINI_ENTITY.get().create(level, EntitySpawnReason.BUCKET);
		//?} else
		/*MiniEntity mini = ModEntities.MINI_ENTITY.get().create(level);*/
		if (mini == null) {
			return InteractionResult.FAIL;
		}

		mini.loadFromItem(data);
		BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
		Player player = context.getPlayer();
		float yaw = player == null ? 0.0F : player.getYRot() + 180.0F;
		//? if >=1.21.5 {
		mini.snapTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, yaw, 0.0F);
		//?} else
		/*mini.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, yaw, 0.0F);*/
		mini.setYHeadRot(yaw);
		mini.setYBodyRot(yaw);
		level.addFreshEntity(mini);

		context.getItemInHand().shrink(1);
		return InteractionResult.SUCCESS;
	}
}
