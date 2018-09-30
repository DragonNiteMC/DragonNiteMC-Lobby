package lobby.listener;

import addon.ericlam.JoinItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
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
    public void NoItemMovementOnNotCreative(InventoryClickEvent e){
        if (!e.getWhoClicked().getGameMode().equals(GameMode.CREATIVE)) e.setCancelled(true);
    }

    @EventHandler
    public void NoItemPick(InventoryPickupItemEvent e){
        ItemStack item = e.getItem().getItemStack();
        Player player = (Player) e.getInventory().getHolder();
        if (item.isSimilar(joinitem.getPlayItem()) || item.isSimilar(joinitem.getAchieveItem()) || item.isSimilar(joinitem.getSettingsItem())){
            if (!player.getGameMode().equals(GameMode.CREATIVE)) e.setCancelled(true);
        }
    }
    @EventHandler
    public void NoItemDrop(PlayerDropItemEvent e){
        ItemStack item = e.getItemDrop().getItemStack();
        Player player = e.getPlayer();
        if (item.isSimilar(joinitem.getPlayItem()) || item.isSimilar(joinitem.getAchieveItem()) || item.isSimilar(joinitem.getSettingsItem())){
            if (!player.getGameMode().equals(GameMode.CREATIVE)) e.setCancelled(true);
        }
    }

    @EventHandler
    public void InteractWithItem(PlayerInteractEvent event){
        ItemStack item = event.getItem();
        Player player = event.getPlayer();
        boolean isrightclick = event.getAction().equals(Action.RIGHT_CLICK_AIR);
        if (isrightclick) {
            if (item.isSimilar(joinitem.getPlayItem())) {
                player.performCommand("play");
            } else if (item.isSimilar(joinitem.getSettingsItem())) {
                player.performCommand("settings");
            } else if (item.isSimilar(joinitem.getAchieveItem())) {
                player.performCommand("achieve");
            }
        }
    }

}
