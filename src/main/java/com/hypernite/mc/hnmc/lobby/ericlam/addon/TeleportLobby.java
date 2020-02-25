package com.hypernite.mc.hnmc.lobby.ericlam.addon;

import com.hypernite.mc.hnmc.core.main.HyperNiteMC;
import com.hypernite.mc.hnmc.core.managers.CoreConfig;
import com.hypernite.mc.hnmc.lobby.ericlam.addon.config.LobbyConfig;
import com.hypernite.mc.hnmc.lobby.ericlam.addon.config.MessagesConfig;
import com.hypernite.mc.hnmc.lobby.main.HNMCLobby;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportLobby {
    private static TeleportLobby tplobby;
    private LobbyConfig lobbyfile;


    private TeleportLobby() {
        lobbyfile = HNMCLobby.getConfigManager().getConfigAs(LobbyConfig.class);
    }

    public static TeleportLobby getInstance() {
        if (tplobby == null) tplobby = new TeleportLobby();
        return tplobby;
    }

    public boolean TeleportToLobby(Player player) {
        CoreConfig var = HyperNiteMC.getAPI().getCoreConfig();
        if (lobbyfile.spawntp == null || lobbyfile.spawntp.getY() == 0) {
            player.sendMessage(var.getPrefix() + ChatColor.RED + "伺服器尚未設置重生點，因此無法傳送");
            return false;
        }
        player.teleportAsync(lobbyfile.spawntp);
        return true;
    }

    public void TeleportToLobby(Player player, CommandSender sender) {
        if (lobbyfile.spawntp == null || lobbyfile.spawntp.getY() == 0) {
            sender.sendMessage(HyperNiteMC.getAPI().getCoreConfig().getPrefix() + ChatColor.RED + "由於重生點尚未設置，因此無法傳送 " + ChatColor.YELLOW + player.getName() + ChatColor.RED + " 。");
            return;
        }
        MessagesConfig msg = HNMCLobby.getConfigManager().getConfigAs(MessagesConfig.class);
        player.teleportAsync(lobbyfile.spawntp);
        sender.sendMessage(msg.getSpawn("be-send").replace("<player>", player.getName()));
        player.sendMessage(msg.getSpawn("send"));
    }
}
