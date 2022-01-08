package net.dvornick.cattp.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.dvornick.cattp.events.InitCatTeleport;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.LiteralText;
import net.dvornick.cattp.util.IEntityDataSaver;

public class UpdatePointTimingCommand {

    public static final String POINT_KEY = "pointPos_";
    public static final String INITED = "TeleportPlayerInformation";

    public static void register() {
        ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("update-point")
                .then(ClientCommandManager.argument("name", StringArgumentType.string())
                        .then(ClientCommandManager.argument("from", IntegerArgumentType.integer())
                                .then(ClientCommandManager.argument("to", IntegerArgumentType.integer()).executes(UpdatePointTimingCommand::run)))));
    }

    private static int run(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        IEntityDataSaver player = (IEntityDataSaver) context.getSource().getPlayer();
        String name = context.getArgument("name", String.class);
        Integer from = context.getArgument("from", Integer.class);
        Integer to = context.getArgument("to", Integer.class);
        int[] time = {from, to};

        if (!player.getPersistentData().contains(INITED)) {
            InitCatTeleport.initialize(player, context);
        }

        if (player.getPersistentData().contains(POINT_KEY + name)) {
            player.getPersistentData().putIntArray(POINT_KEY + name, time);
            context.getSource().sendFeedback(new LiteralText("Point " + name + "timings: from " + from + " to " + to));
            return 1;
        }

        context.getSource().sendFeedback(new LiteralText("Point " + name + " not found"));
        return -1;
    }


}