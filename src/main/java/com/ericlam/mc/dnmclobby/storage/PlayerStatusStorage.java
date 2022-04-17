package com.ericlam.mc.dnmclobby.storage;

import com.ericlam.mc.dnmclobby.PlayerConfigStatus;

import java.util.Map;
import java.util.UUID;

public interface PlayerStatusStorage {
    PlayerConfigStatus getPlayerSetting(UUID player);

    void savePlayerSetting(UUID player, PlayerConfigStatus config);

    void savePlayerSetting(Map<UUID, PlayerConfigStatus> configs);
}
