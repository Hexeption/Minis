package uk.co.hexeption.minis.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import uk.co.hexeption.minis.Minis;
import uk.co.hexeption.minis.client.render.entity.MiniRenderer;
import uk.co.hexeption.minis.init.ModEntities;

/**
 * RenderRegistry
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 13/04/2021 - 04:41 pm
 */
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RenderRegistry {


    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        Minis.LOGGER.info("Register Renderers");
        event.registerEntityRenderer(ModEntities.MINI_ENTITY.get(), (EntityRendererProvider.Context p_174557_) -> new MiniRenderer(p_174557_, true));
    }

}
