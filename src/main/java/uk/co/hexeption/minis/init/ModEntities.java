package uk.co.hexeption.minis.init;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import uk.co.hexeption.minis.Minis;
import uk.co.hexeption.minis.entity.MiniEntity;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;

/**
 * ModEntities
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 13/04/2021 - 03:59 pm
 */
@Mod.EventBusSubscriber(modid = Minis.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {

	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Minis.MODID);

	public static final RegistryObject<EntityType<MiniEntity>> MINI_ENTITY = ENTITY_TYPES.register("mini",
			() -> EntityType.Builder.create(MiniEntity::new, EntityClassification.AMBIENT)
					.size(0.6f, 2f)
					.build(new ResourceLocation(Minis.MODID, "mini").toString())
	);

	@SubscribeEvent
	public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
		event.put(MINI_ENTITY.get(), MiniEntity.setCustomAttributes().create());
	}
}
