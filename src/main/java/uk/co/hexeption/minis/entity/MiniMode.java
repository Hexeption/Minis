package uk.co.hexeption.minis.entity;

import java.util.Locale;

/**
 * MiniMode
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 23/10/2026
 */
public enum MiniMode {
	WANDER,
	FOLLOW,
	SIT;

	public static MiniMode byName(String name) {
		for (MiniMode mode : values()) {
			if (mode.name().equalsIgnoreCase(name)) {
				return mode;
			}
		}
		return WANDER;
	}

	public static MiniMode byId(int id) {
		return id >= 0 && id < values().length ? values()[id] : WANDER;
	}

	public MiniMode next() {
		return values()[(ordinal() + 1) % values().length];
	}

	public String translationKey() {
		return "minis.mode." + name().toLowerCase(Locale.ROOT);
	}
}
