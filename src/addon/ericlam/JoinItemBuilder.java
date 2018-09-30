package addon.ericlam;

import com.caxerx.builders.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class JoinItemBuilder {
    private ItemStack PlayItem;
    private ItemStack AchieveItem;
    private ItemStack SettingsItem;
    private static JoinItemBuilder JoinItem;

    public static JoinItemBuilder getInstance() {
        if (JoinItem == null) JoinItem = new JoinItemBuilder();
        return JoinItem;
    }

    private JoinItemBuilder(){
        PlayItem = new ItemStackBuilder(Material.PAPER).displayName("§a§l/Play §7- 選擇伺服器").lore("§c歡迎來到 §bHyperniteMC§7!").lore("§2按我進入伺服器菜單!").build();
        AchieveItem = new ItemStackBuilder(Material.BOOK).displayName("§e§l/Achieve §7- 打開成就介面").lore("§c歡迎來到 §bHyperniteMC§7!").lore("§2按我進入成就介面!").build();
        SettingsItem = new ItemStackBuilder(Material.TRIPWIRE_HOOK).displayName("§c§l/Prefs §7- 玩家設定").lore("§c歡迎來到 §bHyperniteMC§7!").lore("§2按我進入玩家設定菜單!").build();
    }

    public void givePlayerJoinItem(Player player){
        player.getInventory().clear();
        player.getInventory().setItem(0, PlayItem);
        player.getInventory().setItem(4, AchieveItem);
        player.getInventory().setItem(7, SettingsItem);
    }

    public ItemStack getPlayItem() {
        return PlayItem;
    }

    public ItemStack getAchieveItem() {
        return AchieveItem;
    }

    public ItemStack getSettingsItem() {
        return SettingsItem;
    }
}
