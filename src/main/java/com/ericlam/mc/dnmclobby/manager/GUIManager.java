package com.ericlam.mc.dnmclobby.manager;

import com.ericlam.mc.dnmclobby.SettingItem;
import com.ericlam.mc.dnmclobby.config.MessagesConfig;
import com.ericlam.mc.dnmclobby.main.DNMCLobby;
import com.dragonnite.mc.dnmc.core.builders.InventoryBuilder;
import com.dragonnite.mc.dnmc.core.builders.ItemStackBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class GUIManager {
    private final PlayerSettingManager psm;
    private final Map<Player, Inventory> ownGUI = new HashMap<>();
    private final Set<SettingItem> items = new HashSet<>();

    public GUIManager(PlayerSettingManager psm) {
        this.psm = psm;
        items.add(new SettingItem(10, Material.IRON_BOOTS, "speed", "Speed"));
        items.add(new SettingItem(13, Material.ELYTRA, "fly", "Fly"));
        items.add(new SettingItem(16, Material.PAPER, "hidechat", "HideChat"));
        items.add(new SettingItem(29, Material.PLAYER_HEAD, "hideplayer", "HidePlayer"));
        items.add(new SettingItem(33, Material.STICKY_PISTON, "stacker", "Stacker"));
    }

    public Inventory getInventoryGUI(Player player) {
        if (ownGUI.containsKey(player)) return ownGUI.get(player);
        MessagesConfig msg = DNMCLobby.getConfigManager().getConfigAs(MessagesConfig.class);
        InventoryBuilder inv = new InventoryBuilder(54, msg.getGUI("title"));
        for (SettingItem settingItem : items) {
            inv.item(settingItem.getSlot(),
                    new ItemStackBuilder(settingItem.getMaterial())
                            .displayName(msg.getGUI(settingItem.getName()))
                            .lore(msg.getPureList("GUI.Lore").stream().map(e -> ChatColor.translateAlternateColorCodes('&', e)).collect(Collectors.toList()))
                            .onClick(e -> {
                                        Player p = (Player) e.getWhoClicked();
                                        p.performCommand(settingItem.getCommand());
                                        this.changeStatus(p);
                                    }
                            ).build());
        }
        Inventory inventory = inv.build();
        ownGUI.putIfAbsent(player, inventory);
        return inventory;
    }

    public void changeStatus(Player player) {
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
        for (int i = 0; i < Status.length; i++) {
            int slot = StatusSlot[i];
            boolean stats = Status[i];
            if (stats) inventory.setItem(slot, new ItemStack(Material.LIME_STAINED_GLASS_PANE));
            else inventory.setItem(slot, new ItemStack(Material.RED_STAINED_GLASS_PANE));
        }
    }


}
