package com.hypernite.mc.hnmc.lobby.ericlam.addon.config;

import com.hypernite.mc.hnmc.core.config.yaml.Configuration;
import com.hypernite.mc.hnmc.core.config.yaml.Resource;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

@Resource(locate = "Lobby.yml")
public class LobbyConfig extends Configuration {
    public Location spawntp;
    public Map<Material, LobbyItem> joinItems;

    public static class LobbyItem {
        public int slot;
        public String name;
        public List<String> lores;
        public String command;
    }
}
