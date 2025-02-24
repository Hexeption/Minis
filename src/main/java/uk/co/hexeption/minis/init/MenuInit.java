package uk.co.hexeption.minis.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;
import uk.co.hexeption.minis.Minis;
import uk.co.hexeption.minis.client.gui.MiniInventoryMenu;

import java.util.function.Supplier;

public class MenuInit {

    private static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU, Minis.MODID);

    public static final Supplier<MenuType<MiniInventoryMenu>> MINI_INVENTORY = MENU_TYPES.register("mini_inventory", () -> IMenuTypeExtension.create(MiniInventoryMenu::new));

    public static void register(IEventBus bus) {
        MENU_TYPES.register(bus);
    }


}
