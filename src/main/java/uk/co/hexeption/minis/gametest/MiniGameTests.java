package uk.co.hexeption.minis.gametest;

import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attributes;
import uk.co.hexeption.minis.Minis;
import net.minecraft.world.level.block.Blocks;
import uk.co.hexeption.minis.entity.MiniEntity;
import uk.co.hexeption.minis.entity.MiniUpgrade;
import uk.co.hexeption.minis.init.ModItems;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.phys.AABB;
import uk.co.hexeption.minis.entity.MiniMode;
import uk.co.hexeption.minis.item.MiniItem;
import uk.co.hexeption.minis.menu.MiniMenu;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
//? if >=1.20.5
import net.minecraft.world.level.GameType;
import uk.co.hexeption.minis.init.ModEntities;

//? if >=1.21.6 {
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
//?} else {
/*import net.minecraft.nbt.CompoundTag;
*///?}
//? if >=1.21.2
import net.minecraft.world.entity.EntitySpawnReason;
//? if >=1.21.5 {
import net.minecraft.core.registries.Registries;
//?} else {
/*import net.minecraft.gametest.framework.GameTest;
*///?}
//? if >=1.21.5 && neoforge {
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;
//?} else if neoforge {
/*import net.neoforged.neoforge.gametest.GameTestHolder;
*///?}
//? if >=1.21.5 && forge {
/*import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;
*///?} else if forge {
/*import net.minecraftforge.gametest.GameTestHolder;
*///?}
//? if >=1.21.5 && fabric {
/*import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
*///?}

/**
 * MiniGameTests
 *
 * From 1.21.5 each test also needs a matching data/minis/test_instance/*.json.
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 23/10/2026
 */
//? if >=1.21.5 && neoforge {
@EventBusSubscriber(modid = Minis.MODID)
//?} else if >=1.21.5 && forge {
/*@Mod.EventBusSubscriber(modid = Minis.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
*///?} else if forgeLike {
/*@GameTestHolder(Minis.MODID)
*///?}
public class MiniGameTests {

    private static final BlockPos SPAWN = new BlockPos(1, 1, 1);
    // jeb_ and Notch
    private static final UUID OWNER = UUID.fromString("853c80ef-3c37-49fd-aa49-938b674adae6");
    private static final UUID SKIN = UUID.fromString("069a79f4-44e9-4726-a5be-fca90e38aaf5");

