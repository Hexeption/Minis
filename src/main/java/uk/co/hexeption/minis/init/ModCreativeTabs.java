package uk.co.hexeption.minis.init;

import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import uk.co.hexeption.minis.Minis;
import uk.co.hexeption.minis.entity.MiniUpgrade;

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
//? if fabric && >=26.1 {
/*import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
*///?} else if fabric {
/*import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
*///?}

/**
 * ModCreativeTabs
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 23/10/2026
 */
public class ModCreativeTabs {

    //? if forgeLike {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Minis.MODID);

    public static final Supplier<CreativeModeTab> MINIS = CREATIVE_TABS.register("minis", () -> fill(CreativeModeTab.builder()));
    //?} else if >=26.1 {
    /*public static final CreativeModeTab MINIS = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Minis.id("minis"), fill(FabricCreativeModeTab.builder()));
    *///?} else {
    /*public static final CreativeModeTab MINIS = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Minis.id("minis"), fill(FabricItemGroup.builder()));
    *///?}

    private static CreativeModeTab fill(CreativeModeTab.Builder builder) {
        return builder.title(Component.translatable("itemGroup.minis"))
                .icon(() -> new ItemStack(ModItems.MINI_EGG.get()))
                .displayItems((parameters, output) -> {
                    output.accept(ModItems.MINI_EGG.get());
                    for (MiniUpgrade upgrade : MiniUpgrade.values()) {
                        output.accept(ModItems.upgrade(upgrade));
                    }
                })
                .build();
    }

    //? if neoforge {
    public static void register(IEventBus modEventBus) {
        CREATIVE_TABS.register(modEventBus);
    }
    //?}
    //? if forge && >=1.21.6 {
    /*public static void register(BusGroup modBusGroup) {
        CREATIVE_TABS.register(modBusGroup);
    }
    *///?} else if forge {
    /*public static void register(IEventBus modEventBus) {
        CREATIVE_TABS.register(modEventBus);
    }
    *///?}
    //? if fabric {
    /*public static void register() {
    }
    *///?}

}
