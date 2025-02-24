package uk.co.hexeption.minis.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import uk.co.hexeption.minis.Minis;
import uk.co.hexeption.minis.client.gui.MiniInventoryMenuScreen;
import uk.co.hexeption.minis.client.render.entity.MiniRenderer;
import uk.co.hexeption.minis.init.MenuInit;
import uk.co.hexeption.minis.init.ModEntities;

/**
 * RenderRegistry
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 13/04/2021 - 04:41 pm
 */
@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class RenderRegistry {


    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        Minis.LOGGER.info("Register Renderers");
        event.registerEntityRenderer(ModEntities.MINI_ENTITY.get(), (EntityRendererProvider.Context p_174557_) -> new MiniRenderer(p_174557_, true));
    }

    @SubscribeEvent
    public static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        Minis.LOGGER.info("Register Menu Screens");
        event.register(MenuInit.MINI_INVENTORY.get(), MiniInventoryMenuScreen::new);
    }


}
