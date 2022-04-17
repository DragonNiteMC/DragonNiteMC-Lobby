package com.ericlam.mc.dnmclobby.listener;

import com.ericlam.mc.dnmclobby.manager.JoinItemManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class LobbyJoinListener implements Listener {

    private final JoinItemManager joinItemManager = new JoinItemManager();
    private final Plugin plugin;

    public LobbyJoinListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void GiveItemOnJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        joinItemManager.givePlayerJoinItem(player);
    }

    @EventHandler
    public void GiveItemOnRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            joinItemManager.givePlayerJoinItem(player);
        });
    }

    @EventHandler
    public void NoItemMovementOnNotCreative(InventoryClickEvent e) {
        if (!e.getWhoClicked().getGameMode().equals(GameMode.CREATIVE)) e.setCancelled(true);
    }

    @EventHandler
    public void NoItemPick(InventoryPickupItemEvent e) {
        ItemStack item = e.getItem().getItemStack();
        Player player = (Player) e.getInventory().getHolder();
        if (player == null) return;
        if (joinItemManager.getJoinItems().containsKey(item)) {
            if (!player.getGameMode().equals(GameMode.CREATIVE)) e.setCancelled(true);
        }
    }

    @EventHandler
    public void NoItemDrop(PlayerDropItemEvent e) {
        ItemStack item = e.getItemDrop().getItemStack();
        Player player = e.getPlayer();
        if (joinItemManager.getJoinItems().containsKey(item)) {
            if (!player.getGameMode().equals(GameMode.CREATIVE)) e.setCancelled(true);
        }
    }
}
