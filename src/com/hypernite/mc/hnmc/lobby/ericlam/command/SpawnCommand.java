package com.hypernite.mc.hnmc.lobby.ericlam.command;

import com.hypernite.mc.hnmc.lobby.ericlam.addon.TeleportLobby;
import com.hypernite.mc.hnmc.lobby.ericlam.addon.config.MessagesConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand extends SettingsCommandNode {

    public SpawnCommand() {
        super("spawn", "傳送到大堂重生點");
    }

    @Override
    public void executeSettings(Player name, CommandSender sender, MessagesConfig config) {
        TeleportLobby lobby = TeleportLobby.getInstance();
        if (sender == name){
            if (lobby.TeleportToLobby(name)){
                name.sendMessage(config.getCommandMSG().getSpawn().get("send"));
            }
        }else{
            lobby.TeleportToLobby(name,sender);
        }

    }
}
