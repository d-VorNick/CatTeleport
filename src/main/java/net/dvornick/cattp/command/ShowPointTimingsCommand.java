package net.dvornick.cattp.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.dvornick.cattp.events.InitCatTeleport;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.LiteralText;
import net.dvornick.cattp.util.IEntityDataSaver;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

public class ShowPointTimingsCommand {

    public static final String POINT_KEY = "pointPos_";
    public static final String INITED = "TeleportPlayerInformation";


    public static void register() {
        ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("point-info")
                .then(ClientCommandManager.argument("name", StringArgumentType.string()).executes(ShowPointTimingsCommand::run)));
    }

    private static int run(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        IEntityDataSaver player = (IEntityDataSaver) context.getSource().getPlayer();
        String name = context.getArgument("name", String.class);

        if (!player.getPersistentData().contains(INITED)) {
            InitCatTeleport.initialize(player, context);
        }
        int[] time = player.getPersistentData().getIntArray(POINT_KEY + name);
        Style style = new LiteralText(name).getStyle();
        Style gold = style.withColor(Formatting.GOLD);
        Style red = style.withColor(Formatting.RED);
        if (player.getPersistentData().contains(POINT_KEY + name)) {


            context.getSource().sendFeedback(new LiteralText("Timings for point " + name + ": from " + time[0] + " to " + time[1]).setStyle(gold));
            return 1;
        }

        context.getSource().sendFeedback(new LiteralText("Point " + name + " not found").setStyle(red));
        return -1;
    }


}