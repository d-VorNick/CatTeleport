package net.dvornick.cattp.events;

import com.mojang.brigadier.context.CommandContext;
import net.dvornick.cattp.util.IEntityDataSaver;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.LiteralText;

import java.io.*;

public class InitCatTeleport {

    public static final String POINT_KEY = "pointPos_";
    public static final String POINTS_STORAGE = "TeleportPointsStorage";
    public static final String INITED = "TeleportPlayerInformation";
    //public static final String DATA = "..\\playerdata\\playerdata.txt";
    public static final String DATA = "playerdata.txt";


    public static void initialize(IEntityDataSaver player, CommandContext<FabricClientCommandSource> context) {
        try {
            File file = new File(DATA);
            file.createNewFile();

            Reader reader = new FileReader(file);
            BufferedReader buffReader = new BufferedReader(reader);

            //FileInputStream fileInputStream = new FileInputStream(DATA);


            StringBuilder fpoints = new StringBuilder();
            fpoints.append("&");
            String data = "";
            data = buffReader.readLine();
            context.getSource().sendFeedback(new LiteralText(data + "pzzzz"));
            if (data != null) {
                String[] splitData = data.split(" ");
                for (int i = 1; i < (splitData.length - 1) / 3 + 1; i++) {
                    String fname = splitData[(i - 1) * 3 + 1];
                    int ffrom = Integer.parseInt(splitData[(i - 1) * 3 + 2]);
                    int fto = Integer.parseInt(splitData[(i - 1) * 3 + 3]);
                    int[] timing = {ffrom, fto};
                    player.getPersistentData().putIntArray(POINT_KEY + fname, timing);
                    fpoints.append(fname).append("&");
                    player.getPersistentData().putString(POINTS_STORAGE, fpoints.toString());
                }
            }
            player.getPersistentData().putBoolean(INITED, true);
            reader.close();
            buffReader.close();
        } catch (IOException e) {
            context.getSource().sendFeedback(new LiteralText("IOException"));
        }
    }
}
