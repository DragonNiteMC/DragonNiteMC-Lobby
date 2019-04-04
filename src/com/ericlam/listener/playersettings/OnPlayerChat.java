package com.ericlam.listener.playersettings;

import com.caxerx.mc.PlayerSettingManager;
import com.ericlam.addon.ConfigManager;
import main.HyperNiteMC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.sql.SQLException;
import java.util.UUID;


public class OnPlayerChat implements Listener {
    private ConfigManager var;
    public OnPlayerChat(){
        var = HyperNiteMC.getConfigManager();
    }
    @EventHandler
    public void onHideChat(AsyncPlayerChatEvent event) throws SQLException {
        Player player = event.getPlayer();
        UUID puuid = player.getUniqueId();
        PlayerSettingManager psm = PlayerSettingManager.getInstance();
        if (psm.getPlayerSetting(puuid).isHideChat()) {
            event.setCancelled(true);
            event.getRecipients().remove(player);
            player.sendMessage(var.getMessage("Commands.HideChat.hidden"));
        }
        event.getRecipients().removeIf(pl -> psm.getPlayerSetting(pl.getUniqueId()).isHideChat());
    }



}
