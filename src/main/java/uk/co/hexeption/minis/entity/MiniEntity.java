package uk.co.hexeption.minis.entity;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.properties.Property;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.world.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.items.ItemStackHandler;
import uk.co.hexeption.minis.Minis;
import uk.co.hexeption.minis.client.gui.MiniInventoryMenu;
import uk.co.hexeption.minis.util.SkinUtil;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * MiniEntity
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 13/04/2021 - 03:50 pm
 */
public class MiniEntity extends PathfinderMob implements ContainerListener, MenuProvider {

    protected static final EntityDataAccessor<Optional<UUID>> OWNER_UNIQUE_ID = SynchedEntityData.defineId(MiniEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    private ResolvableProfile owner;
    private String textureB64 = null;
    public double xCloakO;
    public double yCloakO;
    public double zCloakO;
    public double xCloak;
    public double yCloak;
    public double zCloak;
    public float oBob;
    public float bob;

    protected SimpleContainer inventory;

    public MiniEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
        super(type, worldIn);
        this.inventory = new SimpleContainer(5);
        this.inventory.addListener(this);

        syncArmor(inventory);
    }

    public void syncArmor(SimpleContainer inventory) {
        this.inventory.setItem(4, this.getItemBySlot(EquipmentSlot.HEAD));
        this.inventory.setItem(3, this.getItemBySlot(EquipmentSlot.CHEST));
        this.inventory.setItem(2, this.getItemBySlot(EquipmentSlot.LEGS));
        this.inventory.setItem(1, this.getItemBySlot(EquipmentSlot.FEET));
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return MiniEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {

        super.defineSynchedData(pBuilder);
        pBuilder.define(OWNER_UNIQUE_ID, Optional.empty());
    }

    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getSkinLocation() {
        if (getOwnerId() == null) {
            setOwnerId(Minecraft.getInstance().player.getUUID());
        }
        if (textureB64 == null) {
            textureB64 = SkinUtil.getHeadValue(getOwnerId());
        }
        if (textureB64.equals("nil")) {
            return DefaultPlayerSkin.get(getOwnerId()).texture();
        }

        GameProfile gameProfile = new GameProfile(getOwnerId(), "MiniEntity");
        gameProfile.getProperties().put("textures", new Property("textures", textureB64));
        if (gameProfile.getProperties().get("textures") != null) {
            final SkinManager manager = Minecraft.getInstance().getSkinManager();
            PlayerSkin map = manager.getInsecureSkin(gameProfile);
            if (map != null) {
                return map.texture();
            } else {
                UUID uuid = UUIDUtil.createOfflinePlayerUUID(gameProfile.getName());
                return DefaultPlayerSkin.get(uuid).texture();
            }
        } else {
            UUID uuid = UUIDUtil.createOfflinePlayerUUID(gameProfile.getName());
            return DefaultPlayerSkin.get(uuid).texture();
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.getOwnerId() != null) {
            compound.putUUID("Owner", this.getOwnerId());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        UUID uuid;
        if (compound.hasUUID("Owner")) {
            uuid = compound.getUUID("Owner");
        } else {
            String s = compound.getString("Owner");
            uuid = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }

        if (uuid != null) {
            try {
                this.setOwnerId(uuid);
            } catch (Throwable throwable) {
            }
        }
    }

    @Nullable
    public UUID getOwnerId() {
        return this.entityData.get(OWNER_UNIQUE_ID).orElse((UUID) null);
    }

    /**
     * Updates the skin and name of the MiniEntity based on the provided username.
     *
     * @param username The target username.
     */
    public void updateSkinAndName(String username) {
        if (username == null || username.isEmpty()) {
            Minis.LOGGER.warn("Provided username is null or empty.");
            return;
        }

        var profile = SkullBlockEntity.fetchGameProfile(username).thenAccept(gameProfile -> {
            if (gameProfile.isPresent()) {
                this.textureB64 = null;
                this.setOwnerId(gameProfile.get().getId());
                this.setCustomName(Component.literal(gameProfile.get().getName()));
            } else {
                Minis.LOGGER.warn("Failed to fetch game profile for username: " + username);
            }
        });
    }

    public void setOwnerId(@Nullable UUID uuid) {
        this.entityData.set(OWNER_UNIQUE_ID, Optional.ofNullable(uuid));
        SkullBlockEntity.fetchGameProfile(this.getOwnerId()).thenAccept(gameProfile -> {
            setCustomName(Component.literal(gameProfile.get().getName()));
        });
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }


    @OnlyIn(Dist.CLIENT)
    public PlayerSkin getSkin() {

        var profile = SkullBlockEntity.fetchGameProfile(this.getOwnerId()).getNow(null);
        if (profile != null) {
            var skin = Minecraft.getInstance().getSkinManager().getInsecureSkin(profile.get());
            if (skin != null) {
                return skin;
            }
        }

        return DefaultPlayerSkin.get(this.getOwnerId());

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
        double d3 = 10.0;
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


    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        var itemStack = player.getItemInHand(hand);

        if (itemStack.isEmpty()) {
            openSkinAndNameGui(player);
            return InteractionResult.SUCCESS;
        }


        return super.mobInteract(player, hand);
    }

    /**
     * Opens the GUI for changing the MiniEntity's skin or name.
     * Placeholder method to be implemented.
     */
    private void openSkinAndNameGui(Player player) {
        if (!player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
            player.openMenu(new SimpleMenuProvider((id, playerInventory, playerIn) -> new MiniInventoryMenu(id, playerInventory, this), Component.literal("Demo")), buf -> {
                buf.writeVarInt(this.getId());
                buf.writeVarInt(this.getId());
            });
        }
    }
    
    @Override
    public void containerChanged(Container container) {
        this.syncInventoryToFlags();
    }

    public void syncInventoryToFlags() {
        if (!this.level().isClientSide()) {
            for (int i = 0; i < this.inventory.getContainerSize(); ++i) {
                ItemStack itemstack = this.inventory.getItem(i);
                this.equipArmor(itemstack);
            }
        }
    }

    public boolean isValidArmor(ItemStack itemStack) {
        return itemStack.getItem() instanceof ArmorItem item;
    }

    public void equipArmor(ItemStack stack) {
        if (!this.level().isClientSide()) {
            if (this.isValidArmor(stack)) {
                this.setItemSlot(this.getEquipmentSlotForItem(stack), stack);
                this.setGuaranteedDrop(this.getEquipmentSlotForItem(stack));
            }
        }
    }
    
    public int getInventorySize() {
        return 5;
    }

    public SimpleContainer getInventory() {
        if (this.inventory == null) {
            return new SimpleContainer(getInventorySize());
        }

        return this.inventory;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new MiniInventoryMenu(containerId, playerInventory, this);
    }
}
