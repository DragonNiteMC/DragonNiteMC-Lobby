package com.ericlam.mc.dnmclobby.manager;

import com.ericlam.mc.dnmclobby.PlayerConfigStatus;
import com.ericlam.mc.dnmclobby.storage.PlayerStatusStorage;
import com.ericlam.mc.dnmclobby.storage.SQLDatabaseStorage;

import java.util.HashMap;
import java.util.UUID;

public class PlayerSettingManager {
    private static PlayerSettingManager instance;
    private final HashMap<UUID, PlayerConfigStatus> playerSettingMap;

    private final PlayerStatusStorage storage;

    public PlayerSettingManager() {
        playerSettingMap = new HashMap<>();
        storage = new SQLDatabaseStorage();
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