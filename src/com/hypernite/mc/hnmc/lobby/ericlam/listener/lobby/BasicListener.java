package com.hypernite.mc.hnmc.lobby.ericlam.listener.lobby;

import com.hypernite.mc.hnmc.core.main.HyperNiteMC;
import com.hypernite.mc.hnmc.lobby.ericlam.addon.LobbyConfig;
import com.hypernite.mc.hnmc.lobby.ericlam.addon.TeleportLobby;
import com.hypernite.mc.hnmc.lobby.main.HNMCLobby;
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

import java.util.Optional;

public class BasicListener implements Listener {

    private LobbyConfig var;
    public BasicListener(){
        var = HNMCLobby.getLobbyConfig();
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
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Optional.ofNullable(var.lobbyfile.getString("join.donor-msg")).map(str->str.replace("<player>", player.getDisplayName())).orElse("null")));
        }
        spawn.TeleportToLobby(player);
        player.setGameMode(GameMode.ADVENTURE);
        HyperNiteMC.getAPI().getTabListManager().setHeader(var.header, player);
    }

    @EventHandler
    public void onLobbyRespawn(PlayerRespawnEvent e){
        Player player = e.getPlayer();
        Bukkit.getScheduler().scheduleSyncDelayedTask(HNMCLobby.plugin, () -> spawn.TeleportToLobby(player));
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
