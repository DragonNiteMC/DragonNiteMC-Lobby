package com.ericlam.mc.hnmclobby.storage;

import com.ericlam.mc.hnmclobby.PlayerConfigStatus;
import com.hypernite.mc.hnmc.core.main.HyperNiteMC;
import com.hypernite.mc.hnmc.core.managers.SQLDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class SQLDatabaseStorage implements PlayerStatusStorage {
    private final SQLDataSource dataSourceManager;

    public SQLDatabaseStorage() {
        this.dataSourceManager = HyperNiteMC.getAPI().getSQLDataSource();
        try (Connection connection = dataSourceManager.getConnection(); PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `Lobby_settings` (`PlayerUUID` VARCHAR(40) NOT NULL PRIMARY KEY, `Fly` bit, `HideChat` bit, `Stacker` bit, `Speed` bit, `HidePlayer` bit)")) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PlayerConfigStatus getPlayerSetting(UUID player) {
        PlayerConfigStatus setting = new PlayerConfigStatus();
        try (Connection connection = dataSourceManager.getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM `Lobby_settings` WHERE `PlayerUUID` = ?")) {
            statement.setString(1, player + "");
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                boolean fly = result.getBoolean("Fly");
                boolean hideChat = result.getBoolean("HideChat");
                boolean stacker = result.getBoolean("Stacker");
                boolean speed = result.getBoolean("Speed");
                boolean hidePlayer = result.getBoolean("HidePlayer");
                setting = new PlayerConfigStatus(fly, hideChat, hidePlayer, stacker, speed);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return setting;
    }

    @Override
    public void savePlayerSetting(UUID player, PlayerConfigStatus setting) {
        try (Connection connection = dataSourceManager.getConnection();
             PreparedStatement lockStatement = connection.prepareStatement("SELECT * FROM `Lobby_settings` WHERE `PlayerUUID`=? FOR UPDATE");
             PreparedStatement statement = connection.prepareStatement("INSERT INTO `Lobby_settings` VALUES(?,?,?,?,?,?) ON DUPLICATE KEY UPDATE Fly = ?, HideChat = ?, Stacker = ?, Speed = ?, HidePlayer = ?")) {
            lockStatement.setString(1, player.toString());
            lockStatement.execute();
            setStatement(statement, player, setting);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void savePlayerSetting(Map<UUID, PlayerConfigStatus> configs) {
        try (Connection connection = dataSourceManager.getConnection()) {
            for (UUID player : configs.keySet()) {
                PlayerConfigStatus setting = configs.get(player);
                if (!setting.isChanged()) continue;
                try (PreparedStatement statement = connection.prepareStatement("INSERT INTO `Lobby_settings` VALUES(?,?,?,?,?,?) ON DUPLICATE KEY UPDATE Fly = ?, HideChat = ?, Stacker = ?, Speed = ?, HidePlayer = ?")) {
                    setStatement(statement, player, setting);
                    statement.execute();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void setStatement(PreparedStatement statement, UUID player, PlayerConfigStatus setting) {
        try {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
