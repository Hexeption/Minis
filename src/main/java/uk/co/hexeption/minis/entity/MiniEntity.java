package uk.co.hexeption.minis.entity;

import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.core.NonNullList;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import uk.co.hexeption.minis.menu.MiniMenu;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import uk.co.hexeption.minis.entity.ai.MiniFollowOwnerGoal;
import uk.co.hexeption.minis.entity.ai.MiniSitGoal;
import uk.co.hexeption.minis.util.MiniProfiles;

//? if >=1.21.6 {
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
//?} else {
/*import net.minecraft.nbt.CompoundTag;
*///?}

/**
 * MiniEntity
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 13/04/2021 - 03:50 pm
 */
public class MiniEntity extends PathfinderMob {

	// The owner's UUID as a string, "" when it has none
	protected static final EntityDataAccessor<String> OWNER = SynchedEntityData.defineId(MiniEntity.class, EntityDataSerializers.STRING);
	protected static final EntityDataAccessor<Integer> MODE = SynchedEntityData.defineId(MiniEntity.class, EntityDataSerializers.INT);
	// Whose skin the Mini wears, "" for its owner's
	protected static final EntityDataAccessor<String> SKIN = SynchedEntityData.defineId(MiniEntity.class, EntityDataSerializers.STRING);

	public static final int INVENTORY_SIZE = 9;
	public static final EquipmentSlot[] EQUIPMENT_SLOTS = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND};

	private final SimpleContainer inventory = new SimpleContainer(INVENTORY_SIZE);

	public double xCloakO;
	public double yCloakO;
	public double zCloakO;
	public double xCloak;
	public double yCloak;
	public double zCloak;

	public MiniEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
		super(type, worldIn);
	}

	public static AttributeSupplier.Builder setCustomAttributes() {
		return MiniEntity.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 10.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.3D);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(2, new MiniSitGoal(this));
		this.goalSelector.addGoal(3, new MiniFollowOwnerGoal(this, 1.2D, 4.0D, 2.0D));
		this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1D) {
			@Override
			public boolean canUse() {
				return getMode() == MiniMode.WANDER && super.canUse();
			}

			@Override
			public boolean canContinueToUse() {
				return getMode() == MiniMode.WANDER && super.canContinueToUse();
			}
		});
		this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
		this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
	}

	//? if >=1.20.5 {
	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(OWNER, "");
		builder.define(MODE, MiniMode.WANDER.ordinal());
		builder.define(SKIN, "");
	}
	//?} else {
	/*@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(OWNER, "");
		this.entityData.define(MODE, MiniMode.WANDER.ordinal());
		this.entityData.define(SKIN, "");
	}
	*///?}

	//? if >=1.21.6 {
	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
		super.addAdditionalSaveData(output);
		output.storeNullable("Owner", UUIDUtil.CODEC, this.getOwnerId());
		output.putString("Mode", this.getMode().name());
		output.storeNullable("Skin", UUIDUtil.CODEC, this.getSkinOverride());
		ContainerHelper.saveAllItems(output, this.inventoryItems());
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
		super.readAdditionalSaveData(input);
		input.read("Owner", UUIDUtil.CODEC).ifPresent(this::setOwnerId);
		this.setMode(MiniMode.byName(input.getStringOr("Mode", MiniMode.WANDER.name())));
		this.setSkin(input.read("Skin", UUIDUtil.CODEC).orElse(null));
		NonNullList<ItemStack> items = NonNullList.withSize(INVENTORY_SIZE, ItemStack.EMPTY);
		ContainerHelper.loadAllItems(input, items);
		this.setInventoryItems(items);
	}
	//?} else {
	/*@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		if (this.getOwnerId() != null) {
			compound.putUUID("Owner", this.getOwnerId());
		}
		compound.putString("Mode", this.getMode().name());
		if (this.getSkinOverride() != null) {
			compound.putUUID("Skin", this.getSkinOverride());
		}
		//? if >=1.20.5 {
		ContainerHelper.saveAllItems(compound, this.inventoryItems(), this.registryAccess());
		//?} else
		/^ContainerHelper.saveAllItems(compound, this.inventoryItems());^/
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.hasUUID("Owner")) {
			this.setOwnerId(compound.getUUID("Owner"));
		}
		this.setMode(MiniMode.byName(compound.getString("Mode")));
		this.setSkin(compound.hasUUID("Skin") ? compound.getUUID("Skin") : null);
		NonNullList<ItemStack> items = NonNullList.withSize(INVENTORY_SIZE, ItemStack.EMPTY);
		//? if >=1.20.5 {
		ContainerHelper.loadAllItems(compound, items, this.registryAccess());
		//?} else
		/^ContainerHelper.loadAllItems(compound, items);^/
		this.setInventoryItems(items);
	}
	*///?}

	@Nullable
	public UUID getOwnerId() {
		String owner = this.entityData.get(OWNER);
		return owner.isEmpty() ? null : UUID.fromString(owner);
	}

	public void setOwnerId(@Nullable UUID uuid) {
		this.entityData.set(OWNER, uuid == null ? "" : uuid.toString());

		if (!this.hasCustomName()) {
			this.nameAfterOwner();
		}
	}

	// Names the Mini after its owner once the lookup comes back, unless it's been given a name since
	public void nameAfterOwner() {
		UUID owner = this.getOwnerId();
		if (owner != null && this.level() instanceof ServerLevel serverLevel) {
			MiniProfiles.get(owner).thenAccept(profile -> profile.ifPresent(found -> serverLevel.getServer().execute(() -> {
				if (!this.hasCustomName()) {
					this.setCustomName(Component.literal(found.name()));
				}
			})));
		}
	}

	@Nullable
	public Player getOwner() {
		UUID owner = this.getOwnerId();
		return owner == null ? null : this.level().getPlayerByUUID(owner);
	}

	public boolean isOwnedBy(Player player) {
		return player.getUUID().equals(this.getOwnerId());
	}

	// The player whose skin the Mini wears
	@Nullable
	public UUID getSkinId() {
		UUID skin = this.getSkinOverride();
		return skin != null ? skin : this.getOwnerId();
	}

	@Nullable
	private UUID getSkinOverride() {
		String skin = this.entityData.get(SKIN);
		return skin.isEmpty() ? null : UUID.fromString(skin);
	}

	public void setSkin(@Nullable UUID skin) {
		this.entityData.set(SKIN, skin == null ? "" : skin.toString());
	}

	// Wears the skin of the player with this name, or the owner's if there's no such player
	public void useSkinOf(String name) {
		if (this.level() instanceof ServerLevel serverLevel) {
			MiniProfiles.idOf(name).thenAccept(id -> serverLevel.getServer().execute(() -> this.setSkin(id.orElse(null))));
		}
	}

	public MiniMode getMode() {
		return MiniMode.byId(this.entityData.get(MODE));
	}

	public void setMode(MiniMode mode) {
		this.entityData.set(MODE, mode.ordinal());
		if (mode == MiniMode.SIT) {
			this.getNavigation().stop();
		}
	}

	public boolean isSitting() {
		return this.getMode() == MiniMode.SIT;
	}

	public SimpleContainer getInventory() {
		return this.inventory;
	}

	// Saved with each item's slot, so nothing moves around when the world loads
	private NonNullList<ItemStack> inventoryItems() {
		NonNullList<ItemStack> items = NonNullList.withSize(INVENTORY_SIZE, ItemStack.EMPTY);
		for (int i = 0; i < INVENTORY_SIZE; i++) {
			items.set(i, this.inventory.getItem(i));
		}
		return items;
	}

	private void setInventoryItems(NonNullList<ItemStack> items) {
		for (int i = 0; i < INVENTORY_SIZE; i++) {
			this.inventory.setItem(i, items.get(i));
		}
	}

	// Right click opens the Mini's screen for its owner. A Mini without an owner belongs to whoever clicks it first
	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		if (this.getOwnerId() != null && !this.isOwnedBy(player)) {
			return InteractionResult.PASS;
		}

		if (!this.level().isClientSide()) {
			if (this.getOwnerId() == null) {
				this.setOwnerId(player.getUUID());
			}
			player.openMenu(new SimpleMenuProvider((containerId, playerInventory, opener) -> new MiniMenu(containerId, playerInventory, this), this.getDisplayName()));
		}
		return InteractionResult.SUCCESS;
	}

	// Everything given to a Mini drops, unlike a normal mob's gear
	//? if >=1.21.2 {
	@Override
	protected void dropEquipment(ServerLevel level) {
		super.dropEquipment(level);
	//?} else {
	/*@Override
	protected void dropEquipment() {
		super.dropEquipment();
	*///?}
		for (EquipmentSlot slot : EQUIPMENT_SLOTS) {
			ItemStack stack = this.getItemBySlot(slot);
			if (!stack.isEmpty()) {
				Containers.dropItemStack(this.level(), this.getX(), this.getY(), this.getZ(), stack);
				this.setItemSlot(slot, ItemStack.EMPTY);
			}
		}
		Containers.dropContents(this.level(), this, this.inventory);
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return false;
	}

	@Override
	public void tick() {
		super.tick();
		this.moveCloak();
	}

	private void moveCloak() {
		this.xCloakO = this.xCloak;
		this.yCloakO = this.yCloak;
		this.zCloakO = this.zCloak;
		double d0 = this.getX() - this.xCloak;
		double d1 = this.getY() - this.yCloak;
		double d2 = this.getZ() - this.zCloak;
		if (d0 > 10.0) {
			this.xCloak = this.getX();
			this.xCloakO = this.xCloak;
		}

		if (d2 > 10.0) {
			this.zCloak = this.getZ();
			this.zCloakO = this.zCloak;
		}

		if (d1 > 10.0) {
			this.yCloak = this.getY();
			this.yCloakO = this.yCloak;
		}

		if (d0 < -10.0) {
			this.xCloak = this.getX();
			this.xCloakO = this.xCloak;
		}

		if (d2 < -10.0) {
			this.zCloak = this.getZ();
			this.zCloakO = this.zCloak;
		}

		if (d1 < -10.0) {
			this.yCloak = this.getY();
			this.yCloakO = this.yCloak;
		}

		this.xCloak += d0 * 0.25;
		this.zCloak += d2 * 0.25;
		this.yCloak += d1 * 0.25;
	}
}
