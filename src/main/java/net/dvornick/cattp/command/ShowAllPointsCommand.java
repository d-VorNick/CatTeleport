package net.dvornick.cattp.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.dvornick.cattp.events.InitCatTeleport;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.LiteralText;
import net.dvornick.cattp.util.IEntityDataSaver;

public class ShowAllPointsCommand {

    public static final String POINT_KEY = "pointPos_";
    public static final String POINTS_STORAGE = "TeleportPointsStorage";
    public static final String INITED = "TeleportPlayerInformation";


    public static void register() {
        ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("all-points").executes(ShowAllPointsCommand::run));
    }

    private static int run(CommandContext<FabricClientCommandSource> context) {
        IEntityDataSaver player = (IEntityDataSaver) context.getSource().getPlayer();
        if (!player.getPersistentData().contains(INITED)) {
            InitCatTeleport.initialize(player, context);
        }
        if (!player.getPersistentData().contains(POINTS_STORAGE)) {
            context.getSource().sendFeedback(new LiteralText("You don't have any teleport points"));
            return 1;
        }

        String points = player.getPersistentData().getString(POINTS_STORAGE);
        String[] allPoints = points.split("&");
        int countPoints = allPoints.length - 1;
        if (countPoints < 1) {
            context.getSource().sendFeedback(new LiteralText("You don't have any teleport points"));
            return 1;
        }

        StringBuilder answer = new StringBuilder("You have " + (allPoints.length - 1) + " teleport points:\n");

        for (int i = 1; i < allPoints.length; i ++) {
            int[] point = player.getPersistentData().getIntArray(POINT_KEY + allPoints[i]);
            answer.append(allPoints[i]).append(" with timings from ").append(point[0]).append(" to ").append(point[1]).append("\n");
        }
        context.getSource().sendFeedback(new LiteralText(answer.toString()));
        return 1;
    }


}