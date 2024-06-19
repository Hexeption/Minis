package uk.co.hexeption.minis;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.co.hexeption.minis.init.ModEntities;

@Mod("minis")
public class Minis {

    public final static String MODID = "minis";

    public static final Logger LOGGER = LogManager.getLogger();

    public Minis(IEventBus modEventBus, ModContainer modContainer) {
        ModEntities.ENTITY_TYPES.register(modEventBus);
    }


}
