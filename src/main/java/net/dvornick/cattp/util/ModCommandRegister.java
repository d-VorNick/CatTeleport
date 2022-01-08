package net.dvornick.cattp.util;

import net.dvornick.cattp.command.*;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

public class ModCommandRegister {
    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(DisconnectCommand::register);
        CommandRegistrationCallback.EVENT.register(ShowDataCommand::register);




        AddTeleportPointCommand.register();
        DeleteTeleportPointCommand.register();
        ShowPointTimingsCommand.register();
        UpdatePointTimingCommand.register();
        ShowAllPointsCommand.register();
    }
}