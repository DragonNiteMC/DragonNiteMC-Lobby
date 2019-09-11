package com.hypernite.mc.hnmc.lobby.caxerx;

import com.hypernite.mc.hnmc.lobby.caxerx.storage.PlayerStatusStorage;
import com.hypernite.mc.hnmc.lobby.caxerx.storage.SQLDatabaseStorage;
import com.hypernite.mc.hnmc.lobby.main.HNMCLobby;

import java.util.HashMap;
import java.util.UUID;

public class PlayerSettingManager {
    private static PlayerSettingManager instance;
    private HashMap<UUID, PlayerConfigStatus> playerSettingMap;

    private PlayerStatusStorage storage;

    private PlayerSettingManager() {
        playerSettingMap = new HashMap<>();
        if (HNMCLobby.isMySQL) {
            storage = new SQLDatabaseStorage();
        } else {
            throw new UnsupportedOperationException("cannot save in yaml");
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