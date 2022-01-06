package net.dvornick.cattp.util;

import net.dvornick.cattp.command.AddTeleportPointCommand;
import net.dvornick.cattp.command.ShowDataCommand;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.dvornick.cattp.command.DisconnectCommand;

public class ModCommandRegister {
    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(DisconnectCommand::register);
        CommandRegistrationCallback.EVENT.register(ShowDataCommand::register);
       // CommandRegistrationCallback.EVENT.register(AddTeleportPointCommand::register);
    }
}