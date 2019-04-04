package com.ericlam.listener.playersettings;

import com.caxerx.mc.PlayerSettingManager;
import com.ericlam.addon.ConfigManager;
import com.ericlam.addon.GUIBuilder;
import main.HyperNiteMC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class OnPlayerJoin implements Listener {
    private ConfigManager var;
    private PlayerSettingManager psm;
    public OnPlayerJoin(){
        var = HyperNiteMC.getConfigManager();
        psm = PlayerSettingManager.getInstance();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID puuid = player.getUniqueId();
        int amplifier = var.config.getInt("Speed.Level") - 1;

        Bukkit.getScheduler().runTaskAsynchronously(HyperNiteMC.plugin, () -> {
            psm.getPlayerSetting(puuid);
            Bukkit.getScheduler().runTask(HyperNiteMC.plugin, () -> {
                boolean speed = psm.getPlayerSetting(puuid).isSpeed() && player.hasPermission("settings.speed");
                boolean fly = psm.getPlayerSetting(puuid).isFly() && player.hasPermission("settings.fly");
                player.setAllowFlight(fly);
                player.setFlying(fly);
                if (speed) player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999*20, amplifier));
                else player.removePotionEffect(PotionEffectType.SPEED);


                if (var.config.getBoolean("Join-Show-UUID.Enable")) {
                    player.sendMessage(var.getMessage("Functions.ShowUUID.JoinMessage").replace("<UUID>", puuid.toString()));
                }
                GUIBuilder gui = GUIBuilder.getInstance();
                if (!gui.OwnGUIHavePlayer(player)) gui.setOwnGUI(player, gui.getInventoryGUI());

                trackPlayerHide(player);

            });
        });
    }

    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent e) {
        trackPlayerHide(e.getPlayer());
    }

    private void trackPlayerHide(Player player) {

        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
            if (online == player) continue;

            if (psm.getPlayerSetting(online.getUniqueId()).isHidePlayer()) {
                online.hidePlayer(HyperNiteMC.plugin, player);
                HyperNiteMC.plugin.getLogger().info("DEBUG: Someone Enabled HidePlayer, Hided onJoin player.");
            } else {
                online.showPlayer(HyperNiteMC.plugin, player);
                HyperNiteMC.plugin.getLogger().info("DEBUG: Someone Disabled HidePlayer, Showed onJoin player.");
            }

            if (psm.getPlayerSetting(player.getUniqueId()).isHidePlayer()) {
                player.hidePlayer(HyperNiteMC.plugin, online);
                HyperNiteMC.plugin.getLogger().info("DEBUG: onJoinPlayer Enabled HidePlayer, Hided online players.");
            } else {
                player.showPlayer(HyperNiteMC.plugin, online);
                HyperNiteMC.plugin.getLogger().info("DEBUG: onJoinPlayer Disabled HidePlayer, Showed online players.");
            }
        }

    }
}
