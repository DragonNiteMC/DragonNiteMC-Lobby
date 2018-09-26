package Listener;

import CmdExecute.ericlam.HideChatExe;
import main.ericlam.PlayerSettings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.*;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitScheduler;
import java.util.*;
import java.util.List;

import static addon.ericlam.Variable.*;
import static main.ericlam.PlayerSettings.plugin;


public class OnPlayerChat implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (config.getBoolean("ChatFilter.Enable")) {
            Player player = event.getPlayer();
            String msg = event.getMessage();
            BukkitScheduler scheduler = Bukkit.getScheduler();
            List<String> keys = config.getStringList("ChatFilter.List");
            boolean chatbypass = player.hasPermission("ericlam.chat.bypass");
            for (String keyword : keys) {
                if (msg.contains(keyword) && !chatbypass) {
                    event.setCancelled(true);
                    player.sendMessage(prefix + returnColoredMessage("Functions.ChatResponse.block-message"));
                    break;
                }
            }
            if (msg.contains("fuck you") && !chatbypass) {
                event.setCancelled(true);
                player.sendMessage(prefix + ChatColor.RED + "Fuck you too!");
            }
            if ((msg.contains("i want to die")) && !chatbypass) {
                scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        player.setHealth(0);
                        player.sendMessage(prefix + ChatColor.RED + "Here you go!");
                        event.setCancelled(true);
                    }
                });

            }
        }
    }
    @EventHandler
    public void onHideChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID puuid = player.getUniqueId();
        if (HideChatExe.chatdisabled.contains(puuid)) {
            event.setCancelled(true);
            event.getRecipients().remove(player);
            player.sendMessage(prefix + returnColoredMessage("Commands.HideChat.hidden"));
        }
        for (Player pl : event.getRecipients()) {
            if (HideChatExe.chatdisabled.contains(pl.getUniqueId())) event.getRecipients().remove(pl);
        }
    }



}
