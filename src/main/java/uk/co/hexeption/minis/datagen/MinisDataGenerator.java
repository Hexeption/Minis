package uk.co.hexeption.minis.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

/**
 * MinisDataGenerator
 *
 * Datagen only runs on Fabric. What it writes is plain vanilla data, so the Forge and NeoForge builds of the same
 * Minecraft version read the same files.
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 23/10/2026
 */
public class MinisDataGenerator implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		FabricDataGenerator.Pack pack = generator.createPack();
		pack.addProvider(MinisRecipeProvider::new);
		pack.addProvider(MinisModelProvider::new);
	}
}
