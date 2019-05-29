package com.hypernite.mc.hnmc.lobby.ericlam.command;

import com.hypernite.mc.hnmc.core.managers.ConfigManager;
import com.hypernite.mc.hnmc.lobby.caxerx.PlayerSettingManager;
import com.hypernite.mc.hnmc.lobby.ericlam.addon.LobbyConfig;
import com.hypernite.mc.hnmc.lobby.main.HNMCLobby;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.UUID;

public class HidePlayerCommand extends SettingsCommandNode {
    public HidePlayerCommand() {
        super("hideplayer", "隱藏所有玩家");
    }

    @Override
    public void executeSettings(Player name, CommandSender sender, ConfigManager var, boolean isMySQL) throws IOException {
        UUID puuid = name.getUniqueId();
        PlayerSettingManager psm = PlayerSettingManager.getInstance();
        boolean nohide = !psm.getPlayerSetting(puuid).isHidePlayer();
        if (sender != name) sender.sendMessage(var.getMessage("Commands.HidePlayer.be-" + (nohide ? "hide":"show")).replace("<player>",name.getName()));
        name.sendMessage(var.getMessage("Commands.HidePlayer." + (nohide ? "hide":"show")));
        psm.getPlayerSetting(puuid).setHidePlayer(nohide);
        for (Player onlinep : Bukkit.getServer().getOnlinePlayers()) {
            if (nohide) {
                name.hidePlayer(HNMCLobby.plugin, onlinep);
            } else {
                name.showPlayer(HNMCLobby.plugin, onlinep);
            }
        }
        if (!isMySQL) LobbyConfig.setYml("HidePlayer", puuid, nohide);
    }
}