    //? if >=1.21.5 && forgeLike {
    @SubscribeEvent
    public static void registerTestFunctions(RegisterEvent event) {
        event.register(Registries.TEST_FUNCTION, registry -> {
            registry.register(Minis.id("mini_has_its_attributes"), MiniGameTests::miniHasItsAttributes);
            registry.register(Minis.id("mini_is_half_player_size"), MiniGameTests::miniIsHalfPlayerSize);
            registry.register(Minis.id("mini_keeps_owner_when_saved"), MiniGameTests::miniKeepsOwnerWhenSaved);
            registry.register(Minis.id("sitting_mini_stays_put"), MiniGameTests::sittingMiniStaysPut);
            registry.register(Minis.id("mode_buttons_change_the_mode"), MiniGameTests::modeButtonsChangeTheMode);
            registry.register(Minis.id("picked_up_mini_comes_back_the_same"), MiniGameTests::pickedUpMiniComesBackTheSame);
            registry.register(Minis.id("food_heals_a_hurt_mini"), MiniGameTests::foodHealsAHurtMini);
            registry.register(Minis.id("upgrades_change_speed_and_health"), MiniGameTests::upgradesChangeSpeedAndHealth);
            registry.register(Minis.id("magnet_collects_nearby_items"), MiniGameTests::magnetCollectsNearbyItems);
            registry.register(Minis.id("upgrade_slots_take_one_of_each"), MiniGameTests::upgradeSlotsTakeOneOfEach);
            registry.register(Minis.id("combat_upgrade_lets_a_mini_fight"), MiniGameTests::combatUpgradeLetsAMiniFight);
            registry.register(Minis.id("dungeon_chests_can_have_eggs"), MiniGameTests::dungeonChestsCanHaveEggs);
        });
    }
    //?} else if >=1.21.5 {
    /*public MiniGameTests() {
        Registry.register(BuiltInRegistries.TEST_FUNCTION, Minis.id("mini_has_its_attributes"), MiniGameTests::miniHasItsAttributes);
        Registry.register(BuiltInRegistries.TEST_FUNCTION, Minis.id("mini_is_half_player_size"), MiniGameTests::miniIsHalfPlayerSize);
        Registry.register(BuiltInRegistries.TEST_FUNCTION, Minis.id("mini_keeps_owner_when_saved"), MiniGameTests::miniKeepsOwnerWhenSaved);
        Registry.register(BuiltInRegistries.TEST_FUNCTION, Minis.id("sitting_mini_stays_put"), MiniGameTests::sittingMiniStaysPut);
        Registry.register(BuiltInRegistries.TEST_FUNCTION, Minis.id("mode_buttons_change_the_mode"), MiniGameTests::modeButtonsChangeTheMode);
        Registry.register(BuiltInRegistries.TEST_FUNCTION, Minis.id("picked_up_mini_comes_back_the_same"), MiniGameTests::pickedUpMiniComesBackTheSame);
        Registry.register(BuiltInRegistries.TEST_FUNCTION, Minis.id("food_heals_a_hurt_mini"), MiniGameTests::foodHealsAHurtMini);
        Registry.register(BuiltInRegistries.TEST_FUNCTION, Minis.id("upgrades_change_speed_and_health"), MiniGameTests::upgradesChangeSpeedAndHealth);
        Registry.register(BuiltInRegistries.TEST_FUNCTION, Minis.id("magnet_collects_nearby_items"), MiniGameTests::magnetCollectsNearbyItems);
        Registry.register(BuiltInRegistries.TEST_FUNCTION, Minis.id("upgrade_slots_take_one_of_each"), MiniGameTests::upgradeSlotsTakeOneOfEach);
        Registry.register(BuiltInRegistries.TEST_FUNCTION, Minis.id("combat_upgrade_lets_a_mini_fight"), MiniGameTests::combatUpgradeLetsAMiniFight);
        Registry.register(BuiltInRegistries.TEST_FUNCTION, Minis.id("dungeon_chests_can_have_eggs"), MiniGameTests::dungeonChestsCanHaveEggs);
    }
    *///?} else {
    /*//? if fabric {
    /^private static final String TEMPLATE = "fabric-gametest-api-v1:empty";
    ^///?} else if forge && >1.20.1 {
    /^private static final String TEMPLATE = "minis:minigametests.empty";
    ^///?} else {
    /^private static final String TEMPLATE = "empty";
    ^///?}

    @GameTest(template = TEMPLATE)
    public void miniHasItsAttributesTest(GameTestHelper helper) {
        miniHasItsAttributes(helper);
    }

    @GameTest(template = TEMPLATE)
    public void miniIsHalfPlayerSizeTest(GameTestHelper helper) {
        miniIsHalfPlayerSize(helper);
    }

    @GameTest(template = TEMPLATE)
    public void miniKeepsOwnerWhenSavedTest(GameTestHelper helper) {
        miniKeepsOwnerWhenSaved(helper);
    }

    @GameTest(template = TEMPLATE)
    public void sittingMiniStaysPutTest(GameTestHelper helper) {
        sittingMiniStaysPut(helper);
    }

    @GameTest(template = TEMPLATE)
    public void modeButtonsChangeTheModeTest(GameTestHelper helper) {
        modeButtonsChangeTheMode(helper);
    }

    @GameTest(template = TEMPLATE)
    public void pickedUpMiniComesBackTheSameTest(GameTestHelper helper) {
        pickedUpMiniComesBackTheSame(helper);
    }

    @GameTest(template = TEMPLATE)
    public void foodHealsAHurtMiniTest(GameTestHelper helper) {
        foodHealsAHurtMini(helper);
    }

    @GameTest(template = TEMPLATE)
    public void upgradesChangeSpeedAndHealthTest(GameTestHelper helper) {
        upgradesChangeSpeedAndHealth(helper);
    }

    @GameTest(template = TEMPLATE, timeoutTicks = 100)
    public void magnetCollectsNearbyItemsTest(GameTestHelper helper) {
        magnetCollectsNearbyItems(helper);
    }

    @GameTest(template = TEMPLATE)
    public void upgradeSlotsTakeOneOfEachTest(GameTestHelper helper) {
        upgradeSlotsTakeOneOfEach(helper);
    }

    @GameTest(template = TEMPLATE)
    public void combatUpgradeLetsAMiniFightTest(GameTestHelper helper) {
        combatUpgradeLetsAMiniFight(helper);
    }

    @GameTest(template = TEMPLATE)
    public void dungeonChestsCanHaveEggsTest(GameTestHelper helper) {
        dungeonChestsCanHaveEggs(helper);
    }
    *///?}

