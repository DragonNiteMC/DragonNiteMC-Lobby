package com.caxerx.mc;

import com.caxerx.mc.storage.PlayerStatusStorage;
import com.caxerx.mc.storage.SQLDatabaseStorage;
import com.caxerx.mc.storage.YamlStorage;
import com.ericlam.addon.ConfigManager;
import main.HyperNiteMC;

import java.util.HashMap;
import java.util.UUID;

public class PlayerSettingManager {
    private static PlayerSettingManager instance;
    private HashMap<UUID, PlayerConfigStatus> playerSettingMap;

    private PlayerStatusStorage storage;

    private PlayerSettingManager() {
        playerSettingMap = new HashMap<>();
        ConfigManager var = ConfigManager.getInstance();
        if (var.isMySQL()) {
            storage = new SQLDatabaseStorage();
        } else {
            storage = new YamlStorage(HyperNiteMC.plugin);
        }
    }

    public static PlayerSettingManager getInstance() {
        if (instance == null) {
            instance = new PlayerSettingManager();
        }
        return instance;
    }

    public PlayerConfigStatus getPlayerSetting(UUID player) {
        if (playerSettingMap.containsKey(player)) return playerSettingMap.get(player);
        PlayerConfigStatus setting = storage.getPlayerSetting(player);
        playerSettingMap.put(player, setting);
        return setting;
    }


    public void savePlayerSetting(UUID player) {
        if (!playerSettingMap.containsKey(player)) return;
        PlayerConfigStatus setting = playerSettingMap.get(player);
        if (!setting.isChanged()) {
            playerSettingMap.remove(player);
        }
    }

    public void saveAll() {
        storage.savePlayerSetting(playerSettingMap);
    }
}