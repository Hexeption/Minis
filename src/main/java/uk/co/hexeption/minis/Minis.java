package uk.co.hexeption.minis;

import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.hexeption.minis.command.MiniCommand;
import uk.co.hexeption.minis.init.ModEntities;
import uk.co.hexeption.minis.init.ModItems;
import uk.co.hexeption.minis.init.ModMenus;

//? if neoforge {
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
//?}
//? if forge {
/*import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.event.RegisterCommandsEvent;
import uk.co.hexeption.minis.client.MinisClient;
*///?}
//? if forge && <1.21.6
/*import net.minecraftforge.common.MinecraftForge;*/
//? if fabric {
/*import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
*///?}

//? if forgeLike {
@Mod(Minis.MODID)
public class Minis {
//?} else {
/*public class Minis implements ModInitializer {
*///?}

    public final static String MODID = "minis";

    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    //? if neoforge {
    public Minis(IEventBus modEventBus) {
        ModEntities.register(modEventBus);
        ModMenus.register(modEventBus);
        ModItems.register(modEventBus);
        NeoForge.EVENT_BUS.addListener((RegisterCommandsEvent event) -> MiniCommand.register(event.getDispatcher()));
    }
    //?}
    //? if forge && >=1.21.6 {
    /*public Minis(FMLJavaModLoadingContext context) {
        ModEntities.register(context.getModBusGroup());
        ModMenus.register(context.getModBusGroup());
        ModItems.register(context.getModBusGroup());
        RegisterCommandsEvent.BUS.addListener(event -> MiniCommand.register(event.getDispatcher()));

        if (FMLEnvironment.dist == Dist.CLIENT) {
            MinisClient.register(context.getModBusGroup());
        }
    }
    *///?} else if forge {
    /*public Minis() {
        ModEntities.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModMenus.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModItems.register(FMLJavaModLoadingContext.get().getModEventBus());
        MinecraftForge.EVENT_BUS.addListener((RegisterCommandsEvent event) -> MiniCommand.register(event.getDispatcher()));

        if (FMLEnvironment.dist == Dist.CLIENT) {
            MinisClient.register(FMLJavaModLoadingContext.get().getModEventBus());
        }
    }
    *///?}
    //? if fabric {
    /*@Override
    public void onInitialize() {
        ModEntities.register();
        ModMenus.register();
        ModItems.register();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> MiniCommand.register(dispatcher));
    }
    *///?}

    public static Identifier id(String path) {
        //? if >=1.21 {
        return Identifier.fromNamespaceAndPath(MODID, path);
        //?} else
        /*return new Identifier(MODID, path);*/
    }

}
