package com.hypernite.mc.hnmc.lobby.ericlam.addon;

import com.hypernite.mc.hnmc.core.config.ConfigSetter;
import com.hypernite.mc.hnmc.core.config.Extract;
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

    @Extract
    public String header;

    @Extract(name = "join_items")
    private List<JoinItem> joinItems = new ArrayList<>();

    @Extract
    private boolean mysql;

    public LobbyConfig(Plugin plugin) {
        super(plugin, "Messages.yml","config.yml","Lobby.yml");
    }

    @Override
    public void loadConfig(Map<String, FileConfiguration> map) {
        this.messagefile = map.get("Messages.yml");
        this.config = map.get("config.yml");
        this.lobbyfile = map.get("Lobby.yml");
        this.mysql = config.getBoolean("General.Use-MySQL");
        header = lobbyfile.getString("tablist-header");
        Set<String> join_items = Optional.ofNullable(lobbyfile.getConfigurationSection("join-items")).map(sec->sec.getKeys(false)).orElse(Set.of());
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
