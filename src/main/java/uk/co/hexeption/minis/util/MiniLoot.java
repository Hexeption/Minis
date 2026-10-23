package uk.co.hexeption.minis.util;

import java.util.Set;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import uk.co.hexeption.minis.init.ModItems;

//? if neoforge {
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.LootTableLoadEvent;
//?}
//? if forge {
/*import net.minecraftforge.event.LootTableLoadEvent;
*///?}
//? if forge && <1.21.6
/*import net.minecraftforge.common.MinecraftForge;*/
//? if fabric && >=1.21 {
/*import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
*///?} else if fabric {
/*import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
*///?}

/**
 * MiniLoot
 *
 * Puts the odd Mini egg in dungeon, mineshaft and temple chests.
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 23/10/2026
 */
public final class MiniLoot {

	private static final Set<String> EGG_CHESTS = Set.of(
			"minecraft:chests/simple_dungeon",
			"minecraft:chests/abandoned_mineshaft",
			"minecraft:chests/desert_pyramid",
			"minecraft:chests/jungle_temple");

	private MiniLoot() {
	}

	// A pool rolls once by default, so this is a one in four chance of an egg
	private static LootPool.Builder eggPool() {
		return LootPool.lootPool()
				.add(LootItem.lootTableItem(ModItems.MINI_EGG.get()).setWeight(1))
				.add(EmptyLootItem.emptyItem().setWeight(3));
	}

	public static void register() {
		//? if neoforge {
		NeoForge.EVENT_BUS.addListener((LootTableLoadEvent event) -> {
			if (EGG_CHESTS.contains(event.getName().toString())) {
				event.getTable().addPool(eggPool().build());
			}
		});
		//?} else if forge && >=1.21.6 {
		/*LootTableLoadEvent.BUS.addListener(event -> {
			if (EGG_CHESTS.contains(event.getName().toString())) {
				event.getTable().addPool(eggPool().build());
			}
		});
		*///?} else if forge {
		/*MinecraftForge.EVENT_BUS.addListener((LootTableLoadEvent event) -> {
			if (EGG_CHESTS.contains(event.getName().toString())) {
				event.getTable().addPool(eggPool().build());
			}
		});
		*///?} else if >=1.21.11 {
		/*LootTableEvents.MODIFY.register((key, table, source, registries) -> {
			if (source.isBuiltin() && EGG_CHESTS.contains(key.identifier().toString())) {
				table.withPool(eggPool());
			}
		});
		*///?} else if >=1.21 {
		/*LootTableEvents.MODIFY.register((key, table, source, registries) -> {
			if (source.isBuiltin() && EGG_CHESTS.contains(key.location().toString())) {
				table.withPool(eggPool());
			}
		});
		*///?} else if >=1.20.5 {
		/*LootTableEvents.MODIFY.register((key, table, source) -> {
			if (source.isBuiltin() && EGG_CHESTS.contains(key.location().toString())) {
				table.withPool(eggPool());
			}
		});
		*///?} else {
		/*LootTableEvents.MODIFY.register((resourceManager, lootManager, id, table, source) -> {
			if (source.isBuiltin() && EGG_CHESTS.contains(id.toString())) {
				table.withPool(eggPool());
			}
		});
		*///?}
	}
}
