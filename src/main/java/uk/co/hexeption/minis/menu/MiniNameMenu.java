package uk.co.hexeption.minis.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import uk.co.hexeption.minis.entity.MiniEntity;
import uk.co.hexeption.minis.init.ModMenus;

/**
 * MiniNameMenu
 *
 * Open while the owner names a newly hatched Mini. It has no slots, the screen only sends the name.
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 23/10/2026
 */
public class MiniNameMenu extends AbstractContainerMenu implements MiniHolder {

	@Nullable
	private final MiniEntity mini;

	public MiniNameMenu(int containerId, Inventory playerInventory) {
		this(containerId, playerInventory, null);
	}

	public MiniNameMenu(int containerId, Inventory playerInventory, @Nullable MiniEntity mini) {
		super(ModMenus.MINI_NAME.get(), containerId);
		this.mini = mini;
		if (mini != null) {
			mini.startViewing();
		}
	}

	@Override
	@Nullable
	public MiniEntity getMini() {
		return this.mini;
	}

	@Override
	public void removed(Player player) {
		super.removed(player);
		if (this.mini != null) {
			this.mini.stopViewing();
		}
	}

	@Override
	public boolean stillValid(Player player) {
		return this.mini == null || (this.mini.isAlive() && this.mini.isOwnedBy(player) && this.mini.distanceToSqr(player) < 64.0D);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		return ItemStack.EMPTY;
	}
}
