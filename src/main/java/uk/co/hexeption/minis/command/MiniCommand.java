package uk.co.hexeption.minis.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import uk.co.hexeption.minis.entity.MiniEntity;
import uk.co.hexeption.minis.init.ModEntities;

//? if >=1.21.2
import net.minecraft.world.entity.EntitySpawnReason;

/**
 * MiniCommand
 *
 * /minis spawn [owner] spawns a Mini of the owner (you by default) where you're standing.
 *
 * @author Hexeption admin@hexeption.co.uk
 * @since 23/10/2026
 */
public final class MiniCommand {

    private MiniCommand() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("minis")
                //? if >=1.21.11 {
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                //?} else
                /*.requires(source -> source.hasPermission(2))*/
                .then(Commands.literal("spawn")
                        .executes(context -> spawn(context.getSource(), context.getSource().getPlayerOrException()))
                        .then(Commands.argument("owner", EntityArgument.player())
                                .executes(context -> spawn(context.getSource(), EntityArgument.getPlayer(context, "owner"))))));
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
