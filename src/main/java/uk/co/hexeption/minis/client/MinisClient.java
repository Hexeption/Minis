package uk.co.hexeption.minis.client;

import uk.co.hexeption.minis.client.render.entity.MiniRenderer;
import uk.co.hexeption.minis.init.ModEntities;

//? if forgeLike {
import uk.co.hexeption.minis.Minis;
//?}
//? if neoforge {
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
//?}
//? if neoforge && >=1.20.5 {
import net.neoforged.fml.common.EventBusSubscriber;
//?} else if neoforge {
/*import net.neoforged.fml.common.Mod;
*///?}
//? if forge {
/*import net.minecraftforge.client.event.EntityRenderersEvent;
*///?}
//? if forge && >=1.21.6 {
/*import net.minecraftforge.eventbus.api.bus.BusGroup;
*///?} else if forge {
/*import net.minecraftforge.eventbus.api.IEventBus;
*///?}
//? if fabric {
/*import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
*///?}

/**
 * MinisClient
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 13/04/2021 - 04:41 pm
 */
//? if neoforge && >=1.21.4 {
@EventBusSubscriber(modid = Minis.MODID, value = Dist.CLIENT)
//?} else if neoforge && >=1.20.5 {
/*@EventBusSubscriber(modid = Minis.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
*///?} else if neoforge {
/*@Mod.EventBusSubscriber(modid = Minis.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
*///?}
//? if fabric {
/*public class MinisClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.MINI_ENTITY.get(), MiniRenderer::new);
    }
*///?} else {
public class MinisClient {
//?}

    //? if forge && >=1.21.11 {
    /*public static void register(BusGroup modBusGroup) {
        EntityRenderersEvent.RegisterRenderers.BUS.addListener(MinisClient::onRegisterRenderers);
    }
    *///?} else if forge && >=1.21.6 {
    /*public static void register(BusGroup modBusGroup) {
        EntityRenderersEvent.RegisterRenderers.getBus(modBusGroup).addListener(MinisClient::onRegisterRenderers);
    }
    *///?} else if forge {
    /*public static void register(IEventBus modEventBus) {
        modEventBus.addListener(MinisClient::onRegisterRenderers);
    }
    *///?}

    //? if forgeLike {
    //? if neoforge
    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        Minis.LOGGER.info("Register Renderers");
        event.registerEntityRenderer(ModEntities.MINI_ENTITY.get(), MiniRenderer::new);
    }
    //?}

}
