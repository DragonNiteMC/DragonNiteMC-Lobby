package com.hypernite.mc.hnmc.lobby.ericlam.command;

import com.hypernite.mc.hnmc.lobby.caxerx.PlayerSettingManager;
import com.hypernite.mc.hnmc.lobby.ericlam.addon.config.MessagesConfig;
import com.hypernite.mc.hnmc.lobby.main.HNMCLobby;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class HidePlayerCommand extends SettingsCommandNode {
    public HidePlayerCommand() {
        super("hideplayer", "隱藏所有玩家");
    }

    @Override
    public void executeSettings(Player name, CommandSender sender, MessagesConfig config) {
        UUID puuid = name.getUniqueId();
        PlayerSettingManager psm = PlayerSettingManager.getInstance();
        boolean nohide = !psm.getPlayerSetting(puuid).isHidePlayer();
        if (sender != name)
            sender.sendMessage(nohide ? config.getBeHide("HidePlayer") : config.getBeShow("HidePlayer").replace("<player>", name.getName()));
        name.sendMessage(nohide ? config.getHide("HidePlayer") : config.getShow("HidePlayer"));
        psm.getPlayerSetting(puuid).setHidePlayer(nohide);
        for (Player onlinep : Bukkit.getServer().getOnlinePlayers()) {
            if (nohide) {
                name.hidePlayer(HNMCLobby.plugin, onlinep);
            } else {
                name.showPlayer(HNMCLobby.plugin, onlinep);
            }
        }
    }
}
