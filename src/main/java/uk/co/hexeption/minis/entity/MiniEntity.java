package uk.co.hexeption.minis.entity;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.properties.Property;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import uk.co.hexeption.minis.util.SkinUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * MiniEntity
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 13/04/2021 - 03:50 pm
 */
public class MiniEntity extends CreatureEntity implements IEntityAdditionalSpawnData {

	protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.createKey(MiniEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);

	private String textureB64 = null;

	public MiniEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
		super(type, worldIn);
	}

	public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
		return MiniEntity.func_233666_p_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 10.0D)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3D);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new SwimGoal(this));
		this.goalSelector.addGoal(2, new RandomWalkingGoal(this, 1D));
		this.goalSelector.addGoal(3, new LookAtGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
	}



	@Override
	protected void registerData() {
		super.registerData();
		this.dataManager.register(OWNER_UNIQUE_ID, Optional.empty());
	}

	@OnlyIn(Dist.CLIENT)
	public ResourceLocation getSkinLocation() {
		if (getOwnerId() == null) {
			setOwnerId(UUID.fromString("33e602eb-2f7e-4a84-8606-aaa1ac4faa68"));
		}
		if (textureB64 == null) {
			textureB64 = SkinUtil.getHeadValue(getOwnerId());
		}
		if (textureB64.equals("nil")) {
			return DefaultPlayerSkin.getDefaultSkin(getOwnerId());
		}
		GameProfile gameProfile = new GameProfile(getOwnerId(), null);
		gameProfile.getProperties().put("textures", new Property("textures", textureB64));
		if (gameProfile.getProperties().get("textures") != null) {
			final SkinManager manager = Minecraft.getInstance().getSkinManager();
			Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = manager.loadSkinFromCache(gameProfile);
			if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
				final MinecraftProfileTexture skin = map.get(MinecraftProfileTexture.Type.SKIN);
				return manager.loadSkin(skin, MinecraftProfileTexture.Type.SKIN);
			} else {
				UUID uuid = PlayerEntity.getUUID(gameProfile);
				return DefaultPlayerSkin.getDefaultSkin(uuid);
			}
		} else {
			UUID uuid = PlayerEntity.getUUID(gameProfile);
			return DefaultPlayerSkin.getDefaultSkin(uuid);
		}
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		if (this.getOwnerId() != null) {
			compound.putUniqueId("Owner", this.getOwnerId());
		}

	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		UUID uuid;
		if (compound.hasUniqueId("Owner")) {
			uuid = compound.getUniqueId("Owner");
		} else {
			String s = compound.getString("Owner");
			uuid = PreYggdrasilConverter.convertMobOwnerIfNeeded(this.getServer(), s);
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
		return this.dataManager.get(OWNER_UNIQUE_ID).orElse((UUID) null);
	}

	public void setOwnerId(@Nullable UUID uuid) {
		this.dataManager.set(OWNER_UNIQUE_ID, Optional.ofNullable(uuid));
	}

	@Override
	public boolean canDespawn(double distanceToClosestPlayer) {
		return false;
	}

	@Override
	public void writeSpawnData(PacketBuffer buffer) {
	}

	@Override
	public void readSpawnData(PacketBuffer additionalData) {

	}
}
