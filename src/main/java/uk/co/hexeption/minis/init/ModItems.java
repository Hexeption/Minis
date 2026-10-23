package uk.co.hexeption.minis.init;

import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import uk.co.hexeption.minis.Minis;
import uk.co.hexeption.minis.item.MiniItem;

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
 * ModItems
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 23/10/2026
 */
public class ModItems {

    //? if neoforge {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Minis.MODID);

    public static final Supplier<MiniItem> MINI = ITEMS.registerItem("mini", MiniItem::new);
    //?} else if forge {
    /*public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Minis.MODID);

    public static final Supplier<MiniItem> MINI = ITEMS.register("mini", () -> new MiniItem(properties("mini")));
    *///?} else {
    /*public static final Supplier<MiniItem> MINI = register("mini");
    *///?}

    // From 1.21.2 an item's properties have to know its id
    private static Item.Properties properties(String name) {
        //? if >=1.21.2 {
        return new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Minis.id(name)));
        //?} else
        /*return new Item.Properties();*/
    }

    //? if neoforge {
    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
    //?}
    //? if forge && >=1.21.6 {
    /*public static void register(BusGroup modBusGroup) {
        ITEMS.register(modBusGroup);
    }
    *///?} else if forge {
    /*public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
    *///?}
    //? if fabric {
    /*public static void register() {
    }

    private static Supplier<MiniItem> register(String name) {
        MiniItem item = Registry.register(BuiltInRegistries.ITEM, Minis.id(name), new MiniItem(properties(name)));
        return () -> item;
    }
    *///?}

}
