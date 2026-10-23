package uk.co.hexeption.minis.entity;

import java.util.UUID;
import net.minecraft.resources.Identifier;
//? if <1.21
/*import java.nio.charset.StandardCharsets;*/
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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.food.FoodProperties;
//? if >=1.20.5
import net.minecraft.core.component.DataComponents;
import uk.co.hexeption.minis.item.MiniItem;
import uk.co.hexeption.minis.menu.MiniMenu;
import uk.co.hexeption.minis.menu.MiniNameMenu;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.Vec3;
import uk.co.hexeption.minis.Minis;
import uk.co.hexeption.minis.entity.ai.MiniDefendOwnerGoal;
import uk.co.hexeption.minis.item.UpgradeItem;
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
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
//?}

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
	public static final int UPGRADE_SLOTS = 4;
	private static final double MAGNET_RANGE = 6.0D;
	//? if >=1.20.5 {
	private static final AttributeModifier.Operation ADD = AttributeModifier.Operation.ADD_VALUE;
	private static final AttributeModifier.Operation MULTIPLY = AttributeModifier.Operation.ADD_MULTIPLIED_BASE;
	//?} else {
	/*private static final AttributeModifier.Operation ADD = AttributeModifier.Operation.ADDITION;
	private static final AttributeModifier.Operation MULTIPLY = AttributeModifier.Operation.MULTIPLY_BASE;
	*///?}
	public static final EquipmentSlot[] EQUIPMENT_SLOTS = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND};

	private final SimpleContainer inventory = new SimpleContainer(INVENTORY_SIZE);
	private final SimpleContainer upgrades = new SimpleContainer(UPGRADE_SLOTS) {
		@Override
		public void setChanged() {
			super.setChanged();
			if (!MiniEntity.this.loadingUpgrades) {
				MiniEntity.this.applyUpgrades();
			}
		}
	};
	// How many players have one of the Mini's menus open. It stands still for them, so it can't walk out of reach
	private int viewers;
	// Loading puts the upgrades back one at a time, and applying them part way would cut the Mini's health
	private boolean loadingUpgrades;

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
				.add(Attributes.MOVEMENT_SPEED, 0.3D)
				.add(Attributes.ATTACK_DAMAGE, 2.0D);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(2, new MiniSitGoal(this));
		this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.2D, true) {
			@Override
			public boolean canUse() {
				return canFight() && super.canUse();
			}

			@Override
			public boolean canContinueToUse() {
				return canFight() && super.canContinueToUse();
			}
		});
		this.goalSelector.addGoal(4, new MiniFollowOwnerGoal(this, 1.2D, 4.0D, 2.0D));
		this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1D) {
			@Override
			public boolean canUse() {
				return getMode() == MiniMode.WANDER && super.canUse();
			}

			@Override
			public boolean canContinueToUse() {
				return getMode() == MiniMode.WANDER && super.canContinueToUse();
			}
		});
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

		// Fights back, but never against its owner
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this) {
			@Override
			public boolean canUse() {
				return canFight() && getLastHurtByMob() != getOwner() && super.canUse();
			}
		});
		this.targetSelector.addGoal(2, new MiniDefendOwnerGoal(this, true));
		this.targetSelector.addGoal(3, new MiniDefendOwnerGoal(this, false));
	}

	public boolean canFight() {
		return this.hasUpgrade(MiniUpgrade.COMBAT) && !this.isSitting();
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
		NonNullList<ItemStack> items = NonNullList.withSize(INVENTORY_SIZE + UPGRADE_SLOTS, ItemStack.EMPTY);
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
		NonNullList<ItemStack> items = NonNullList.withSize(INVENTORY_SIZE + UPGRADE_SLOTS, ItemStack.EMPTY);
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

	// Food heals a hurt Mini by as much as it would fill a player's hunger
	public boolean feed(ItemStack stack) {
		int nutrition = nutrition(stack);
		if (nutrition <= 0 || this.getHealth() >= this.getMaxHealth()) {
			return false;
		}

		this.heal(nutrition);
		return true;
	}

	private static int nutrition(ItemStack stack) {
		//? if >=1.20.5 {
		FoodProperties food = stack.get(DataComponents.FOOD);
		return food == null ? 0 : food.nutrition();
		//?} else {
		/*FoodProperties food = stack.getItem().getFoodProperties();
		return food == null ? 0 : food.getNutrition();
		*///?}
	}

	// Everything about the Mini except where it is, for storing in a MiniItem
	public CompoundTag saveForItem() {
		//? if >=1.21.6 {
		TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, this.registryAccess());
		this.saveWithoutId(output);
		CompoundTag tag = output.buildResult();
		//?} else
		/*CompoundTag tag = this.saveWithoutId(new CompoundTag());*/
		tag.remove("UUID");
		tag.remove("Pos");
		tag.remove("Motion");
		return tag;
	}

	public void loadFromItem(CompoundTag tag) {
		//? if >=1.21.6 {
		this.load(TagValueInput.create(ProblemReporter.DISCARDING, this.registryAccess(), tag));
		//?} else
		/*this.load(tag);*/
	}

	public SimpleContainer getInventory() {
		return this.inventory;
	}

	public SimpleContainer getUpgrades() {
		return this.upgrades;
	}

	public boolean hasUpgrade(MiniUpgrade upgrade) {
		for (int i = 0; i < UPGRADE_SLOTS; i++) {
			if (this.upgrades.getItem(i).getItem() instanceof UpgradeItem item && item.getUpgrade() == upgrade) {
				return true;
			}
		}
		return false;
	}

	// Speed and health come from attribute modifiers, which are saved with the Mini so its health survives a reload
	private void applyUpgrades() {
		if (this.level().isClientSide()) {
			return;
		}

		this.setModifier(this.getAttribute(Attributes.MOVEMENT_SPEED), MiniUpgrade.SPEED, 0.5D, MULTIPLY);
		this.setModifier(this.getAttribute(Attributes.MAX_HEALTH), MiniUpgrade.HEALTH, 10.0D, ADD);
		if (this.getHealth() > this.getMaxHealth()) {
			this.setHealth(this.getMaxHealth());
		}
	}

	private void setModifier(@Nullable AttributeInstance attribute, MiniUpgrade upgrade, double amount, AttributeModifier.Operation operation) {
		if (attribute == null) {
			return;
		}

		//? if >=1.21 {
		Identifier id = Minis.id(upgrade.id());
		attribute.removeModifier(id);
		if (this.hasUpgrade(upgrade)) {
			attribute.addPermanentModifier(new AttributeModifier(id, amount, operation));
		}
		//?} else {
		/*UUID id = UUID.nameUUIDFromBytes(Minis.id(upgrade.id()).toString().getBytes(StandardCharsets.UTF_8));
		attribute.removeModifier(id);
		if (this.hasUpgrade(upgrade)) {
			attribute.addPermanentModifier(new AttributeModifier(id, Minis.MODID + " " + upgrade.id(), amount, operation));
		}
		*///?}
	}

	// Pulls dropped items in, then picks them up once they're close enough
	private void collectItems() {
		for (ItemEntity item : this.level().getEntitiesOfClass(ItemEntity.class, this.getBoundingBox().inflate(MAGNET_RANGE), found -> found.isAlive() && !found.hasPickUpDelay())) {
			if (this.distanceToSqr(item) > 1.0D) {
				Vec3 pull = this.position().subtract(item.position()).normalize().scale(0.3D);
				item.setDeltaMovement(pull);
				continue;
			}

			ItemStack stack = item.getItem();
			ItemStack left = this.inventory.addItem(stack.copy());
			int taken = stack.getCount() - left.getCount();
			if (taken > 0) {
				this.take(item, taken);
				if (left.isEmpty()) {
					item.discard();
				} else {
					item.setItem(left);
				}
			}
		}
	}

	// The inventory and the upgrades are saved as one list, each item with its slot, so nothing moves around when the world loads
	private NonNullList<ItemStack> inventoryItems() {
		NonNullList<ItemStack> items = NonNullList.withSize(INVENTORY_SIZE + UPGRADE_SLOTS, ItemStack.EMPTY);
		for (int i = 0; i < INVENTORY_SIZE; i++) {
			items.set(i, this.inventory.getItem(i));
		}
		for (int i = 0; i < UPGRADE_SLOTS; i++) {
			items.set(INVENTORY_SIZE + i, this.upgrades.getItem(i));
		}
		return items;
	}

	private void setInventoryItems(NonNullList<ItemStack> items) {
		for (int i = 0; i < INVENTORY_SIZE; i++) {
			this.inventory.setItem(i, items.get(i));
		}
		this.loadingUpgrades = true;
		for (int i = 0; i < UPGRADE_SLOTS; i++) {
			this.upgrades.setItem(i, items.get(INVENTORY_SIZE + i));
		}
		this.loadingUpgrades = false;
		this.applyUpgrades();
	}

	// Right click opens the Mini's screen for its owner, or feeds it when they're holding food. Sneaking picks it up.
	// A Mini without an owner belongs to whoever clicks it first.
	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		if (this.getOwnerId() != null && !this.isOwnedBy(player)) {
			return InteractionResult.PASS;
		}

		if (!this.level().isClientSide()) {
			if (this.getOwnerId() == null) {
				this.setOwnerId(player.getUUID());
			}
			ItemStack held = player.getItemInHand(hand);
			if (!player.isShiftKeyDown() && this.feed(held)) {
				if (!player.isCreative()) {
					held.shrink(1);
				}
				return InteractionResult.SUCCESS;
			}
			if (player.isShiftKeyDown()) {
				ItemStack stack = MiniItem.of(this);
				if (!player.getInventory().add(stack)) {
					Containers.dropItemStack(this.level(), player.getX(), player.getY(), player.getZ(), stack);
				}
				this.discard();
				return InteractionResult.SUCCESS;
			}
			this.openMenu(player);
		}
		return InteractionResult.SUCCESS;
	}

	public void startViewing() {
		this.viewers++;
		this.getNavigation().stop();
	}

	public void stopViewing() {
		this.viewers = Math.max(0, this.viewers - 1);
	}

	public void openMenu(Player player) {
		player.openMenu(new SimpleMenuProvider((containerId, playerInventory, opener) -> new MiniMenu(containerId, playerInventory, this), this.getDisplayName()));
	}

	// Asks a newly hatched Mini's owner what to call it, before showing the Mini's screen
	public void openNameMenu(Player player) {
		player.openMenu(new SimpleMenuProvider((containerId, playerInventory, opener) -> new MiniNameMenu(containerId, playerInventory, this), this.getDisplayName()));
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
		Containers.dropContents(this.level(), this, this.upgrades);
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return false;
	}

	@Override
	public void tick() {
		super.tick();
		this.moveCloak();
		if (!this.level().isClientSide() && this.hasUpgrade(MiniUpgrade.MAGNET)) {
			this.collectItems();
		}
		if (this.viewers > 0) {
			this.getNavigation().stop();
		}
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
