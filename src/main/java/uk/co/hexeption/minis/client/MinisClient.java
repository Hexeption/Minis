package uk.co.hexeption.minis.client;

import uk.co.hexeption.minis.client.render.entity.MiniRenderer;
import uk.co.hexeption.minis.client.screen.MiniScreen;
import uk.co.hexeption.minis.init.ModEntities;
import uk.co.hexeption.minis.init.ModMenus;

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
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
//?} else if neoforge {
/*import net.minecraft.client.gui.screens.MenuScreens;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
*///?}
//? if forge {
/*import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
*///?}
//? if forge && >=1.21.6 {
/*import net.minecraftforge.eventbus.api.bus.BusGroup;
*///?} else if forge {
/*import net.minecraftforge.eventbus.api.IEventBus;
*///?}
//? if fabric {
/*import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
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
        MenuScreens.register(ModMenus.MINI.get(), MiniScreen::new);
    }
*///?} else {
public class MinisClient {
//?}

    //? if forge && >=1.21.11 {
    /*public static void register(BusGroup modBusGroup) {
        EntityRenderersEvent.RegisterRenderers.BUS.addListener(MinisClient::onRegisterRenderers);
        FMLClientSetupEvent.getBus(modBusGroup).addListener(MinisClient::onClientSetup);
    }
    *///?} else if forge && >=1.21.6 {
    /*public static void register(BusGroup modBusGroup) {
        EntityRenderersEvent.RegisterRenderers.getBus(modBusGroup).addListener(MinisClient::onRegisterRenderers);
        FMLClientSetupEvent.getBus(modBusGroup).addListener(MinisClient::onClientSetup);
    }
    *///?} else if forge {
    /*public static void register(IEventBus modEventBus) {
        modEventBus.addListener(MinisClient::onRegisterRenderers);
        modEventBus.addListener(MinisClient::onClientSetup);
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

    //? if neoforge && >=1.20.5 {
    @SubscribeEvent
    public static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.MINI.get(), MiniScreen::new);
    }
    //?} else if forgeLike {
    /*//? if neoforge
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> MenuScreens.register(ModMenus.MINI.get(), MiniScreen::new));
    }
    *///?}

}
