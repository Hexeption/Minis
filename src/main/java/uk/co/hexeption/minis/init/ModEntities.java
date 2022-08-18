package uk.co.hexeption.minis.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import uk.co.hexeption.minis.Minis;
import uk.co.hexeption.minis.entity.MiniEntity;

/**
 * ModEntities
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 13/04/2021 - 03:59 pm
 */
@Mod.EventBusSubscriber(modid = Minis.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {

	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Minis.MODID);

	public static final RegistryObject<EntityType<MiniEntity>> MINI_ENTITY = ENTITY_TYPES.register("mini",
			() -> EntityType.Builder.of(MiniEntity::new, MobCategory.AMBIENT)
					.sized(0.6f, 2f)
					.build(new ResourceLocation(Minis.MODID, "mini").toString())
	);

	@SubscribeEvent
	public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
		event.put(MINI_ENTITY.get(), MiniEntity.setCustomAttributes().build());
	}
}
