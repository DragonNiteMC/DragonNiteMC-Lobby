package com.ericlam.mc.hnmclobby.manager;

import com.ericlam.mc.hnmclobby.config.LobbyConfig;
import com.ericlam.mc.hnmclobby.main.HNMCLobby;
import com.hypernite.mc.hnmc.core.builders.ItemStackBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class JoinItemManager {

    private final HashMap<ItemStack, Integer> joinItems = new HashMap<>();

    public JoinItemManager() {
        HNMCLobby.getConfigManager().getConfigAs(LobbyConfig.class).joinItems.forEach((material, joinItem) -> {
            ItemStack stack = new ItemStackBuilder(material)
                    .displayName(joinItem.name).lore(joinItem.lores)
                    .onInteract(e -> {
                        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)
                            return;
                        e.getPlayer().chat("/" + joinItem.command);
                    }).build();
            joinItems.put(stack, joinItem.slot);
        });
    }

    public void givePlayerJoinItem(Player player) {
        player.getInventory().clear();
        joinItems.forEach((item, slot) -> player.getInventory().setItem(slot, item));
    }

    public HashMap<ItemStack, Integer> getJoinItems() {
        return joinItems;
    }
}
