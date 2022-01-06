package net.dvornick.cattp.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.sun.jdi.connect.Connector;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.command.argument.Vec2ArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.TeleportCommand;
import net.minecraft.text.LiteralText;
import net.dvornick.cattp.util.IEntityDataSaver;

public class AddTeleportPointCommand {

    public static final String POINT_KEY = "pointPos_";

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, boolean dedicated) {
        dispatcher.register(ClientCommandManager.literal("add-point")
                .then(ClientCommandManager.argument("name", StringArgumentType.string())
                        .then(ClientCommandManager.argument("time", Vec2ArgumentType.vec2()).executes(AddTeleportPointCommand::run))));
    }

    private static int run(CommandContext<FabricClientCommandSource> context) {
        IEntityDataSaver player = (IEntityDataSaver)context.getSource().getPlayer();
        //StringArgumentType name = context.getArgument("name", );
        //player.getPersistentData().putString(POINT_KEY + name, String.valueOf(name));
        context.getSource().sendFeedback(new LiteralText("Set point "));
        return 1;
    }

}