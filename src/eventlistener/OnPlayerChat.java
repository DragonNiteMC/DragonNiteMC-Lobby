package listener;

import addon.ericlam.Variable;
import command.ericlam.HideChatExe;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.List;
import java.util.UUID;

import static addon.ericlam.Variable.config;
import static addon.ericlam.Variable.messagefile;
import static main.ericlam.PlayerSettings.plugin;


public class OnPlayerChat implements Listener {
    private Variable var = Variable.getInstance();
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (config.getBoolean("ChatFilter.Enable")) {
            Player player = event.getPlayer();
            String msg = event.getMessage();
            BukkitScheduler scheduler = Bukkit.getScheduler();
            List<String> keys = config.getStringList("ChatFilter.List");
            boolean chatbypass = player.hasPermission("settings.chat.bypass");
            for (String keyword : keys) {
                if (msg.contains(keyword) && !chatbypass) {
                    event.setCancelled(true);
                    player.sendMessage(var.prefix() + var.getFs().returnColoredMessage(messagefile,"Functions.ChatResponse.block-message"));
                    break;
                }
            }
            if (msg.contains("fuck you") && !chatbypass) {
                event.setCancelled(true);
                player.sendMessage(var.prefix() + ChatColor.RED + "Fuck you too!");
            }
            if ((msg.contains("i want to die")) && !chatbypass) {
                scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        player.setHealth(0);
                        player.sendMessage(var.prefix() + ChatColor.RED + "Here you go!");
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
            player.sendMessage(var.prefix() + var.getFs().returnColoredMessage(messagefile,"Commands.HideChat.hidden"));
        }
        for (Player pl : event.getRecipients()) {
            if (HideChatExe.chatdisabled.contains(pl.getUniqueId())) event.getRecipients().remove(pl);
        }
    }



}
