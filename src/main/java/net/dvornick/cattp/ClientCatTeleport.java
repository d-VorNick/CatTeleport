package net.dvornick.cattp;
import net.dvornick.cattp.util.ModCommandRegister;
import net.dvornick.cattp.util.ModEventsRegister;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.minecraft.server.command.HelpCommand;
import net.minecraft.text.LiteralText;


@Environment(EnvType.CLIENT)
public class ClientCatTeleport implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModCommandRegister.registerCommands();
        ModEventsRegister.registerEvents();

    }
}