    private static void check(GameTestHelper helper, boolean condition, String message) {
        //? if >=1.21.5 {
        helper.assertTrue(condition, Component.literal(message));
        //?} else
        /*helper.assertTrue(condition, message);*/
    }

    public static void miniHasItsAttributes(GameTestHelper helper) {
        MiniEntity mini = helper.spawn(ModEntities.MINI_ENTITY.get(), SPAWN);

        check(helper, mini.getMaxHealth() == 10.0F, "Expected 10 max health but got " + mini.getMaxHealth());
        check(helper, mini.getAttributeValue(Attributes.MOVEMENT_SPEED) == 0.3D, "Expected a movement speed of 0.3 but got " + mini.getAttributeValue(Attributes.MOVEMENT_SPEED));
        helper.succeed();
    }

    public static void miniIsHalfPlayerSize(GameTestHelper helper) {
        MiniEntity mini = helper.spawn(ModEntities.MINI_ENTITY.get(), SPAWN);

        check(helper, mini.getBbWidth() == 0.3F && mini.getBbHeight() == 0.9F, "Expected a 0.3x0.9 hitbox but got " + mini.getBbWidth() + "x" + mini.getBbHeight());
        helper.succeed();
    }

    public static void miniKeepsOwnerWhenSaved(GameTestHelper helper) {
        MiniEntity mini = helper.spawn(ModEntities.MINI_ENTITY.get(), SPAWN);
        mini.setOwnerId(OWNER);
        mini.setMode(MiniMode.FOLLOW);
        mini.setSkin(SKIN);
        mini.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
        mini.getInventory().setItem(3, new ItemStack(Items.DIAMOND, 5));

        //? if >=1.21.2 {
        MiniEntity loaded = ModEntities.MINI_ENTITY.get().create(helper.getLevel(), EntitySpawnReason.LOAD);
        //?} else
        /*MiniEntity loaded = ModEntities.MINI_ENTITY.get().create(helper.getLevel());*/
        //? if >=1.21.6 {
        TagValueOutput output = TagValueOutput.createWithoutContext(ProblemReporter.DISCARDING);
        mini.saveWithoutId(output);
        loaded.load(TagValueInput.create(ProblemReporter.DISCARDING, helper.getLevel().registryAccess(), output.buildResult()));
        //?} else
        /*loaded.load(mini.saveWithoutId(new CompoundTag()));*/

        check(helper, OWNER.equals(loaded.getOwnerId()), "Expected owner " + OWNER + " after loading but got " + loaded.getOwnerId());
        check(helper, loaded.getMode() == MiniMode.FOLLOW, "Expected the follow mode after loading but got " + loaded.getMode());
        check(helper, SKIN.equals(loaded.getSkinId()), "Expected the skin of " + SKIN + " after loading but got " + loaded.getSkinId());
        check(helper, loaded.getItemBySlot(EquipmentSlot.HEAD).is(Items.DIAMOND_HELMET), "The Mini lost its helmet when saved");
        check(helper, loaded.getInventory().getItem(3).is(Items.DIAMOND) && loaded.getInventory().getItem(3).getCount() == 5, "The Mini lost its inventory when saved");
        helper.succeed();
    }

    public static void sittingMiniStaysPut(GameTestHelper helper) {
        floor(helper);
        MiniEntity mini = helper.spawn(ModEntities.MINI_ENTITY.get(), SPAWN);
        mini.setMode(MiniMode.SIT);
        BlockPos target = helper.absolutePos(new BlockPos(1, 1, 7));
        mini.getNavigation().moveTo(target.getX() + 0.5D, target.getY(), target.getZ() + 0.5D, 1.0D);
        double startZ = mini.getZ();

        helper.succeedWhen(() -> {
            check(helper, mini.getNavigation().isDone(), "A sitting Mini is still following a path");
            check(helper, Math.abs(mini.getZ() - startZ) < 0.5D, "A sitting Mini moved " + (mini.getZ() - startZ) + " blocks");
        });
    }

