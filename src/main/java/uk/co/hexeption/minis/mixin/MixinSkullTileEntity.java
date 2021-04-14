package uk.co.hexeption.minis.mixin;

import javax.annotation.Nullable;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.tileentity.SkullTileEntity;
import net.minecraft.util.StringUtils;

/**
 * MixinSkullTileEntity
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 13/04/2021 - 08:12 pm
 */
@Mixin(SkullTileEntity.class)
public class MixinSkullTileEntity {


	@Shadow
	@Nullable
	private static PlayerProfileCache profileCache;

	@Shadow
	@Nullable
	private static MinecraftSessionService sessionService;


	/**
	 * @author
	 */
	@Overwrite
	@Nullable
	public static GameProfile updateGameProfile(@Nullable GameProfile input) {
		if (input != null && !StringUtils.isNullOrEmpty(input.getName())) {
			if (input.isComplete() && input.getProperties().containsKey("textures")) {
				return input;
			} else if (profileCache != null && sessionService != null) {
				GameProfile gameprofile = profileCache.getGameProfileForUsername(input.getName());
				if (gameprofile == null) {
					return input;
				} else {
					Property property = Iterables.getFirst(gameprofile.getProperties().get("textures"), (Property) null);
					if (property == null) {
						gameprofile = sessionService.fillProfileProperties(gameprofile, true);
					}

					return gameprofile;
				}
			} else {
				return input;
			}
		} else {
			if (input != null && !input.getId().toString().isEmpty()) {
				if (input.isComplete() && input.getProperties().containsKey("textures")) {
					return input;
				} else if (profileCache != null && sessionService != null) {
					GameProfile gameprofile = profileCache.getProfileByUUID(input.getId());
					if (gameprofile == null) {
						return input;

					} else {
						Property property = Iterables.getFirst(gameprofile.getProperties().get("textures"), (Property) null);
						if (property == null) {
							gameprofile = sessionService.fillProfileProperties(gameprofile, true);
						}
						return gameprofile;

					}
				} else {
					return input;
				}
			} else {
				return input;
			}
		}
	}
}
