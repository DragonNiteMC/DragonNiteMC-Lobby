package addon.ericlam;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import static main.ericlam.PlayerSettings.plugin;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Variable {
    public static Variable var;
    public static FileConfiguration messagefile = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "Messages.yml"));
    public static FileConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml"));
    public static String prefix = returnColoredMessage("General.Prefix");
    public static String noperm = returnColoredMessage("General.No-Perm");
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

    public static String returnColoredMessage(String messagePath) {
        String path = messagefile.getString(messagePath);
        return ChatColor.translateAlternateColorCodes('&', path);
    }

    public static List<String> returnColoredStringList(String messagePath){
        List<String> path = messagefile.getStringList(messagePath);
        List<String> list = new ArrayList<>();
        for (String ReturnMSG : path){
            list.add(ChatColor.translateAlternateColorCodes('&',ReturnMSG));
        }
        return list;
    }

    public static void renametoUUID(UUID puuid, Player player) throws IOException {
        File folder = new File(plugin.getDataFolder(), "PlayerData" + File.separator);
        File filename = new File(plugin.getDataFolder(),  "PlayerData"+File.separator + puuid.toString() + ".yml");
        if (!folder.exists()) folder.mkdir();
        filename.createNewFile();
        YamlConfiguration.loadConfiguration(filename);
    }
}