    public static void modeButtonsChangeTheMode(GameTestHelper helper) {
        MiniEntity mini = helper.spawn(ModEntities.MINI_ENTITY.get(), SPAWN);
        //? if >=1.20.5 {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        //?} else
        /*Player player = helper.makeMockPlayer();*/
        mini.setOwnerId(player.getUUID());

        MiniMenu menu = new MiniMenu(0, player.getInventory(), mini);
        menu.clickMenuButton(player, MiniMode.SIT.ordinal());

        check(helper, mini.getMode() == MiniMode.SIT && menu.getMode() == MiniMode.SIT, "The sit button left the Mini in " + mini.getMode());
        helper.succeed();
    }

    public static void pickedUpMiniComesBackTheSame(GameTestHelper helper) {
        MiniEntity mini = helper.spawn(ModEntities.MINI_ENTITY.get(), SPAWN);
        mini.setOwnerId(OWNER);
        mini.setMode(MiniMode.SIT);
        mini.setSkin(SKIN);
        mini.setCustomName(Component.literal("Buddy"));
        mini.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
        mini.getInventory().setItem(8, new ItemStack(Items.EMERALD, 3));

        ItemStack item = MiniItem.of(mini);
        mini.discard();
        check(helper, Component.translatable("item.minis.mini.named", "Buddy").equals(item.getHoverName()), "The item is called " + item.getHoverName());

        //? if >=1.21.2 {
        MiniEntity placed = ModEntities.MINI_ENTITY.get().create(helper.getLevel(), EntitySpawnReason.LOAD);
        //?} else
        /*MiniEntity placed = ModEntities.MINI_ENTITY.get().create(helper.getLevel());*/
        placed.loadFromItem(MiniItem.data(item));

        check(helper, OWNER.equals(placed.getOwnerId()) && SKIN.equals(placed.getSkinId()), "The placed Mini lost its owner or skin");
        check(helper, placed.getMode() == MiniMode.SIT, "The placed Mini lost its mode");
        check(helper, placed.getItemBySlot(EquipmentSlot.CHEST).is(Items.IRON_CHESTPLATE), "The placed Mini lost its armor");
        check(helper, placed.getInventory().getItem(8).is(Items.EMERALD), "The placed Mini lost its inventory");
        helper.succeed();
    }

    public static void foodHealsAHurtMini(GameTestHelper helper) {
        MiniEntity mini = helper.spawn(ModEntities.MINI_ENTITY.get(), SPAWN);
        mini.setHealth(4.0F);

        check(helper, mini.feed(new ItemStack(Items.BREAD)), "A hurt Mini wouldn't eat bread");
        check(helper, mini.getHealth() == 9.0F, "Bread should heal 5 but the Mini is on " + mini.getHealth());
        check(helper, !mini.feed(new ItemStack(Items.DIRT)), "A Mini ate dirt");

        mini.setHealth(mini.getMaxHealth());
        check(helper, !mini.feed(new ItemStack(Items.BREAD)), "A Mini at full health still ate");
        helper.succeed();
    }

    public static void upgradesChangeSpeedAndHealth(GameTestHelper helper) {
        MiniEntity mini = helper.spawn(ModEntities.MINI_ENTITY.get(), SPAWN);
        mini.getUpgrades().setItem(0, new ItemStack(ModItems.upgrade(MiniUpgrade.SPEED)));
        mini.getUpgrades().setItem(1, new ItemStack(ModItems.upgrade(MiniUpgrade.HEALTH)));
        mini.setHealth(mini.getMaxHealth());

        check(helper, Math.abs(mini.getAttributeValue(Attributes.MOVEMENT_SPEED) - 0.45D) < 1.0E-6D, "Expected a movement speed of 0.45 but got " + mini.getAttributeValue(Attributes.MOVEMENT_SPEED));
        check(helper, mini.getMaxHealth() == 20.0F && mini.getHealth() == 20.0F, "Expected 20 health but got " + mini.getHealth() + "/" + mini.getMaxHealth());

        //? if >=1.21.2 {
        MiniEntity loaded = ModEntities.MINI_ENTITY.get().create(helper.getLevel(), EntitySpawnReason.LOAD);
        //?} else
        /*MiniEntity loaded = ModEntities.MINI_ENTITY.get().create(helper.getLevel());*/
        loaded.loadFromItem(mini.saveForItem());
        check(helper, loaded.hasUpgrade(MiniUpgrade.HEALTH) && loaded.getHealth() == 20.0F, "The Mini lost its upgrade or health when saved, it's on " + loaded.getHealth());

        mini.getUpgrades().setItem(1, ItemStack.EMPTY);
        check(helper, mini.getMaxHealth() == 10.0F && mini.getHealth() == 10.0F, "Taking the health upgrade out left " + mini.getHealth() + "/" + mini.getMaxHealth());
        helper.succeed();
    }

