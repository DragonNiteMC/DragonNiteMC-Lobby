package com.ericlam.listener.lobby;

import com.ericlam.addon.ConfigManager;
import com.ericlam.addon.TeleportLobby;
import com.ericlam.builders.TablistBuilder;
import main.HyperNiteMC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class BasicListener implements Listener {

    private ConfigManager var;
    public BasicListener(){
        var = HyperNiteMC.getConfigManager();
    }

    @EventHandler
    public void NoDamageAndVoidTp(EntityDamageEvent e){
        if (e.getEntity() instanceof Player){
            Player player = (Player) e.getEntity();
            if(e.getCause() == EntityDamageEvent.DamageCause.VOID) {
                spawn.TeleportToLobby(player);
            }
                e.setCancelled(true);
        }
    }

    private TeleportLobby spawn = TeleportLobby.getInstance();
    @EventHandler
    public void onLobbyJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        if (player.hasPermission("donor.join")){
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',var.lobbyfile.getString("join.donor-msg").replace("<player>", player.getDisplayName())));
        }

        spawn.TeleportToLobby(player);
        player.setGameMode(GameMode.ADVENTURE);
        TablistBuilder.getInstance().setHeader(var.header, player);
    }

    @EventHandler
    public void onLobbyRespawn(PlayerRespawnEvent e){
        Player player = e.getPlayer();
        Bukkit.getScheduler().scheduleSyncDelayedTask(HyperNiteMC.plugin, () ->{
            spawn.TeleportToLobby(player);
        });
    }



    @EventHandler
    public void antiBlockBreak(BlockBreakEvent e){
        Player player = e.getPlayer();
        if (player.hasPermission("hypernite.build") && player.getGameMode().equals(GameMode.CREATIVE)) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void antiBlockPlace(BlockPlaceEvent e){
        Player player = e.getPlayer();
        if (player.hasPermission("hypernite.build") && player.getGameMode().equals(GameMode.CREATIVE)) return;
        e.setCancelled(true);
    }













}
