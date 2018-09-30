package lobby.listener;

import addon.ericlam.JoinItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class LobbyJoinItem implements Listener {
    private JoinItemBuilder joinitem = JoinItemBuilder.getInstance();

    @EventHandler
    public void GiveItemOnJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        joinitem.givePlayerJoinItem(player);
    }

    @EventHandler
    public void NoJoinItemMovement(InventoryMoveItemEvent event){
        ItemStack item = event.getItem();
        if (item == joinitem.getPlayItem() || item == joinitem.getAchieveItem() || item == joinitem.getSettingsItem()){
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void NoItemDrag(InventoryDragEvent e){
            e.setCancelled(true);
    }
    @EventHandler
    public void NoItemPick(InventoryPickupItemEvent e){
        ItemStack item = e.getItem().getItemStack();
        if (item == joinitem.getPlayItem() || item == joinitem.getAchieveItem() || item == joinitem.getSettingsItem()){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void NoItemDrop(PlayerDropItemEvent e){
        ItemStack item = e.getItemDrop().getItemStack();
        if (item == joinitem.getPlayItem() || item == joinitem.getAchieveItem() || item == joinitem.getSettingsItem()){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void InteractWithItem(PlayerInteractEvent event){
        ItemStack item = event.getItem();
        Player player = event.getPlayer();
        boolean isrightclick = event.getAction().equals(Action.LEFT_CLICK_AIR);
        if (isrightclick) {
            if (item == joinitem.getPlayItem()) {
                player.performCommand("play");
                player.sendMessage("you used /play");
            } else if (item == joinitem.getSettingsItem()) {
                player.performCommand("pref");
                player.sendMessage("you used /pref");
            } else if (item == joinitem.getAchieveItem()) {
                player.performCommand("achieve");
                player.sendMessage("you use /achieve");
            }
        }
    }

}
