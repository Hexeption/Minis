package uk.co.hexeption.minis.client;

import uk.co.hexeption.minis.client.render.entity.MiniRenderer;
import uk.co.hexeption.minis.client.screen.MiniNameScreen;
import uk.co.hexeption.minis.client.screen.MiniScreen;
import uk.co.hexeption.minis.client.item.MiniItemRenderer;
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
//? if >=1.21.4 {
import uk.co.hexeption.minis.client.item.MiniSpecialRenderer;
//?}
//? if !neoforge && >=1.21.4
/*import uk.co.hexeption.minis.mixin.SpecialModelRenderersAccessor;*/
//? if neoforge && >=1.21.4
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;
//? if fabric && <1.21.4
/*import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;*/
//? if fabric {
/*import uk.co.hexeption.minis.Minis;
import uk.co.hexeption.minis.init.ModItems;
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
        MenuScreens.register(ModMenus.MINI_NAME.get(), MiniNameScreen::new);
        //? if >=1.21.4 {
        SpecialModelRenderersAccessor.minis$idMapper().put(Minis.id("mini"), MiniSpecialRenderer.Unbaked.CODEC);
        //?} else
        /^BuiltinItemRendererRegistry.INSTANCE.register(ModItems.MINI.get(), (stack, context, poseStack, buffers, light, overlay) -> MiniItemRenderer.render(stack, poseStack, buffers, light));^/
    }
*///?} else {
public class MinisClient {
//?}

    //? if forge && >=1.21.11 {
    /*public static void register(BusGroup modBusGroup) {
        EntityRenderersEvent.RegisterRenderers.BUS.addListener(MinisClient::onRegisterRenderers);
        SpecialModelRenderersAccessor.minis$idMapper().put(Minis.id("mini"), MiniSpecialRenderer.Unbaked.CODEC);
        FMLClientSetupEvent.getBus(modBusGroup).addListener(MinisClient::onClientSetup);
    }
    *///?} else if forge && >=1.21.6 {
    /*public static void register(BusGroup modBusGroup) {
        EntityRenderersEvent.RegisterRenderers.getBus(modBusGroup).addListener(MinisClient::onRegisterRenderers);
        SpecialModelRenderersAccessor.minis$idMapper().put(Minis.id("mini"), MiniSpecialRenderer.Unbaked.CODEC);
        FMLClientSetupEvent.getBus(modBusGroup).addListener(MinisClient::onClientSetup);
    }
    *///?} else if forge {
    /*public static void register(IEventBus modEventBus) {
        modEventBus.addListener(MinisClient::onRegisterRenderers);
        modEventBus.addListener(MinisClient::onClientSetup);
        //? if >=1.21.4
        /^SpecialModelRenderersAccessor.minis$idMapper().put(Minis.id("mini"), MiniSpecialRenderer.Unbaked.CODEC);^/
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

    //? if neoforge && >=1.21.4 {
    @SubscribeEvent
    public static void onRegisterSpecialModelRenderers(RegisterSpecialModelRendererEvent event) {
        event.register(Minis.id("mini"), MiniSpecialRenderer.Unbaked.CODEC);
    }
    //?}

    //? if neoforge && >=1.20.5 {
    @SubscribeEvent
    public static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.MINI.get(), MiniScreen::new);
        event.register(ModMenus.MINI_NAME.get(), MiniNameScreen::new);
    }
    //?} else if forgeLike {
    /*//? if neoforge
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenus.MINI.get(), MiniScreen::new);
            MenuScreens.register(ModMenus.MINI_NAME.get(), MiniNameScreen::new);
        });
    }
    *///?}

}
