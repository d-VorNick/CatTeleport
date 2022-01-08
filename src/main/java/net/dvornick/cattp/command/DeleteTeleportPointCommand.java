package net.dvornick.cattp.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.dvornick.cattp.events.InitCatTeleport;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.LiteralText;
import net.dvornick.cattp.util.IEntityDataSaver;

public class DeleteTeleportPointCommand {

    public static final String POINT_KEY = "pointPos_";
    public static final String POINTS_STORAGE = "TeleportPointsStorage";
    public static final String INITED = "TeleportPlayerInformation";

    public static void register() {
        ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("del-point")
                .then(ClientCommandManager.argument("name", StringArgumentType.string()).executes(DeleteTeleportPointCommand::run)));
    }

    private static int run(CommandContext<FabricClientCommandSource> context) {
        IEntityDataSaver player = (IEntityDataSaver) context.getSource().getPlayer();
        String name = context.getArgument("name", String.class);

        if (!player.getPersistentData().contains(INITED)) {
            InitCatTeleport.initialize(player, context);
        }

        if (player.getPersistentData().contains(POINT_KEY + name)) {
            player.getPersistentData().remove(POINT_KEY + name);
            if (player.getPersistentData().getString(POINTS_STORAGE).contains("&" + name + "&")) {
                String points = player.getPersistentData().getString(POINTS_STORAGE);
                points = points.replaceAll( name + "&", "");
                player.getPersistentData().putString(POINTS_STORAGE, points);
            }
            context.getSource().sendFeedback(new LiteralText("Point " + name + " has been successfully deleted"));
            return 1;
        }

        context.getSource().sendFeedback(new LiteralText("Point " + name + " not found"));
        return -1;
    }


}