package Listener;


import CmdExecute.ericlam.*;
import main.ericlam.PlayerSettings;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.sql.SQLException;

import static CmdExecute.ericlam.SettingsExe.*;

public class OnInventoryClick implements Listener {
    public void OnPlayerClickInventory(InventoryClickEvent event) throws IOException, SQLException {
        Player player = (Player) event.getWhoClicked();
        if(player.getInventory().getName().equals(PlayerSettingGUI.getName())){
            ItemStack clicked = event.getCurrentItem();
            switch (clicked.getType()){
                case IRON_BOOTS:
                    SpeedExe.SetSpeed(player,player);
                    changeStatus(player);
                    break;
                case ELYTRA:
                    FlyExe.flyExecutor(player,player);
                    changeStatus(player);
                    break;
                case PAPER:
                    HideChatExe.HideChat(player,player);
                    changeStatus(player);
                    break;
                case PLAYER_HEAD:
                    HidePlayerExe.HidePlayer(player,player);
                    changeStatus(player);
                    break;
                case STICKY_PISTON:
                    StackerExe.StackerOn(player,player);
                    changeStatus(player);
                    break;
            }
            event.setCancelled(true);
        }
    }
}
