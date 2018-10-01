package addon.ericlam;

import functions.hypernite.mc.Functions;
import main.ericlam.HyperNiteMC;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Variable {
    private static Variable plugin;
    private Functions fs = new Functions(HyperNiteMC.plugin);
    private static Variable var;
    public static FileConfiguration messagefile;
    public static FileConfiguration config;
    public static FileConfiguration lobbyfile;

    public Variable(){
        messagefile = YamlConfiguration.loadConfiguration(new File(HyperNiteMC.plugin.getDataFolder(), "Messages.yml"));
        config = YamlConfiguration.loadConfiguration(new File(HyperNiteMC.plugin.getDataFolder(), "config.yml"));
        lobbyfile = YamlConfiguration.loadConfiguration(new File(HyperNiteMC.plugin.getDataFolder(), "Lobby.yml"));
    }

    public boolean isYaml(){
        return config.getString("General.Saving-Type").equalsIgnoreCase("yaml");
    }

    public boolean isMySQL(){
        return config.getString("General.Saving-Type").equalsIgnoreCase("sql");
    }


    public Functions getFs(){
        return fs;
    }
    public String prefix(){
        return fs.returnColoredMessage(messagefile, "General.Prefix");
    }
    String title(){
         return fs.returnColoredMessage(messagefile, "Commands.GUI.title");
    }
    public String noperm(){
        return fs.returnColoredMessage(messagefile, "General.No-Perm");
    }


    public static void setYml(String YmlName, UUID puuid,boolean status) throws IOException {
        File data = new File(HyperNiteMC.plugin.getDataFolder(), "PlayerData/"+puuid.toString()+".yml");
        FileConfiguration yml = YamlConfiguration.loadConfiguration(data);
        yml.set(YmlName,status);
        yml.save(data);
    }


    public static FileConfiguration uuidYml(UUID uuid){
        return YamlConfiguration.loadConfiguration(new File(HyperNiteMC.plugin.getDataFolder(), "PlayerData/"+uuid.toString()+".yml"));
    }
}
