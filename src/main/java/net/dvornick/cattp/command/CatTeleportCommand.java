package net.dvornick.cattp.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.dvornick.cattp.events.InitCatTeleport;
import net.dvornick.cattp.util.IEntityDataSaver;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.resource.ResourcePack;
import net.minecraft.text.LiteralText;

import java.util.concurrent.TimeUnit;

public class CatTeleportCommand {

    public static final String POINT_KEY = "pointPos_";
    public static final String INITED = "TeleportPlayerInformation";


    public static void register() {
        ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("cat-tp")
                .then(ClientCommandManager.argument("name", StringArgumentType.string()).executes(CatTeleportCommand::run)));

    }

    private static <EntityPlayerMP> int run(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        try {
            IEntityDataSaver player = (IEntityDataSaver) context.getSource().getPlayer();
            String name = context.getArgument("name", String.class);

            if (!player.getPersistentData().contains(INITED)) {
                InitCatTeleport.initialize(player, context);
            }

            if (player.getPersistentData().contains(POINT_KEY + name)) {
                int[] time = player.getPersistentData().getIntArray(POINT_KEY + name);
                context.getSource().sendFeedback(new LiteralText("Teleporting..."));
                ClientPlayerEntity client = context.getSource().getPlayer();
                client.clientWorld.disconnect();
                ServerInfo serverInfo =  new ServerInfo("Season7", "IP:port", true);
                serverInfo.setResourcePackPolicy(ServerInfo.ResourcePackPolicy.ENABLED);
                //Thread.sleep((time[0] + time[1]) * 1000L / 2);
                TimeUnit.MILLISECONDS.sleep((time[0] + time[1]) * 1000L / 2);
                ConnectScreen.connect(new DownloadingTerrainScreen(), context.getSource().getClient(), new ServerAddress("IP", 22222), serverInfo);

                return 1;
            }

            context.getSource().sendFeedback(new LiteralText("Point " + name + " not found"));
            return -1;
        } catch (InterruptedException e) {
            return -2;
        }
    }

}