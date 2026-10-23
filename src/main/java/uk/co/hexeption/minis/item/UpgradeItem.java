package uk.co.hexeption.minis.item;

import net.minecraft.world.item.Item;
import uk.co.hexeption.minis.entity.MiniUpgrade;

/**
 * UpgradeItem
 *
 * Goes in one of a Mini's upgrade slots.
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 23/10/2026
 */
public class UpgradeItem extends Item {

	private final MiniUpgrade upgrade;

	public UpgradeItem(MiniUpgrade upgrade, Properties properties) {
		super(properties.stacksTo(1));
		this.upgrade = upgrade;
	}

	public MiniUpgrade getUpgrade() {
		return this.upgrade;
	}
}
