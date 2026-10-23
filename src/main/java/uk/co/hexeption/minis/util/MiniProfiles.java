package uk.co.hexeption.minis.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.Nullable;
import uk.co.hexeption.minis.Minis;

/**
 * MiniProfiles
 *
 * Looks up a player's name and skin textures from Mojang's session server, off the game thread.
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 23/10/2026
 */
public final class MiniProfiles {

    public record Profile(UUID id, String name, @Nullable String textures, @Nullable String signature) {
    }

    private static final HttpClient CLIENT = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    private static final Map<UUID, CompletableFuture<Optional<Profile>>> CACHE = new ConcurrentHashMap<>();
    private static final Map<String, CompletableFuture<Optional<UUID>>> IDS = new ConcurrentHashMap<>();

    private MiniProfiles() {
    }

    public static CompletableFuture<Optional<Profile>> get(UUID id) {
        return CACHE.computeIfAbsent(id, MiniProfiles::fetch);
    }

    // The player id for a username, or empty when nobody has that name
    public static CompletableFuture<Optional<UUID>> idOf(String name) {
        return IDS.computeIfAbsent(name.toLowerCase(Locale.ROOT), MiniProfiles::fetchId);
    }

    private static CompletableFuture<Optional<UUID>> fetchId(String name) {
        HttpRequest request = HttpRequest.newBuilder(URI.create("https://api.mojang.com/users/profiles/minecraft/" + URLEncoder.encode(name, StandardCharsets.UTF_8)))
                .timeout(Duration.ofSeconds(10))
                .build();

        return CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> response.statusCode() == 200 ? Optional.of(parseId(response.body())) : Optional.<UUID>empty())
                .exceptionally(error -> {
                    Minis.LOGGER.warn("Couldn't look up the player {}", name, error);
                    IDS.remove(name.toLowerCase(Locale.ROOT));
                    return Optional.empty();
                });
    }

    private static UUID parseId(String body) {
        String id = JsonParser.parseString(body).getAsJsonObject().get("id").getAsString();
        return UUID.fromString(id.replaceFirst("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"));
    }

    private static CompletableFuture<Optional<Profile>> fetch(UUID id) {
        HttpRequest request = HttpRequest.newBuilder(URI.create("https://sessionserver.mojang.com/session/minecraft/profile/" + id.toString().replace("-", "") + "?unsigned=false"))
                .timeout(Duration.ofSeconds(10))
                .build();

        return CLIENT.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> response.statusCode() == 200 ? Optional.of(parse(id, response.body())) : Optional.<Profile>empty())
                .exceptionally(error -> {
                    Minis.LOGGER.warn("Couldn't look up the profile for {}", id, error);
                    // Let a later Mini try again instead of caching the failure forever
                    CACHE.remove(id);
                    return Optional.empty();
                });
    }

    private static Profile parse(UUID id, String body) {
        JsonObject json = JsonParser.parseString(body).getAsJsonObject();
        String textures = null;
        String signature = null;

        JsonArray properties = json.has("properties") ? json.getAsJsonArray("properties") : new JsonArray();
        for (JsonElement element : properties) {
            JsonObject property = element.getAsJsonObject();
            if ("textures".equals(property.get("name").getAsString())) {
                textures = property.get("value").getAsString();
                signature = property.has("signature") ? property.get("signature").getAsString() : null;
            }
        }

        return new Profile(id, json.get("name").getAsString(), textures, signature);
    }
}
