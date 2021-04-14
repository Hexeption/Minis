package uk.co.hexeption.minis.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import uk.co.hexeption.minis.Minis;
import uk.co.hexeption.minis.client.render.entity.MiniRenderer;
import uk.co.hexeption.minis.entity.MiniEntity;
import uk.co.hexeption.minis.init.ModEntities;

/**
 * RenderRegistry
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 13/04/2021 - 04:41 pm
 */
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Minis.MODID, value = {Dist.CLIENT})
public class RenderRegistry {

	public static void registryEntityRenders() {
		RenderingRegistry.registerEntityRenderingHandler(ModEntities.MINI_ENTITY.get(), MiniRenderer::new);
	}



}
