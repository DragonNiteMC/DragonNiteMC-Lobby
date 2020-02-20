package com.hypernite.mc.hnmc.lobby.caxerx.storage;

import com.hypernite.mc.hnmc.lobby.caxerx.PlayerConfigStatus;

import java.util.Map;
import java.util.UUID;

public interface PlayerStatusStorage {
    PlayerConfigStatus getPlayerSetting(UUID player);

    void savePlayerSetting(UUID player, PlayerConfigStatus config);

    void savePlayerSetting(Map<UUID, PlayerConfigStatus> configs);
}
