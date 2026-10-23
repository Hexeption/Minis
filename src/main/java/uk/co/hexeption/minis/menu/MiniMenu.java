package uk.co.hexeption.minis.menu;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import uk.co.hexeption.minis.entity.MiniEntity;
import uk.co.hexeption.minis.entity.MiniMode;
import uk.co.hexeption.minis.init.ModMenus;
import uk.co.hexeption.minis.item.UpgradeItem;

//? if <1.21
/*import net.minecraft.world.entity.LivingEntity;*/
//? if <1.21.2
/*import com.mojang.datafixers.util.Pair;*/

/**
 * MiniMenu
 *
 * The client side builds this without the Mini, and learns which Mini it is from the entity id data slots.
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 23/10/2026
 */
public class MiniMenu extends AbstractContainerMenu implements MiniHolder {

	//? if >=1.21.2 {
	private static final Identifier SWORD_ICON = Identifier.withDefaultNamespace("container/slot/sword");
	//?} else if >=1.21 {
	/*private static final Identifier SWORD_ICON = Identifier.withDefaultNamespace("item/empty_slot_sword");
	*///?} else
	/*private static final Identifier SWORD_ICON = new Identifier("item/empty_slot_sword");*/

	private static final int EQUIPMENT_END = MiniEntity.EQUIPMENT_SLOTS.length;
	private static final int INVENTORY_END = EQUIPMENT_END + MiniEntity.INVENTORY_SIZE;
	private static final int UPGRADES_END = INVENTORY_END + MiniEntity.UPGRADE_SLOTS;
	private static final int PLAYER_END = UPGRADES_END + 36;

	@Nullable
	private final MiniEntity mini;
	// Data slots only carry 16 bits, so the entity id is split in two
	private final DataSlot entityIdLow = DataSlot.standalone();
	private final DataSlot entityIdHigh = DataSlot.standalone();
	private final DataSlot mode = DataSlot.standalone();

	public MiniMenu(int containerId, Inventory playerInventory) {
		this(containerId, playerInventory, null, new SimpleContainer(MiniEntity.EQUIPMENT_SLOTS.length), new SimpleContainer(MiniEntity.INVENTORY_SIZE), new SimpleContainer(MiniEntity.UPGRADE_SLOTS));
	}

	public MiniMenu(int containerId, Inventory playerInventory, MiniEntity mini) {
		this(containerId, playerInventory, mini, new EquipmentContainer(mini), mini.getInventory(), mini.getUpgrades());
	}

	private MiniMenu(int containerId, Inventory playerInventory, @Nullable MiniEntity mini, Container equipment, Container inventory, Container upgrades) {
		super(ModMenus.MINI.get(), containerId);
		this.mini = mini;
		if (mini != null) {
			mini.startViewing();
		}

		Identifier[] armorIcons = {InventoryMenu.EMPTY_ARMOR_SLOT_HELMET, InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE, InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS, InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS};
		for (int i = 0; i < 4; i++) {
			this.addSlot(new ArmorSlot(equipment, i, 8, 24 + i * 18, MiniEntity.EQUIPMENT_SLOTS[i], armorIcons[i]));
		}
		this.addSlot(new IconSlot(equipment, 4, 80, 60, SWORD_ICON));
		this.addSlot(new IconSlot(equipment, 5, 80, 78, InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD));

		for (int i = 0; i < MiniEntity.INVENTORY_SIZE; i++) {
			this.addSlot(new Slot(inventory, i, 8 + i * 18, 126));
		}
		for (int i = 0; i < MiniEntity.UPGRADE_SLOTS; i++) {
			this.addSlot(new UpgradeSlot(upgrades, i, 98 + i * 18, 84));
		}

		for (int row = 0; row < 3; row++) {
			for (int column = 0; column < 9; column++) {
				this.addSlot(new Slot(playerInventory, column + row * 9 + 9, 8 + column * 18, 158 + row * 18));
			}
		}
		for (int column = 0; column < 9; column++) {
			this.addSlot(new Slot(playerInventory, column, 8 + column * 18, 216));
		}

		if (mini != null) {
			this.entityIdLow.set(mini.getId() & 0xFFFF);
			this.entityIdHigh.set(mini.getId() >>> 16);
			this.mode.set(mini.getMode().ordinal());
		}
		this.addDataSlot(this.entityIdLow);
		this.addDataSlot(this.entityIdHigh);
		this.addDataSlot(this.mode);
	}

	@Override
	@Nullable
	public MiniEntity getMini() {
		return this.mini;
	}

	public int getMiniId() {
		return (this.entityIdLow.get() & 0xFFFF) | (this.entityIdHigh.get() << 16);
	}

	public MiniMode getMode() {
		return MiniMode.byId(this.mode.get());
	}

