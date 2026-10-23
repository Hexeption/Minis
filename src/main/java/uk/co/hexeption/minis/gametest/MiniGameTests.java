package uk.co.hexeption.minis.gametest;

import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.ai.attributes.Attributes;
import uk.co.hexeption.minis.Minis;
import net.minecraft.world.level.block.Blocks;
import uk.co.hexeption.minis.entity.MiniEntity;
import uk.co.hexeption.minis.entity.MiniMode;
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
import net.minecraft.network.chat.Component;
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
    // jeb_
    private static final UUID OWNER = UUID.fromString("853c80ef-3c37-49fd-aa49-938b674adae6");

    //? if >=1.21.5 && forgeLike {
    @SubscribeEvent
    public static void registerTestFunctions(RegisterEvent event) {
        event.register(Registries.TEST_FUNCTION, registry -> {
            registry.register(Minis.id("mini_has_its_attributes"), MiniGameTests::miniHasItsAttributes);
            registry.register(Minis.id("mini_is_half_player_size"), MiniGameTests::miniIsHalfPlayerSize);
            registry.register(Minis.id("mini_keeps_owner_when_saved"), MiniGameTests::miniKeepsOwnerWhenSaved);
            registry.register(Minis.id("sitting_mini_stays_put"), MiniGameTests::sittingMiniStaysPut);
        });
    }
    //?} else if >=1.21.5 {
    /*public MiniGameTests() {
        Registry.register(BuiltInRegistries.TEST_FUNCTION, Minis.id("mini_has_its_attributes"), MiniGameTests::miniHasItsAttributes);
        Registry.register(BuiltInRegistries.TEST_FUNCTION, Minis.id("mini_is_half_player_size"), MiniGameTests::miniIsHalfPlayerSize);
        Registry.register(BuiltInRegistries.TEST_FUNCTION, Minis.id("mini_keeps_owner_when_saved"), MiniGameTests::miniKeepsOwnerWhenSaved);
        Registry.register(BuiltInRegistries.TEST_FUNCTION, Minis.id("sitting_mini_stays_put"), MiniGameTests::sittingMiniStaysPut);
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

    // Stone floor for movement tests. Starts at z=1 to leave the structure block at the origin alone
    private static void floor(GameTestHelper helper) {
        for (int x = 0; x <= 2; x++) {
            for (int z = 1; z <= 8; z++) {
                helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
            }
        }
    }
}
