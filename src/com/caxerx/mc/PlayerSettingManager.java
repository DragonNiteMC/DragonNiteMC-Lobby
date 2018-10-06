package com.caxerx.mc;

import addon.ericlam.Variable;
import main.ericlam.HyperNiteMC;
import mysql.hypernite.mc.SQLDataSourceManager;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class PlayerSettingManager {
    private SQLDataSourceManager dsm;
    private Plugin plugin;
    private Connection connection;
    private HashMap<UUID, PlayerConfigStatus> playerSettingMap;
    private static PlayerSettingManager playerSettingManager;

    private PlayerSettingManager() throws SQLException {
        plugin = HyperNiteMC.plugin;
        playerSettingMap = new HashMap<>();
    }

    public HashMap<UUID, PlayerConfigStatus> getPlayerSettingMap() {
        return playerSettingMap;
    }

    public void needMySQL() throws SQLException {
        Variable var = new Variable();
        if (var.isMySQL()){
            this.dsm = SQLDataSourceManager.getInstance();
            connection = dsm.getFuckingConnection();
        }
    }

    public static PlayerSettingManager getInstance() {
        try {
            if (playerSettingManager == null) {
                playerSettingManager = new PlayerSettingManager();
            }
            return playerSettingManager;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error on SQL Load");
        }
    }

    public PlayerConfigStatus getPlayerSetting(UUID player) throws SQLException {
        if (playerSettingMap.containsKey(player)) return playerSettingMap.get(player);
        PlayerConfigStatus setting;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM `PS_stats` WHERE `PlayerUUID` = ?")) {
            statement.setString(1, player + "");
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                boolean fly = result.getBoolean("Fly");
                boolean hideChat = result.getBoolean("HideChat");
                boolean stacker = result.getBoolean("Stacker");
                boolean speed = result.getBoolean("Speed");
                boolean hidePlayer = result.getBoolean("HidePlayer");
                setting = new PlayerConfigStatus(fly, hideChat, hidePlayer, stacker, speed);
            } else {
                setting = new PlayerConfigStatus(false, false, false, false, false);
            }
        }
        playerSettingMap.put(player, setting);
        return setting;
    }

    public PlayerConfigStatus getPlayerSettingFromYaml(UUID player) {
        if (playerSettingMap.containsKey(player)) return playerSettingMap.get(player);
        PlayerConfigStatus setting;
        if (new File(HyperNiteMC.plugin.getDataFolder(), "PlayerData/" + player.toString() + ".yml").exists()) {
            boolean fly = Variable.uuidYml(player).getBoolean("Flight") && Variable.uuidYml(player).contains("Flight");
            boolean hideChat = Variable.uuidYml(player).getBoolean("HideChat") && Variable.uuidYml(player).contains("HideChat");
            boolean stacker = Variable.uuidYml(player).getBoolean("Stacker") && Variable.uuidYml(player).contains("Stacker");
            boolean speed = Variable.uuidYml(player).getBoolean("Speed") && Variable.uuidYml(player).contains("Speed");
            boolean hidePlayer = Variable.uuidYml(player).getBoolean("HidePlayer") && Variable.uuidYml(player).contains("HidePlayer");
            setting = new PlayerConfigStatus(fly, hideChat, hidePlayer, stacker, speed);
            plugin.getLogger().info("DEBUG: found player's data folder");
        } else {
            setting = new PlayerConfigStatus(false, false, false, false, false);
            plugin.getLogger().info("DEBUG: no player's data folder found, changed all to false");
        }
        playerSettingMap.put(player, setting);
        return setting;
    }


    public void savePlayerSetting(UUID player, int retry) throws SQLException {
        if (!playerSettingMap.containsKey(player)) return;
        PlayerConfigStatus setting = playerSettingMap.get(player);
        if (!setting.isChanged()) {
            playerSettingMap.remove(player);
            return;
        }
        Variable var = new Variable();
        if(!var.isMySQL()) return;
        System.out.println("Connecting to MySQL and Changing Status...");
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO `PS_stats` VALUES(?,?,?,?,?,?) ON DUPLICATE KEY UPDATE Fly = ?, HideChat = ?, Stacker = ?, Speed = ?, HidePlayer = ?")) {
            statement.setString(1, player + "");
            statement.setBoolean(2, setting.isFly());
            statement.setBoolean(7, setting.isFly());
            statement.setBoolean(3, setting.isHideChat());
            statement.setBoolean(8, setting.isHideChat());
            statement.setBoolean(4, setting.isStacker());
            statement.setBoolean(9, setting.isStacker());
            statement.setBoolean(5, setting.isSpeed());
            statement.setBoolean(10, setting.isSpeed());
            statement.setBoolean(6, setting.isHidePlayer());
            statement.setBoolean(11, setting.isHidePlayer());
            statement.execute();
        } catch (SQLException e) {
            System.out.println("Error, retry " + retry);
            if (retry > 5) throw e;
            connection = dsm.getFuckingConnection();
            savePlayerSetting(player, retry + 1);
        }
    }

    public void saveAll() throws SQLException {
        for (UUID player : playerSettingMap.keySet()) {
            savePlayerSetting(player, 0);
        }
    }
}