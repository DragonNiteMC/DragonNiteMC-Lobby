package com.hypernite.mc.hnmc.lobby.ericlam.addon;

import com.hypernite.mc.hnmc.core.builders.ItemStackBuilder;
import com.hypernite.mc.hnmc.lobby.ericlam.addon.config.LobbyConfig;
import com.hypernite.mc.hnmc.lobby.main.HNMCLobby;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class JoinItemBuilder {

    private HashMap<ItemStack, Integer> joinItems = new HashMap<>();

    public JoinItemBuilder() {
        HNMCLobby.getConfigManager().getConfigAs("Lobby.yml", LobbyConfig.class).getJoinItemMap().forEach((k, joinItem) -> {
            Material material = Material.getMaterial(k);
            if (material == null) {
                HNMCLobby.plugin.getLogger().warning(k + " is not exist in material");
                return;
            }
            ItemStack stack = new ItemStackBuilder(material)
                    .displayName(joinItem.getName()).lore(joinItem.getLores())
                    .onInteract(e -> {
                        if(e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
                        e.getPlayer().chat("/"+joinItem.getCommand());
                    }).build();
            joinItems.put(stack, joinItem.getSlot());
        });
    }

    public void givePlayerJoinItem(Player player){
        player.getInventory().clear();
        joinItems.forEach((item, slot) -> player.getInventory().setItem(slot, item));
    }

    public HashMap<ItemStack, Integer> getJoinItems() {
        return joinItems;
    }
}
