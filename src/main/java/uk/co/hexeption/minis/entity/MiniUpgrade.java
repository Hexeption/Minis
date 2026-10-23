package uk.co.hexeption.minis.entity;

import java.util.Locale;

/**
 * MiniUpgrade
 *
 * What an upgrade in a Mini's upgrade slots does.
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 23/10/2026
 */
public enum MiniUpgrade {
	// Pulls nearby dropped items into the Mini's inventory
	MAGNET,
	// Half as fast again
	SPEED,
	// Five more hearts
	HEALTH,
	// Fights whatever hurts its owner, or whatever its owner attacks
	COMBAT;

	public String id() {
		return this.name().toLowerCase(Locale.ROOT) + "_upgrade";
	}
}
