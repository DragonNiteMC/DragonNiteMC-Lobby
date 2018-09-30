package playersettings.listener;

import addon.ericlam.GUIBuilder;
import com.caxerx.mc.PlayerSettingManager;
import command.ericlam.*;
import main.ericlam.HyperNiteMC;
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
    private SpeedExe speedExe;
    private FlyExe flyExe;
    private HideChatExe hideChatExe;
    private HidePlayerExe hidePlayerExe;
    private StackerExe stackerExe;

    public OnInventoryClick() {
        HyperNiteMC ps = (HyperNiteMC) HyperNiteMC.plugin;
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
        PlayerSettingManager psm = PlayerSettingManager.getInstance();
        GUIBuilder gui = GUIBuilder.getInstance();
        if (inventory == null) return;
        if (event.getSlotType() == InventoryType.SlotType.OUTSIDE) return;
        if (inventory.getName().equals(gui.getGUI().getName())) {
            switch (clicked.getType()) {
                case IRON_BOOTS:
                    speedExe.SetSpeed(player, player);
                    gui.changeStatus(player);
                    break;
                case ELYTRA:
                    flyExe.flyExecutor(player, player);
                    gui.changeStatus(player);
                    break;
                case PAPER:
                    hideChatExe.HideChat(player, player);
                    gui.changeStatus(player);
                    break;
                case PLAYER_HEAD:
                    hidePlayerExe.HidePlayer(player, player);
                    gui.changeStatus(player);
                    break;
                case STICKY_PISTON:
                    stackerExe.StackerOn(player, player);
                    gui.changeStatus(player);
                    break;
                default:
                    event.setCancelled(true);
                    break;
            }
        }
        event.setCancelled(true);
    }
}