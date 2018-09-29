package eventlistener;

import command.ericlam.*;
import main.ericlam.PlayerSettings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.sql.SQLException;

import static command.ericlam.SettingsExe.changeStatus;

public class OnInventoryClick implements Listener {
    private SpeedExe speedExe;
    private FlyExe flyExe;
    private HideChatExe hideChatExe;
    private HidePlayerExe hidePlayerExe;
    private StackerExe stackerExe;

    public OnInventoryClick() {
        PlayerSettings ps = (PlayerSettings) PlayerSettings.plugin;
        speedExe = new SpeedExe(ps);
        flyExe = new FlyExe(ps);
        hideChatExe = new HideChatExe(ps);
        hidePlayerExe = new HidePlayerExe(ps);
        stackerExe = new StackerExe(ps);
    }

    @EventHandler
    public void OnPlayerClickInventory(InventoryClickEvent event) throws IOException, SQLException {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        ItemStack clicked = event.getCurrentItem();
        if (inventory == null) return;
        if (event.getSlotType() == InventoryType.SlotType.OUTSIDE) return;
        SettingsExe gui = SettingsExe.getInstance();
        if (inventory.getName().equals(gui.getGUI().getName())) {
            switch (clicked.getType()) {
                case IRON_BOOTS:
                    speedExe.SetSpeed(player, player);
                    changeStatus(player, gui.getGUI());
                    break;
                case ELYTRA:
                    flyExe.flyExecutor(player, player);
                    changeStatus(player, gui.getGUI());
                    break;
                case PAPER:
                    hideChatExe.HideChat(player, player);
                    changeStatus(player, gui.getGUI());
                    break;
                case PLAYER_HEAD:
                    hidePlayerExe.HidePlayer(player, player);
                    changeStatus(player, gui.getGUI());
                    break;
                case STICKY_PISTON:
                    stackerExe.StackerOn(player, player);
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