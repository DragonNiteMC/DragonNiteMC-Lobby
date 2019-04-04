package com.ericlam.listener.playersettings;

import com.ericlam.addon.GUIBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.sql.SQLException;

public class OnInventoryClick implements Listener {

    @EventHandler
    public void OnPlayerClickInventory(InventoryClickEvent event) throws IOException, SQLException {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        ItemStack clicked = event.getCurrentItem();
        GUIBuilder gui = GUIBuilder.getInstance();
        if (inventory == null) return;
        if (event.getSlotType() == InventoryType.SlotType.OUTSIDE) return;
        if (inventory.equals(gui.getGUIFromPlayerMap(player))) {
            switch (clicked.getType()) {
                case IRON_BOOTS:
                    player.performCommand("speed");
                    gui.changeStatus(player);
                    break;
                case ELYTRA:
                    player.performCommand("fly");
                    gui.changeStatus(player);
                    break;
                case PAPER:
                    player.performCommand("hidechat");
                    gui.changeStatus(player);
                    break;
                case PLAYER_HEAD:
                    player.performCommand("hideplayer");
                    gui.changeStatus(player);
                    break;
                case STICKY_PISTON:
                    player.performCommand("stacker");
                    gui.changeStatus(player);
                    break;
            }
            event.setCancelled(true);
        }
    }
}
