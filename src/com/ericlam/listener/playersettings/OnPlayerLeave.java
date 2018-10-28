package com.ericlam.listener.playersettings;

import com.caxerx.mc.PlayerSettingManager;
import com.ericlam.addon.Variable;
import main.HyperNiteMC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.sql.SQLException;
import java.util.UUID;

public class OnPlayerLeave implements Listener {
    private BukkitScheduler scheduler = Bukkit.getScheduler();

    @EventHandler
    public void PlayerLeaveMySQL(PlayerQuitEvent event) throws SQLException {
        Player player = event.getPlayer();
        UUID puuid = player.getUniqueId();
        if (new Variable().isMySQL())
            Bukkit.getScheduler().runTaskAsynchronously(HyperNiteMC.plugin, () -> {
                PlayerSettingManager.getInstance().savePlayerSetting(puuid);
            });
    }
}