    public static void magnetCollectsNearbyItems(GameTestHelper helper) {
        floor(helper);
        // Opposite corners of the 3x3 test area, so the item has to be pulled in but never leaves the test's space
        MiniEntity mini = helper.spawn(ModEntities.MINI_ENTITY.get(), new BlockPos(0, 1, 0));
        mini.setMode(MiniMode.SIT);
        mini.getUpgrades().setItem(0, new ItemStack(ModItems.upgrade(MiniUpgrade.MAGNET)));
        BlockPos drop = helper.absolutePos(new BlockPos(2, 1, 2));
        ItemEntity item = new ItemEntity(helper.getLevel(), drop.getX() + 0.5D, drop.getY(), drop.getZ() + 0.5D, new ItemStack(Items.DIAMOND, 2));
        item.setNoPickUpDelay();
        helper.getLevel().addFreshEntity(item);

        helper.succeedWhen(() -> check(helper, mini.getInventory().countItem(Items.DIAMOND) == 2, "The magnet hasn't picked up the diamonds"));
    }

    public static void upgradeSlotsTakeOneOfEach(GameTestHelper helper) {
        MiniEntity mini = helper.spawn(ModEntities.MINI_ENTITY.get(), SPAWN);
        //? if >=1.20.5 {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        //?} else
        /*Player player = helper.makeMockPlayer();*/
        mini.setOwnerId(player.getUUID());
        mini.getUpgrades().setItem(0, new ItemStack(ModItems.upgrade(MiniUpgrade.SPEED)));
        MiniMenu menu = new MiniMenu(0, player.getInventory(), mini);
        int second = MiniEntity.EQUIPMENT_SLOTS.length + MiniEntity.INVENTORY_SIZE + 1;

        check(helper, !menu.getSlot(second).mayPlace(new ItemStack(ModItems.upgrade(MiniUpgrade.SPEED))), "A second speed upgrade fit");
        check(helper, menu.getSlot(second).mayPlace(new ItemStack(ModItems.upgrade(MiniUpgrade.MAGNET))), "A magnet upgrade didn't fit next to a speed upgrade");
        check(helper, !menu.getSlot(second).mayPlace(new ItemStack(Items.DIAMOND)), "A diamond fit in an upgrade slot");
        helper.succeed();
    }

    public static void combatUpgradeLetsAMiniFight(GameTestHelper helper) {
        MiniEntity mini = helper.spawn(ModEntities.MINI_ENTITY.get(), SPAWN);
        check(helper, !mini.canFight(), "A Mini without the combat upgrade can fight");

        mini.getUpgrades().setItem(0, new ItemStack(ModItems.upgrade(MiniUpgrade.COMBAT)));
        check(helper, mini.canFight(), "A Mini with the combat upgrade can't fight");

        mini.setMode(MiniMode.SIT);
        check(helper, !mini.canFight(), "A sitting Mini can fight");
        helper.succeed();
    }

    // Each dungeon chest has a one in four chance, so 40 chests without an egg would be about a one in 100,000 fluke
    public static void dungeonChestsCanHaveEggs(GameTestHelper helper) {
        BlockPos at = helper.absolutePos(SPAWN);
        MinecraftServer server = helper.getLevel().getServer();
        CommandSourceStack source = server.createCommandSourceStack().withLevel(helper.getLevel()).withSuppressedOutput();
        for (int i = 0; i < 40; i++) {
            server.getCommands().performPrefixedCommand(source, "loot spawn " + at.getX() + " " + at.getY() + " " + at.getZ() + " loot minecraft:chests/simple_dungeon");
        }

        AABB area = new AABB(at).inflate(2.0D);
        boolean egg = !helper.getLevel().getEntitiesOfClass(ItemEntity.class, area, item -> item.getItem().is(ModItems.MINI_EGG.get())).isEmpty();
        for (ItemEntity item : helper.getLevel().getEntitiesOfClass(ItemEntity.class, area)) {
            item.discard();
        }
        check(helper, egg, "40 dungeon chests and not one Mini egg");
        helper.succeed();
    }

    // Stone floor for movement tests. Starts at z=1 to leave the structure block at the origin alone
    private static void floor(GameTestHelper helper) {
        for (int x = 0; x <= 2; x++) {
            for (int z = 1; z <= 8; z++) {
                helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
            }
        }
    }
}
