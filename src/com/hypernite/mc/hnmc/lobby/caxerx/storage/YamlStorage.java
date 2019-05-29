package com.hypernite.mc.hnmc.lobby.caxerx.storage;

import com.hypernite.mc.hnmc.lobby.caxerx.PlayerConfigStatus;
import com.hypernite.mc.hnmc.lobby.main.HNMCLobby;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class YamlStorage implements PlayerStatusStorage {
    private final File dataFolder;

    public YamlStorage(Plugin plugin) {
        dataFolder = new File(HNMCLobby.plugin.getDataFolder(), "PlayerData");
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }
    }

    @Override
    public PlayerConfigStatus getPlayerSetting(UUID player) {
        File playerFile = new File(dataFolder, player.toString() + ".yml");
        PlayerConfigStatus setting = new PlayerConfigStatus();
        if (playerFile.exists()) {
            YamlConfiguration fileConfig = YamlConfiguration.loadConfiguration(playerFile);
            boolean fly = fileConfig.getBoolean("Flight", false);
            boolean hideChat = fileConfig.getBoolean("HideChat", false);
            boolean stacker = fileConfig.getBoolean("Stacker", false);
            boolean speed = fileConfig.getBoolean("Speed", false);
            boolean hidePlayer = fileConfig.getBoolean("HidePlayer", false);
            setting = new PlayerConfigStatus(fly, hideChat, hidePlayer, stacker, speed);
        }
        return setting;
    }

    @Override
    public void savePlayerSetting(UUID player, PlayerConfigStatus config) {
        File playerFile = new File(dataFolder, player.toString() + ".yml");
        YamlConfiguration yml = new YamlConfiguration();
        yml.set("Flight", config.isFly());
        yml.set("HideChat", config.isHideChat());
        yml.set("Stacker", config.isStacker());
        yml.set("Speed", config.isSpeed());
        yml.set("HidePlayer", config.isHidePlayer());
        try {
            yml.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void savePlayerSetting(Map<UUID, PlayerConfigStatus> configs) {
        for (UUID player : configs.keySet()) {
            PlayerConfigStatus config = configs.get(player);
            if (config.isChanged()) {
                savePlayerSetting(player, config);
            }
        }
    }
}
