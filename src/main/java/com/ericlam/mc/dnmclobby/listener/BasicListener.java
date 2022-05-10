package com.ericlam.mc.dnmclobby.listener;

import com.ericlam.mc.dnmclobby.config.MessagesConfig;
import com.ericlam.mc.dnmclobby.manager.GUIManager;
import com.ericlam.mc.dnmclobby.manager.LobbyManager;
import com.dragonite.mc.dnmc.core.main.DragoniteMC;
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
import org.bukkit.plugin.Plugin;

import java.util.Optional;

public class BasicListener implements Listener {

    private final MessagesConfig msg;
    private final LobbyManager spawn;
    private final GUIManager guiManager;
    private final Plugin plugin;

    public BasicListener(MessagesConfig config, LobbyManager lobby, GUIManager guiManager, Plugin plugin) {
        this.msg = config;
        this.spawn = lobby;
        this.guiManager = guiManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void NoDamageAndVoidTp(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            e.setCancelled(true);
            if (e.getCause() == EntityDamageEvent.DamageCause.VOID) {
                Bukkit.getScheduler().runTask(plugin, () -> {
                    spawn.TeleportToLobby(player);
                    player.setFallDistance(0f);
                });
            }
        }
    }

    @EventHandler
    public void onLobbyJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (player.hasPermission("donor.join")) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Optional.ofNullable(msg.getPure("joinDonorMsg")).map(str -> str.replace("<player>", DragoniteMC.getAPI().getChatFormatManager().getPlayerPrefix(player) + player.getDisplayName())).orElse("null")));
        }
        spawn.TeleportToLobby(player);
        player.setGameMode(GameMode.ADVENTURE);
        DragoniteMC.getAPI().getTabListManager().setHeaderFooter(msg.getPure("tablist.header"), msg.getPure("tablist.footer"), player);
    }

    @EventHandler
    public void onLobbyRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> spawn.TeleportToLobby(player));
    }


    @EventHandler
    public void antiBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (player.hasPermission("dragonnite.build") && player.getGameMode().equals(GameMode.CREATIVE)) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void antiBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if (player.hasPermission("dragonnite.build") && player.getGameMode().equals(GameMode.CREATIVE)) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void cancelMenuClickable(InventoryClickEvent e) {
        Inventory inventory = e.getInventory();
        if (e.getSlotType() == InventoryType.SlotType.OUTSIDE) return;
        if (inventory == guiManager.getInventoryGUI((Player) e.getWhoClicked())) e.setCancelled(true);
    }


}
