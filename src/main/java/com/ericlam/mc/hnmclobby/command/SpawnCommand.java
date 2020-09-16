package com.ericlam.mc.hnmclobby.command;

import com.ericlam.mc.hnmclobby.config.MessagesConfig;
import com.ericlam.mc.hnmclobby.manager.LobbyManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand extends SettingsCommandNode {

    private final LobbyManager lobby;

    public SpawnCommand(LobbyManager lobby) {
        super("spawn", "傳送到大堂重生點", null);
        this.lobby = lobby;
    }

    @Override
    public void executeSettings(Player name, CommandSender sender, MessagesConfig config) {
        if (sender == name) {
            if (lobby.TeleportToLobby(name)) {
                name.sendMessage(config.getSpawn("send"));
            }
        } else {
            lobby.TeleportToLobby(name, sender);
        }

    }
}
