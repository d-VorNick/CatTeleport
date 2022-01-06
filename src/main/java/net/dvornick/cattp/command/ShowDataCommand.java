package net.dvornick.cattp.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.dvornick.cattp.util.IEntityDataSaver;

import java.util.Arrays;

public class ShowDataCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        dispatcher.register(CommandManager.literal("showdata")
                .executes(ShowDataCommand::run));
    }

    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        IEntityDataSaver player = (IEntityDataSaver)context.getSource().getPlayer();
        int[] homePos = player.getPersistentData().getIntArray("homepos");

        if (homePos.length != 0) {
            context.getSource().sendFeedback(new LiteralText("Set home at " + Arrays.toString(homePos)), true);
            return 1;
        }
        else {
            context.getSource().sendFeedback(new LiteralText("Set home before"), true);
            return -1;
        }
    }
}