package com.hypernite.mc.hnmc.lobby.ericlam.command;

import com.hypernite.mc.hnmc.core.managers.ConfigManager;
import com.hypernite.mc.hnmc.lobby.ericlam.addon.TeleportLobby;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class SpawnCommand extends SettingsCommandNode {

    public SpawnCommand() {
        super("spawn", "傳送到大堂重生點");
    }

    @Override
    public void executeSettings(Player name, CommandSender sender, ConfigManager var, boolean isMySQL) throws IOException {
        TeleportLobby lobby = TeleportLobby.getInstance();
        if (sender == name){
            if (lobby.TeleportToLobby(name)){
                name.sendMessage(var.getMessage("Commands.spawn.send"));
            }
        }else{
            lobby.TeleportToLobby(name,sender);
        }

    }
}