	// The mode buttons send their mode's id through the vanilla menu button packet
	@Override
	public boolean clickMenuButton(Player player, int id) {
		if (this.mini == null || id < 0 || id >= MiniMode.values().length) {
			return false;
		}

		this.mini.setMode(MiniMode.byId(id));
		this.mode.set(id);
		return true;
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
		Slot slot = this.slots.get(index);
		if (!slot.hasItem()) {
			return ItemStack.EMPTY;
		}

		ItemStack stack = slot.getItem();
		ItemStack original = stack.copy();
		if (index < UPGRADES_END) {
			if (!this.moveItemStackTo(stack, UPGRADES_END, PLAYER_END, true)) {
				return ItemStack.EMPTY;
			}
		} else if (!this.moveItemStackTo(stack, INVENTORY_END, UPGRADES_END, false)
				&& !this.moveItemStackTo(stack, 0, 4, false)
				&& !this.moveItemStackTo(stack, EQUIPMENT_END, INVENTORY_END, false)) {
			return ItemStack.EMPTY;
		}

		if (stack.isEmpty()) {
			slot.set(ItemStack.EMPTY);
		} else {
			slot.setChanged();
		}
		return original;
	}

	private static EquipmentSlot equipmentSlotFor(Entity mini, ItemStack stack) {
		//? if >=1.21 {
		return mini instanceof MiniEntity living ? living.getEquipmentSlotForItem(stack) : EquipmentSlot.MAINHAND;
		//?} else
		/*return LivingEntity.getEquipmentSlotForItem(stack);*/
	}

	private static class IconSlot extends Slot {
		private final Identifier icon;

		IconSlot(Container container, int index, int x, int y, Identifier icon) {
			super(container, index, x, y);
			this.icon = icon;
		}

		//? if >=1.21.2 {
		@Override
		public Identifier getNoItemIcon() {
			return this.icon;
		}
		//?} else {
		/*@Override
		public Pair<Identifier, Identifier> getNoItemIcon() {
			return Pair.of(InventoryMenu.BLOCK_ATLAS, this.icon);
		}
		*///?}
	}

	private class ArmorSlot extends IconSlot {
		private final EquipmentSlot equipmentSlot;

		ArmorSlot(Container container, int index, int x, int y, EquipmentSlot equipmentSlot, Identifier icon) {
			super(container, index, x, y, icon);
			this.equipmentSlot = equipmentSlot;
		}

		@Override
		public boolean mayPlace(ItemStack stack) {
			return MiniMenu.this.mini == null || equipmentSlotFor(MiniMenu.this.mini, stack) == this.equipmentSlot;
		}

		@Override
		public int getMaxStackSize() {
			return 1;
		}
	}

	// Takes one of each kind of upgrade
	private static class UpgradeSlot extends Slot {
		UpgradeSlot(Container container, int index, int x, int y) {
			super(container, index, x, y);
		}

		@Override
		public boolean mayPlace(ItemStack stack) {
			if (!(stack.getItem() instanceof UpgradeItem upgrade)) {
				return false;
			}
			for (int i = 0; i < this.container.getContainerSize(); i++) {
				if (i != this.getContainerSlot() && this.container.getItem(i).getItem() instanceof UpgradeItem other && other.getUpgrade() == upgrade.getUpgrade()) {
					return false;
				}
			}
			return true;
		}

		@Override
		public int getMaxStackSize() {
			return 1;
		}
	}

	// Lets menu slots read and write the Mini's armor and hands
	private static class EquipmentContainer implements Container {
		private final MiniEntity mini;

		EquipmentContainer(MiniEntity mini) {
			this.mini = mini;
		}

		@Override
		public int getContainerSize() {
			return MiniEntity.EQUIPMENT_SLOTS.length;
		}

		@Override
		public boolean isEmpty() {
			for (EquipmentSlot slot : MiniEntity.EQUIPMENT_SLOTS) {
				if (!this.mini.getItemBySlot(slot).isEmpty()) {
					return false;
				}
			}
			return true;
		}

		@Override
		public ItemStack getItem(int index) {
			return this.mini.getItemBySlot(MiniEntity.EQUIPMENT_SLOTS[index]);
		}

		@Override
		public ItemStack removeItem(int index, int count) {
			ItemStack stack = this.getItem(index);
			if (stack.isEmpty()) {
				return ItemStack.EMPTY;
			}
			ItemStack removed = stack.split(count);
			this.setItem(index, stack);
			return removed;
		}

		@Override
		public ItemStack removeItemNoUpdate(int index) {
			ItemStack stack = this.getItem(index);
			this.setItem(index, ItemStack.EMPTY);
			return stack;
		}

		@Override
		public void setItem(int index, ItemStack stack) {
			this.mini.setItemSlot(MiniEntity.EQUIPMENT_SLOTS[index], stack);
		}

		@Override
		public void setChanged() {
		}

		@Override
		public boolean stillValid(Player player) {
			return this.mini.isAlive();
		}

		@Override
		public void clearContent() {
			for (EquipmentSlot slot : MiniEntity.EQUIPMENT_SLOTS) {
				this.mini.setItemSlot(slot, ItemStack.EMPTY);
			}
		}
	}
}
