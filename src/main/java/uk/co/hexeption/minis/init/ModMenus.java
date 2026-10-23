package uk.co.hexeption.minis.init;

import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;
import uk.co.hexeption.minis.Minis;
import uk.co.hexeption.minis.menu.MiniMenu;

//? if neoforge {
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
//?}
//? if forge {
/*import net.minecraftforge.registries.DeferredRegister;
*///?}
//? if forge && >=1.21.6 {
/*import net.minecraftforge.eventbus.api.bus.BusGroup;
*///?} else if forge {
/*import net.minecraftforge.eventbus.api.IEventBus;
*///?}
//? if fabric {
/*import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
*///?}

/**
 * ModMenus
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 23/10/2026
 */
public class ModMenus {

    //? if forgeLike {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, Minis.MODID);

    public static final Supplier<MenuType<MiniMenu>> MINI = MENU_TYPES.register("mini", () -> new MenuType<>(MiniMenu::new, FeatureFlagSet.of()));
    //?} else {
    /*public static final Supplier<MenuType<MiniMenu>> MINI = register("mini");
    *///?}

    //? if neoforge {
    public static void register(IEventBus modEventBus) {
        MENU_TYPES.register(modEventBus);
    }
    //?}
    //? if forge && >=1.21.6 {
    /*public static void register(BusGroup modBusGroup) {
        MENU_TYPES.register(modBusGroup);
    }
    *///?} else if forge {
    /*public static void register(IEventBus modEventBus) {
        MENU_TYPES.register(modEventBus);
    }
    *///?}
    //? if fabric {
    /*public static void register() {
    }

    private static Supplier<MenuType<MiniMenu>> register(String name) {
        MenuType<MiniMenu> type = Registry.register(BuiltInRegistries.MENU, Minis.id(name), new MenuType<>(MiniMenu::new, FeatureFlagSet.of()));
        return () -> type;
    }
    *///?}

}
