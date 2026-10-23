package uk.co.hexeption.minis.datagen;

import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.level.ItemLike;

//? if >=26.1 {
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
//?} else
/*import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;*/
//? if >=26.3 {
import net.minecraft.advancements.Advancement;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.crafting.Recipe;
//?} else if >=1.20.2
/*import net.minecraft.data.recipes.RecipeOutput;*/
//? if <1.20.2 {
/*import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
*///?}

/**
 * MinisRecipeProvider
 *
 * Writes {@link MinisRecipes} out in whichever recipe format this Minecraft version uses.
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 23/10/2026
 */
public class MinisRecipeProvider extends FabricRecipeProvider {

	//? if >=26.1 {
	public MinisRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}
	//?} else if >=1.20.5 {
	/*public MinisRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}
	*///?} else {
	/*public MinisRecipeProvider(FabricDataOutput output) {
		super(output);
	}
	*///?}

	private static String hasName(ItemLike item) {
		return "has_" + BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
	}

	//? if >=1.21.4 {
	//? if >=26.3 {
	@Override
	protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
		return new RecipeProvider(recipes, advancements) {
	//?} else {
	/*@Override
	protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput exporter) {
		return new RecipeProvider(registries, exporter) {
	*///?}
			@Override
			public void buildRecipes() {
				MinisRecipes.build(new MinisRecipes.Output() {
					@Override
					public ShapedRecipeBuilder create(ItemLike result) {
						return shaped(RecipeCategory.MISC, result);
					}

					@Override
					public void save(ShapedRecipeBuilder recipe, ItemLike unlockedBy) {
						recipe.unlockedBy(hasName(unlockedBy), has(unlockedBy)).save(output);
					}
				});
			}
		};
	}

	@Override
	public String getName() {
		return "Minis recipes";
	}
	//?} else {
	/*@Override
	//? if >=1.20.2 {
	public void buildRecipes(RecipeOutput exporter) {
	//?} else
	/^public void buildRecipes(Consumer<FinishedRecipe> exporter) {^/
		MinisRecipes.build(new MinisRecipes.Output() {
			@Override
			public ShapedRecipeBuilder create(ItemLike result) {
				return ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result);
			}

			@Override
			public void save(ShapedRecipeBuilder recipe, ItemLike unlockedBy) {
				recipe.unlockedBy(hasName(unlockedBy), has(unlockedBy)).save(exporter);
			}
		});
	}
	*///?}
}
