package net.dvornick.cattp.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.dvornick.cattp.events.InitCatTeleport;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.LiteralText;
import net.dvornick.cattp.util.IEntityDataSaver;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class AddTeleportPointCommand {

    public static final String POINT_KEY = "pointPos_";
    public static final String POINTS_STORAGE = "TeleportPointsStorage";
    public static final String INITED = "TeleportPlayerInformation";
    //public static final String DATA = "..\\playerdata\\playerdata.txt";
    public static final String DATA = "mods\\playerdata.txt";

    public static void register() {
        ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("add-point")
                .then(ClientCommandManager.argument("name", StringArgumentType.string())
                        .then(ClientCommandManager.argument("from", IntegerArgumentType.integer())
                                .then(ClientCommandManager.argument("to", IntegerArgumentType.integer()).executes(AddTeleportPointCommand::run)))));
    }

    private static int run(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        try {
            IEntityDataSaver player = (IEntityDataSaver) context.getSource().getPlayer();
            String name = context.getArgument("name", String.class);
            Integer from = context.getArgument("from", Integer.class);
            Integer to = context.getArgument("to", Integer.class);


            if (!player.getPersistentData().contains(INITED)) {
                InitCatTeleport.initialize(player, context);
            }

            Style style = new LiteralText(name).getStyle();
            Style gold = style.withColor(Formatting.GOLD);
            Style red = style.withColor(Formatting.RED);

            if (name.length() > 20) {
                context.getSource().sendFeedback(new LiteralText("Name must contain less than 20 characters").setStyle(red));
                return -1;
            }

            if (name.contains("&")) {
                context.getSource().sendFeedback(new LiteralText("Remove ampersand from name").setStyle(red));
                return -1;
            }

            int[] time = {from, to};
            if (player.getPersistentData().contains(POINT_KEY + name)) {
                context.getSource().sendFeedback(new LiteralText("Point " + name + " already exists. \n" +
                        "Use a different name or command /update to change the timings").setStyle(red));
                return -1;
            }

            player.getPersistentData().putIntArray(POINT_KEY + name, time);

            FileInputStream fileInputStream = new FileInputStream(DATA);


            if (!player.getPersistentData().contains(POINTS_STORAGE)) {
                player.getPersistentData().putString(POINTS_STORAGE, "&" + name + "&");
                FileOutputStream fileOutputStream = new FileOutputStream(DATA);
                fileOutputStream.write((" " + name + " " + from + " " + to + " ").getBytes());
                fileOutputStream.close();
            } else {
                String points = player.getPersistentData().getString(POINTS_STORAGE);

                points = points + name + "&";
                player.getPersistentData().putString(POINTS_STORAGE, points);

                StringBuilder fstr = new StringBuilder();

                int c;
                while((c=fileInputStream.read())!= -1){
                    fstr.append((char) c);
                }
                fileInputStream.close();

                fstr.append(name).append(" ").append(from).append(" ").append(to).append(" ");
                FileOutputStream fileOutputStream = new FileOutputStream(DATA);
                fileOutputStream.write(fstr.toString().getBytes());
                fileOutputStream.close();
            }

            context.getSource().sendFeedback(new LiteralText("Set point " + name + " with timings from " + from + " to " + to).setStyle(gold));
            return 1;

        } catch (IOException e) {
            context.getSource().sendFeedback(new LiteralText("IOException"));
            return -1;
        }
    }


}