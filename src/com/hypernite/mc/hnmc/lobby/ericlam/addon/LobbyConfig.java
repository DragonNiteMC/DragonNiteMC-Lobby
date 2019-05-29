package com.hypernite.mc.hnmc.lobby.ericlam.addon;

import com.hypernite.mc.hnmc.core.config.ConfigSetter;
import com.hypernite.mc.hnmc.lobby.main.HNMCLobby;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class LobbyConfig extends ConfigSetter {
    FileConfiguration messagefile;
    public FileConfiguration config;
    public FileConfiguration lobbyfile;
    public String header;
    private List<JoinItem> joinItems = new ArrayList<>();
    private List<String> test = List.of("a","d","dawda","Dawdaw","awdawdwa");

    public LobbyConfig(Plugin plugin) {
        super(plugin, "Messages.yml","config.yml","Lobby.yml");
    }

    @Override
    public void loadConfig(Map<String, FileConfiguration> map) {
        this.messagefile = map.get("Messages.yml");
        this.config = map.get("config.yml");
        this.lobbyfile = map.get("Lobby.yml");
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

    @Override
    public Map<String, Object> variablesMap() {
        return Map.of("header",header,"join_items",joinItems,"mysql",isMySQL(),"test",test);
    }

    List<JoinItem> getJoinItems() {
        return joinItems;
    }

    public boolean isMySQL(){
        return config.getBoolean("General.Use-MySQL");
    }


    public static void setYml(String YmlName, UUID puuid,boolean status) throws IOException {
        File data = new File(HNMCLobby.plugin.getDataFolder(), "PlayerData/"+puuid.toString()+".yml");
        FileConfiguration yml = YamlConfiguration.loadConfiguration(data);
        yml.set(YmlName,status);
        yml.save(data);
    }

    public static FileConfiguration uuidYml(UUID uuid){
        return YamlConfiguration.loadConfiguration(new File(HNMCLobby.plugin.getDataFolder(), "PlayerData/"+uuid.toString()+".yml"));
    }


}
