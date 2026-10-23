package uk.co.hexeption.minis.client;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import org.jetbrains.annotations.Nullable;
import uk.co.hexeption.minis.entity.MiniEntity;
import uk.co.hexeption.minis.util.MiniProfiles;

//? if >=1.21.9 {
import com.google.common.collect.ImmutableMultimap;
import com.mojang.authlib.properties.PropertyMap;
import java.util.function.Supplier;
import net.minecraft.world.entity.player.PlayerSkin;
//?} else if >=1.20.2 {
/*import net.minecraft.client.resources.PlayerSkin;
*///?} else {
/*import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.resources.Identifier;
*///?}

/**
 * MiniSkins
 *
 * Turns a Mini's owner into the skin and cape it's drawn with.
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 23/10/2026
 */
public final class MiniSkins {

    private static final Map<UUID, GameProfile> PROFILES = new ConcurrentHashMap<>();
    //? if >=1.21.9 {
    private static final Map<UUID, Supplier<PlayerSkin>> LOOKUPS = new ConcurrentHashMap<>();
    //?} else if <1.20.2 {
    /*private static final Map<UUID, Map<MinecraftProfileTexture.Type, Identifier>> TEXTURES = new ConcurrentHashMap<>();
    *///?}

    private MiniSkins() {
    }

    // A Mini without an owner or skin looks like whoever is looking at it
    public static UUID owner(MiniEntity mini) {
        UUID skin = mini.getSkinId();
        if (skin != null) {
            return skin;
        }

        LocalPlayer player = Minecraft.getInstance().player;
        return player != null ? player.getUUID() : mini.getUUID();
    }

    //? if >=1.20.2 {
    public static PlayerSkin skin(MiniEntity mini) {
        UUID owner = owner(mini);
        GameProfile profile = profile(owner);
        if (profile == null) {
            return DefaultPlayerSkin.get(owner);
        }

        //? if >=1.21.9 {
        return LOOKUPS.computeIfAbsent(owner, id -> Minecraft.getInstance().getSkinManager().createLookup(profile, false)).get();
        //?} else
        /*return Minecraft.getInstance().getSkinManager().getInsecureSkin(profile);*/
    }
    //?} else {
    /*public static Identifier texture(MiniEntity mini) {
        UUID owner = owner(mini);
        Identifier texture = registered(owner, MinecraftProfileTexture.Type.SKIN);
        return texture != null ? texture : DefaultPlayerSkin.getDefaultSkin(owner);
    }

    @Nullable
    public static Identifier cape(MiniEntity mini) {
        return registered(owner(mini), MinecraftProfileTexture.Type.CAPE);
    }

    @Nullable
    private static Identifier registered(UUID owner, MinecraftProfileTexture.Type type) {
        GameProfile profile = profile(owner);
        if (profile == null) {
            return null;
        }

        return TEXTURES.computeIfAbsent(owner, id -> {
            SkinManager skinManager = Minecraft.getInstance().getSkinManager();
            Map<MinecraftProfileTexture.Type, Identifier> textures = new ConcurrentHashMap<>();
            skinManager.getInsecureSkinInformation(profile).forEach((textureType, texture) -> textures.put(textureType, skinManager.registerTexture(texture, textureType)));
            return textures;
        }).get(type);
    }
    *///?}

    // Null until the session server lookup has come back with textures
    @Nullable
    private static GameProfile profile(UUID owner) {
        GameProfile cached = PROFILES.get(owner);
        if (cached != null) {
            return cached;
        }

        Optional<MiniProfiles.Profile> found = MiniProfiles.get(owner).getNow(Optional.empty());
        if (found.isEmpty() || found.get().textures() == null) {
            return null;
        }

        MiniProfiles.Profile profile = found.get();
        Property textures = new Property("textures", profile.textures(), profile.signature());
        //? if >=1.21.9 {
        GameProfile gameProfile = new GameProfile(profile.id(), profile.name(), new PropertyMap(ImmutableMultimap.of("textures", textures)));
        //?} else {
        /*GameProfile gameProfile = new GameProfile(profile.id(), profile.name());
        gameProfile.getProperties().put("textures", textures);
        *///?}
        PROFILES.put(owner, gameProfile);
        return gameProfile;
    }
}
