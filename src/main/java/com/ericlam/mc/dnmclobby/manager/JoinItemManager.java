package com.ericlam.mc.dnmclobby.manager;

import com.ericlam.mc.dnmclobby.config.LobbyConfig;
import com.ericlam.mc.dnmclobby.main.DNMCLobby;
import com.dragonnite.mc.dnmc.core.builders.ItemStackBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class JoinItemManager {

    private final HashMap<ItemStack, Integer> joinItems = new HashMap<>();

    public JoinItemManager() {
        DNMCLobby.getConfigManager().getConfigAs(LobbyConfig.class).joinItems.forEach((material, joinItem) -> {
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
