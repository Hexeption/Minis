package uk.co.hexeption.minis.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import uk.co.hexeption.minis.entity.MiniEntity;
import uk.co.hexeption.minis.init.ModEntities;
import uk.co.hexeption.minis.menu.MiniHolder;
import uk.co.hexeption.minis.menu.MiniNameMenu;

//? if >=1.21.2
import net.minecraft.world.entity.EntitySpawnReason;

/**
 * MiniCommand
 *
 * /minis spawn [owner] spawns a Mini of the owner (you by default) where you're standing.
 * /minis name [name] renames the Mini whose screen you have open, and it takes that player's skin. Naming a newly
 * hatched Mini then opens its main screen.
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 23/10/2026
 */
public final class MiniCommand {

    public static final int MAX_NAME_LENGTH = 32;

    private MiniCommand() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("minis")
                .then(Commands.literal("spawn")
                        //? if >=1.21.11 {
                        .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                        //?} else
                        /*.requires(source -> source.hasPermission(2))*/
                        .executes(context -> spawn(context.getSource(), context.getSource().getPlayerOrException()))
                        .then(Commands.argument("owner", EntityArgument.player())
                                .executes(context -> spawn(context.getSource(), EntityArgument.getPlayer(context, "owner")))))
                // Used by the Mini's screen, so it only works on a Mini you own and have open
                .then(Commands.literal("name")
                        .executes(context -> rename(context.getSource(), ""))
                        .then(Commands.argument("name", StringArgumentType.greedyString())
                                .executes(context -> rename(context.getSource(), StringArgumentType.getString(context, "name"))))));
    }

    private static int rename(CommandSourceStack source, String name) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        if (!(player.containerMenu instanceof MiniHolder menu) || menu.getMini() == null || !menu.getMini().isOwnedBy(player)) {
            return 0;
        }

        MiniEntity mini = menu.getMini();
        String trimmed = name.trim();
        if (trimmed.isEmpty()) {
            mini.setCustomName(null);
            mini.setSkin(null);
            mini.nameAfterOwner();
        } else {
            String newName = trimmed.length() > MAX_NAME_LENGTH ? trimmed.substring(0, MAX_NAME_LENGTH) : trimmed;
            mini.setCustomName(Component.literal(newName));
            mini.useSkinOf(newName);
        }
        if (menu instanceof MiniNameMenu) {
            mini.openMenu(player);
        }
        return 1;
    }

    private static int spawn(CommandSourceStack source, ServerPlayer owner) {
        ServerLevel level = source.getLevel();
        //? if >=1.21.2 {
        MiniEntity mini = ModEntities.MINI_ENTITY.get().create(level, EntitySpawnReason.COMMAND);
        //?} else
        /*MiniEntity mini = ModEntities.MINI_ENTITY.get().create(level);*/
        if (mini == null) {
            return 0;
        }

        Vec3 position = source.getPosition();
        //? if >=1.21.5 {
        mini.snapTo(position.x, position.y, position.z, source.getRotation().y, 0.0F);
        //?} else
        /*mini.moveTo(position.x, position.y, position.z, source.getRotation().y, 0.0F);*/
        mini.setOwnerId(owner.getUUID());
        level.addFreshEntity(mini);

        source.sendSuccess(() -> Component.translatable("commands.minis.spawn.success", owner.getDisplayName()), true);
        return 1;
    }
}
