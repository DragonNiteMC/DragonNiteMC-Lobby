package addon.ericlam;

import main.ericlam.PlayerSettings;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import static main.ericlam.PlayerSettings.plugin;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Variable {
    public static Variable var;
    public static FileConfiguration messagefile = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "Messages.yml"));
    public static FileConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml"));
    public static String prefix = PlayerSettings.returnColoredMessage("General.Prefix");
    public static String noperm = PlayerSettings.returnColoredMessage("General.No-Perm");
    private static String savingtype = config.getString("General.Saving-Type");
    public static boolean yaml = savingtype.equalsIgnoreCase("YAML");
    public static boolean MYsql = savingtype.equalsIgnoreCase("SQL");
    public static String table = config.getString("General.MySQL.table");
    synchronized public static void setYml(String YmlName, UUID puuid,boolean status) throws IOException {
        File data = new File(plugin.getDataFolder(), "PlayerData/"+puuid.toString()+".yml");
        FileConfiguration yml = YamlConfiguration.loadConfiguration(data);
        yml.set(YmlName,status);
        yml.save(data);
    }
    public static FileConfiguration uuidYml(UUID uuid){
        return YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "PlayerData/"+uuid.toString()+".yml"));
    }
}
