package uk.co.hexeption.minis.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import uk.co.hexeption.minis.Minis;
import uk.co.hexeption.minis.entity.MiniEntity;

import java.util.function.Supplier;

/**
 * ModEntities
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 13/04/2021 - 03:59 pm
 */
@EventBusSubscriber(modid = Minis.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, Minis.MODID);

    public static final Supplier<EntityType<MiniEntity>> MINI_ENTITY = ENTITY_TYPES.register("mini", () -> EntityType.Builder.of(MiniEntity::new, MobCategory.AMBIENT)
            .sized(0.6f, 2f)
            .build("mini"));

    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {

        event.put(MINI_ENTITY.get(), MiniEntity.setCustomAttributes().build());
    }

}
