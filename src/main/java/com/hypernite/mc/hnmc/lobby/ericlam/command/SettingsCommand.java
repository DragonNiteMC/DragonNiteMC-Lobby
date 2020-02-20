package com.hypernite.mc.hnmc.lobby.ericlam.command;

import com.hypernite.mc.hnmc.lobby.ericlam.addon.GUIBuilder;
import com.hypernite.mc.hnmc.lobby.ericlam.addon.config.MessagesConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class SettingsCommand extends SettingsCommandNode {


    public SettingsCommand() {
        super("setting", "打開設定界面");
    }

    @Override
    public void executeSettings(Player name, CommandSender sender, MessagesConfig config) {
        GUIBuilder gui = GUIBuilder.getInstance();
        Inventory owngui = gui.getInventoryGUI(name);
        name.openInventory(owngui);
        if (sender != name)
            sender.sendMessage(config.getBeShow("GUI").replace("<player>", name.getName()));
        gui.changeStatus(name);
    }
}
