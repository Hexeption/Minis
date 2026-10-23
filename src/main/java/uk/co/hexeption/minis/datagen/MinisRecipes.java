package uk.co.hexeption.minis.datagen;

import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import uk.co.hexeption.minis.entity.MiniUpgrade;
import uk.co.hexeption.minis.init.ModItems;

/**
 * MinisRecipes
 *
 * The mod's crafting recipes, kept apart from the recipe provider because every Minecraft version builds them differently.
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 23/10/2026
 */
public final class MinisRecipes {

	private MinisRecipes() {
	}

	public static void build(Output output) {
		output.save(output.create(ModItems.MINI_EGG.get())
				.pattern("NNN")
				.pattern("NEN")
				.pattern("NNN")
				.define('N', Items.GOLD_NUGGET)
				.define('E', Items.EGG), Items.EGG);

		output.save(output.create(ModItems.upgrade(MiniUpgrade.MAGNET))
				.pattern("R R")
				.pattern("I I")
				.pattern("III")
				.define('R', Items.REDSTONE)
				.define('I', Items.IRON_INGOT), Items.IRON_INGOT);

		output.save(output.create(ModItems.upgrade(MiniUpgrade.SPEED))
				.pattern("SFS")
				.pattern("FGF")
				.pattern("SFS")
				.define('S', Items.SUGAR)
				.define('F', Items.FEATHER)
				.define('G', Items.GOLD_INGOT), Items.SUGAR);

		output.save(output.create(ModItems.upgrade(MiniUpgrade.HEALTH))
				.pattern("NMN")
				.pattern("MAM")
				.pattern("NMN")
				.define('N', Items.GOLD_NUGGET)
				.define('M', Items.GLISTERING_MELON_SLICE)
				.define('A', Items.APPLE), Items.APPLE);

		output.save(output.create(ModItems.upgrade(MiniUpgrade.COMBAT))
				.pattern(" S ")
				.pattern("IDI")
				.pattern(" I ")
				.define('S', Items.IRON_SWORD)
				.define('I', Items.IRON_INGOT)
				.define('D', Items.DIAMOND), Items.IRON_SWORD);
	}

	public interface Output {
		ShapedRecipeBuilder create(ItemLike result);

		// Unlocks the recipe once the player has the given item, then writes it out
		void save(ShapedRecipeBuilder recipe, ItemLike unlockedBy);
	}
}
