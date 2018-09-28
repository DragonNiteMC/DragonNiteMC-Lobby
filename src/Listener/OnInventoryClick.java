package Listener;


import CmdExecute.ericlam.*;
import addon.ericlam.Variable;
import main.ericlam.PlayerSettings;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

import static CmdExecute.ericlam.SettingsExe.*;

public class OnInventoryClick implements Listener {
    @EventHandler
    public void OnPlayerClickInventory(InventoryClickEvent event) throws IOException, SQLException {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        ItemStack clicked = event.getCurrentItem();
        if(inventory == null) return;
        if(event.getSlotType() == InventoryType.SlotType.OUTSIDE) return;
            SettingsExe gui = SettingsExe.getInstance();
            if (inventory.getName().equals(gui.getGUI().getName())) {
                switch (clicked.getType()) {
                    case IRON_BOOTS:
                        SpeedExe.SetSpeed(player, player);
                        changeStatus(player, gui.getGUI());
                        break;
                    case ELYTRA:
                        FlyExe.flyExecutor(player, player);
                        changeStatus(player, gui.getGUI());
                        break;
                    case PAPER:
                        HideChatExe.HideChat(player, player);
                        changeStatus(player, gui.getGUI());
                        break;
                    case PLAYER_HEAD:
                        HidePlayerExe.HidePlayer(player, player);
                        changeStatus(player, gui.getGUI());
                        break;
                    case STICKY_PISTON:
                        StackerExe.StackerOn(player, player);
                        changeStatus(player, gui.getGUI());
                        break;
                    default:
                        event.setCancelled(true);
                        break;
                }
            }
        event.setCancelled(true);
    }
}
