package com.ericlam.addon;

import com.hypernite.config.ConfigGenerator;
import main.HyperNiteMC;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ConfigManager extends ConfigGenerator {
    private static ConfigManager var;
    public FileConfiguration messagefile;
    public FileConfiguration config;
    public FileConfiguration lobbyfile;
    public String header;
    private List<JoinItem> joinItems = new ArrayList<>();

    public ConfigManager(Plugin plugin) {
        super(plugin, "Messages.yml","config.yml","Lobby.yml");
        this.messagefile = ymls.get("Messages.yml");
        this.config = ymls.get("config.yml");
        this.lobbyfile = ymls.get("Lobby.yml");
        setMsgConfig("Messages.yml");
    }

    public void loadConfig(){
        header = lobbyfile.getString("tablist-header");
        Set<String> join_items = lobbyfile.getConfigurationSection("join-items").getKeys(false);
        for (String item : join_items) {
            Material material = Material.valueOf(item);
            String name = lobbyfile.getString("join-items." + item + ".name");
            List<String> lores = lobbyfile.getStringList("join-items." + item + ".lores");
            String command = lobbyfile.getString("join-items." + item + ".command");
            int slot = lobbyfile.getInt("join-items." + item + ".slot");
            joinItems.add(new JoinItem(name, lores, command, material, slot));
        }
    }

    List<JoinItem> getJoinItems() {
        return joinItems;
    }

    public boolean isMySQL(){
        return config.getBoolean("General.Use-MySQL");
    }
    String title(){
         return getPureMessage("Commands.GUI.title");
    }
    public String noperm(){
        return getMessage("General.No-Perm");
    }

    public String prefix(){
        return var.getPureMessage("General.Prefix");
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
