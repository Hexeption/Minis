package uk.co.hexeption.minis.datagen;

//? if >=1.21.4 {
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
//?} else {
/*import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplates;
*///?}
import uk.co.hexeption.minis.entity.MiniUpgrade;
import uk.co.hexeption.minis.init.ModItems;

//? if >=1.21.4 {
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.data.models.model.ItemModelUtils;
import uk.co.hexeption.minis.Minis;
import uk.co.hexeption.minis.client.item.MiniSpecialRenderer;
//?} else
/*import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;*/
//? if >=26.1 {
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
//?} else
/*import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;*/

/**
 * MinisModelProvider
 *
 * Flat item models for the egg and the upgrades, and from 1.21.4 the item definitions that point at them.
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 23/10/2026
 */
public class MinisModelProvider extends FabricModelProvider {

	//? if >=26.1 {
	public MinisModelProvider(FabricPackOutput output) {
	//?} else
	/*public MinisModelProvider(FabricDataOutput output) {*/
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockModelGenerators generators) {
	}

	@Override
	public void generateItemModels(ItemModelGenerators generators) {
		generators.generateFlatItem(ModItems.MINI_EGG.get(), ModelTemplates.FLAT_ITEM);
		for (MiniUpgrade upgrade : MiniUpgrade.values()) {
			generators.generateFlatItem(ModItems.upgrade(upgrade), ModelTemplates.FLAT_ITEM);
		}
		//? if >=1.21.4 {
		// The Mini's own model is written by hand, this just points the item at the renderer that draws the Mini
		ItemModel.Unbaked mini = ItemModelUtils.specialModel(Minis.id("item/mini"), new MiniSpecialRenderer.Unbaked());
		generators.itemModelOutput.accept(ModItems.MINI.get(), mini);
		//?}
	}
}
