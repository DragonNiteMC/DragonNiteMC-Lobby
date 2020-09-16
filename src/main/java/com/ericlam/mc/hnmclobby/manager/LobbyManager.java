package com.ericlam.mc.hnmclobby.manager;

import com.ericlam.mc.hnmclobby.config.LobbyConfig;
import com.ericlam.mc.hnmclobby.config.MessagesConfig;
import com.ericlam.mc.hnmclobby.main.HNMCLobby;
import com.hypernite.mc.hnmc.core.main.HyperNiteMC;
import com.hypernite.mc.hnmc.core.managers.CoreConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LobbyManager {
    private final LobbyConfig config;

    public LobbyManager(LobbyConfig config) {
        this.config = config;
    }

    public boolean TeleportToLobby(Player player) {
        CoreConfig var = HyperNiteMC.getAPI().getCoreConfig();
        if (config.spawntp == null || config.spawntp.getY() == 0) {
            player.sendMessage(var.getPrefix() + ChatColor.RED + "伺服器尚未設置重生點，因此無法傳送");
            return false;
        }
        player.teleportAsync(config.spawntp);
        return true;
    }

    public void TeleportToLobby(Player player, CommandSender sender) {
        if (config.spawntp == null || config.spawntp.getY() == 0) {
            sender.sendMessage(HyperNiteMC.getAPI().getCoreConfig().getPrefix() + ChatColor.RED + "由於重生點尚未設置，因此無法傳送 " + ChatColor.YELLOW + player.getName() + ChatColor.RED + " 。");
            return;
        }
        MessagesConfig msg = HNMCLobby.getConfigManager().getConfigAs(MessagesConfig.class);
        player.teleportAsync(config.spawntp);
        sender.sendMessage(msg.getSpawn("be-send").replace("<player>", player.getName()));
        player.sendMessage(msg.getSpawn("send"));
    }
}
