package addon.ericlam;

import functions.hypernite.mc.Functions;
import main.ericlam.PlayerSettings;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Variable {
    private static Variable plugin;
    private Functions fs = new Functions(PlayerSettings.plugin);
    private static Variable var;
    public static FileConfiguration messagefile = YamlConfiguration.loadConfiguration(new File(PlayerSettings.plugin.getDataFolder(), "Messages.yml"));
    public static FileConfiguration config = YamlConfiguration.loadConfiguration(new File(PlayerSettings.plugin.getDataFolder(), "config.yml"));
    public static Variable getInstance() {
        if (var == null) var = new Variable();
        return var;
    }
    public Functions getFs(){
        return fs;
    }
    public String prefix(){
        return fs.returnColoredMessage(messagefile, "General.Prefix");
    }
    public String title(){
         return fs.returnColoredMessage(messagefile, "Commands.GUI.title");
    }
    public String noperm(){
        return fs.returnColoredMessage(messagefile, "General.No-Perm");
    }
    private static String savingtype = config.getString("General.Saving-Type");
    public static boolean yaml = savingtype.equalsIgnoreCase("YAML");
    public static boolean mysql = savingtype.equalsIgnoreCase("SQL");

    synchronized public static void setYml(String YmlName, UUID puuid,boolean status) throws IOException {
        File data = new File(PlayerSettings.plugin.getDataFolder(), "PlayerData/"+puuid.toString()+".yml");
        FileConfiguration yml = YamlConfiguration.loadConfiguration(data);
        yml.set(YmlName,status);
        yml.save(data);
    }
    public static FileConfiguration uuidYml(UUID uuid){
        return YamlConfiguration.loadConfiguration(new File(PlayerSettings.plugin.getDataFolder(), "PlayerData/"+uuid.toString()+".yml"));
    }
}
