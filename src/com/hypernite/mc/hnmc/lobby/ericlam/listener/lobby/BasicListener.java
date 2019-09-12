package com.hypernite.mc.hnmc.lobby.ericlam.listener.lobby;

import com.hypernite.mc.hnmc.core.main.HyperNiteMC;
import com.hypernite.mc.hnmc.lobby.ericlam.addon.GUIBuilder;
import com.hypernite.mc.hnmc.lobby.ericlam.addon.TeleportLobby;
import com.hypernite.mc.hnmc.lobby.ericlam.addon.config.LobbyConfig;
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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;

import java.util.Optional;

public class BasicListener implements Listener {

    private LobbyConfig lobbyConfig;
    public BasicListener(){
        lobbyConfig = HNMCLobby.getConfigManager().getConfigAs(LobbyConfig.class);
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
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Optional.ofNullable(lobbyConfig.getJoinDonorMessage()).map(str -> str.replace("<player>", HyperNiteMC.getAPI().getVaultAPI().getChat().getPlayerPrefix(player) + player.getDisplayName())).orElse("null")));
        }
        spawn.TeleportToLobby(player);
        player.setGameMode(GameMode.ADVENTURE);
        HyperNiteMC.getAPI().getTabListManager().setHeader(lobbyConfig.getTablistHeader(), player);
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

    @EventHandler
    public void cancelMenuClickable(InventoryClickEvent e){
        Inventory inventory = e.getInventory();
        if (e.getSlotType() == InventoryType.SlotType.OUTSIDE) return;
        if (inventory == GUIBuilder.getInstance().getInventoryGUI((Player)e.getWhoClicked())) e.setCancelled(true);
    }













}
