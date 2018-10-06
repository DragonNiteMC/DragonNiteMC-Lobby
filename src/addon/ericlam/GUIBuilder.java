package addon.ericlam;

import com.caxerx.builders.InventoryBuilder;
import com.caxerx.builders.ItemStackBuilder;
import com.caxerx.mc.PlayerSettingManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import static addon.ericlam.Variable.messagefile;

public class GUIBuilder {
    private static GUIBuilder setting;
    private PlayerSettingManager psm = PlayerSettingManager.getInstance();
    private HashMap<Player, Inventory> ownGUI = new HashMap<>();

    public static GUIBuilder getInstance() {
        if (setting == null) setting = new GUIBuilder();
        return setting;
    }

    public Inventory getInventoryGUI(){
        Variable var = new Variable();
        InventoryBuilder inv = new InventoryBuilder(54, var.title());
        Material[] representItem = {Material.IRON_BOOTS, Material.ELYTRA, Material.PAPER, Material.PLAYER_HEAD, Material.STICKY_PISTON};
        String[] ItemName = {"Speed", "Fly", "HideChat", "HidePlayer", "Stacker"};
        Integer[] ItemSlot = {10, 13, 16, 29, 33};
        for (int i = 0; i < representItem.length;i++){
            Material IS = representItem[i];
            String IN = ItemName[i];
            int slot = ItemSlot[i];
            inv.item(slot, new ItemStackBuilder(IS)
                    .displayName(messagefile.getString("Commands.GUI."+IN))
                    .lore(messagefile.getStringList("Commands.GUI.Lore"))
                    .build());
        }
        return inv.build();
    }

    public void setOwnGUI(Player player, Inventory inventory){
        ownGUI.put(player, inventory);
    }

    public void removeOwnGUI(Player player){
        ownGUI.remove(player);
    }

    public Inventory getGUIFromPlayerMap(Player player){
        return ownGUI.get(player);
    }

    public boolean OwnGUIHavePlayer(Player player){
        return ownGUI.containsKey(player);
    }

    public void changeStatus(Player player) throws SQLException {
        UUID puuid = player.getUniqueId();
        boolean Fly = psm.getPlayerSetting(puuid).isFly();
        boolean Speed = psm.getPlayerSetting(puuid).isSpeed();
        boolean HidePlayer = psm.getPlayerSetting(puuid).isHidePlayer();
        boolean HideChat = psm.getPlayerSetting(puuid).isHideChat();
        boolean Stacker = psm.getPlayerSetting(puuid).isStacker();
        Inventory inventory = ownGUI.get(player);
        if (inventory == null) return;
        Integer[] StatusSlot = {19, 22, 25, 38, 42};
        Boolean[] Status = {Speed, Fly, HideChat, HidePlayer, Stacker};
        for (int i = 0; i < Status.length;i++) {
            int slot = StatusSlot[i];
            boolean stats = Status[i];
            if (stats) inventory.setItem(slot, new ItemStack(Material.LIME_STAINED_GLASS_PANE));
            else inventory.setItem(slot, new ItemStack(Material.RED_STAINED_GLASS_PANE));
        }
    }


}
