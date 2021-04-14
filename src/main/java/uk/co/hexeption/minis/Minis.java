package uk.co.hexeption.minis;

import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.co.hexeption.minis.client.RenderRegistry;
import uk.co.hexeption.minis.entity.MiniEntity;
import uk.co.hexeption.minis.init.ModEntities;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

@Mod("minis")
public class Minis {

	public final static String MODID = "minis";

	public static final Logger LOGGER = LogManager.getLogger();

	public static ItemGroup itemGroup = new ItemGroup("minis") {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(Blocks.CHEST.asItem());
		}
	};

	public Minis() {

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);

		ModEntities.ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());

	}

	private void setupClient(final FMLClientSetupEvent event) {
		RenderRegistry.registryEntityRenders();
	}




}
