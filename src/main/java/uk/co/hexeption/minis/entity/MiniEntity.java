package uk.co.hexeption.minis.entity;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.properties.Property;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import uk.co.hexeption.minis.Minis;
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
public class MiniEntity extends PathfinderMob {

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
			if(map != null){
				return map.texture();
			}else {
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





}
