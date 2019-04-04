package com.ericlam.addon;

import com.caxerx.builders.ItemStackBuilder;
import main.HyperNiteMC;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class JoinItemBuilder {
    private static JoinItemBuilder JoinItem;

    public static JoinItemBuilder getInstance() {
        if (JoinItem == null) JoinItem = new JoinItemBuilder();
        return JoinItem;
    }

    private HashMap<ItemStack, Integer> joinItems = new HashMap<>();

    private JoinItemBuilder(){
        for (com.ericlam.addon.JoinItem joinItem : HyperNiteMC.getConfigManager().getJoinItems()) {
            ItemStack stack = new ItemStackBuilder(joinItem.getMaterial()).displayName(joinItem.getName()).lore(joinItem.getLores()).onClick(e -> ((Player) e.getWhoClicked()).performCommand(joinItem.getCommand())).build();
            joinItems.put(stack, joinItem.getSlot());
        }
    }

    public void givePlayerJoinItem(Player player){
        player.getInventory().clear();
        joinItems.forEach((item, slot) -> player.getInventory().setItem(slot, item));
    }

    public HashMap<ItemStack, Integer> getJoinItems() {
        return joinItems;
    }
}
