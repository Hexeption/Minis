package uk.co.hexeption.minis.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * SkinUtil
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 15/04/2021 - 06:49 pm
 */
public class SkinUtil {

	private static Gson g = new Gson();

	public static String getHeadValue(UUID uuid) {
		try {
			String jsonText = getURLContent("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString());
			JsonObject obj = g.fromJson(jsonText, JsonObject.class);
			String value = obj.getAsJsonArray("properties").get(0).getAsJsonObject().get("value").getAsString();
			return value;

		} catch (Exception e) {
			return "nil";
		}
	}

	private static String getURLContent(String urlStr) throws IOException {
		InputStream is = new URL(urlStr).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
			String text = readAll(rd);

			return text;
		} finally {
			is.close();
		}
	}

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}


}
