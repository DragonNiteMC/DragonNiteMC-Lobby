package com.ericlam.mc.dnmclobby.command;

import com.ericlam.mc.dnmclobby.config.MessagesConfig;
import com.ericlam.mc.dnmclobby.manager.PlayerSettingManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class HidePlayerCommand extends SettingsCommandNode {
    private final Plugin plugin;

    public HidePlayerCommand(PlayerSettingManager psm, Plugin plugin) {
        super("hideplayer", "隱藏所有玩家", psm);
        this.plugin = plugin;
    }

    @Override
    public void executeSettings(Player name, CommandSender sender, MessagesConfig config) {
        UUID puuid = name.getUniqueId();
        boolean nohide = !psm.getPlayerSetting(puuid).isHidePlayer();
        if (sender != name)
            sender.sendMessage(nohide ? config.getBeHide("HidePlayer") : config.getBeShow("HidePlayer").replace("<player>", name.getName()));
        name.sendMessage(nohide ? config.getHide("HidePlayer") : config.getShow("HidePlayer"));
        psm.getPlayerSetting(puuid).setHidePlayer(nohide);
        for (Player onlinep : Bukkit.getServer().getOnlinePlayers()) {
            if (nohide) {
                name.hidePlayer(plugin, onlinep);
            } else {
                name.showPlayer(plugin, onlinep);
            }
        }
    }
}
