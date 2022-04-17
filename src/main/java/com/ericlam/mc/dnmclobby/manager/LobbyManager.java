package com.ericlam.mc.dnmclobby.manager;

import com.ericlam.mc.dnmclobby.config.LobbyConfig;
import com.ericlam.mc.dnmclobby.config.MessagesConfig;
import com.ericlam.mc.dnmclobby.main.DNMCLobby;
import com.dragonnite.mc.dnmc.core.main.DragonNiteMC;
import com.dragonnite.mc.dnmc.core.managers.CoreConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LobbyManager {
    private final LobbyConfig config;

    public LobbyManager(LobbyConfig config) {
        this.config = config;
    }

    public boolean TeleportToLobby(Player player) {
        CoreConfig var = DragonNiteMC.getAPI().getCoreConfig();
        if (config.spawntp == null || config.spawntp.getY() == 0) {
            player.sendMessage(var.getPrefix() + ChatColor.RED + "伺服器尚未設置重生點，因此無法傳送");
            return false;
        }
        player.teleportAsync(config.spawntp);
        return true;
    }

    public void TeleportToLobby(Player player, CommandSender sender) {
        if (config.spawntp == null || config.spawntp.getY() == 0) {
            sender.sendMessage(DragonNiteMC.getAPI().getCoreConfig().getPrefix() + ChatColor.RED + "由於重生點尚未設置，因此無法傳送 " + ChatColor.YELLOW + player.getName() + ChatColor.RED + " 。");
            return;
        }
        MessagesConfig msg = DNMCLobby.getConfigManager().getConfigAs(MessagesConfig.class);
        player.teleportAsync(config.spawntp);
        sender.sendMessage(msg.getSpawn("be-send").replace("<player>", player.getName()));
        player.sendMessage(msg.getSpawn("send"));
    }
}
