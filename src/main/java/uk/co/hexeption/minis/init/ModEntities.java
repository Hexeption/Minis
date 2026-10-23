package uk.co.hexeption.minis.init;

import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import uk.co.hexeption.minis.Minis;
import uk.co.hexeption.minis.entity.MiniEntity;

//? if neoforge {
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
//?}
//? if forge {
/*import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.DeferredRegister;
*///?}
//? if forge && >=1.21.6 {
/*import net.minecraftforge.eventbus.api.bus.BusGroup;
*///?} else if forge {
/*import net.minecraftforge.eventbus.api.IEventBus;
*///?}
//? if fabric {
/*import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
*///?}

/**
 * ModEntities
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 13/04/2021 - 03:59 pm
 */
public class ModEntities {

    //? if forgeLike {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, Minis.MODID);

    public static final Supplier<EntityType<MiniEntity>> MINI_ENTITY = ENTITY_TYPES.register("mini", () -> build("mini"));
    //?} else {
    /*public static final Supplier<EntityType<MiniEntity>> MINI_ENTITY = register("mini");
    *///?}

    // Half a player in each direction, to match how small the Mini is drawn
    private static EntityType<MiniEntity> build(String name) {
        EntityType.Builder<MiniEntity> builder = EntityType.Builder.of(MiniEntity::new, MobCategory.AMBIENT)
                .sized(0.3f, 0.9f);

        //? if >=1.21.2 {
        return builder.build(ResourceKey.create(Registries.ENTITY_TYPE, Minis.id(name)));
        //?} else
        /*return builder.build(name);*/
    }

    //? if neoforge {
    public static void register(IEventBus modEventBus) {
        ENTITY_TYPES.register(modEventBus);
        modEventBus.addListener(ModEntities::onEntityAttributeCreation);
    }
    //?}
    //? if forge && >=1.21.11 {
    /*public static void register(BusGroup modBusGroup) {
        ENTITY_TYPES.register(modBusGroup);
        EntityAttributeCreationEvent.BUS.addListener(ModEntities::onEntityAttributeCreation);
    }
    *///?} else if forge && >=1.21.6 {
    /*public static void register(BusGroup modBusGroup) {
        ENTITY_TYPES.register(modBusGroup);
        EntityAttributeCreationEvent.getBus(modBusGroup).addListener(ModEntities::onEntityAttributeCreation);
    }
    *///?} else if forge {
    /*public static void register(IEventBus modEventBus) {
        ENTITY_TYPES.register(modEventBus);
        modEventBus.addListener(ModEntities::onEntityAttributeCreation);
    }
    *///?}
    //? if fabric {
    /*public static void register() {
        FabricDefaultAttributeRegistry.register(MINI_ENTITY.get(), MiniEntity.setCustomAttributes());
    }

    private static Supplier<EntityType<MiniEntity>> register(String name) {
        EntityType<MiniEntity> type = Registry.register(BuiltInRegistries.ENTITY_TYPE, Minis.id(name), build(name));
        return () -> type;
    }
    *///?}

    //? if forgeLike {
    private static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(MINI_ENTITY.get(), MiniEntity.setCustomAttributes().build());
    }
    //?}

}
