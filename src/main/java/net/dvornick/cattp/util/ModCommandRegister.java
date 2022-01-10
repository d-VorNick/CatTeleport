package net.dvornick.cattp.util;

import net.dvornick.cattp.command.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

public class ModCommandRegister {
    public static void registerCommands() {
        AddTeleportPointCommand.register();
        DeleteTeleportPointCommand.register();
        ShowPointTimingsCommand.register();
        UpdatePointTimingCommand.register();
        ShowAllPointsCommand.register();
        CatTeleportCommand.register();
    }
}