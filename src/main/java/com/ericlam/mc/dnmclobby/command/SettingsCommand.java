package com.ericlam.mc.dnmclobby.command;

import com.ericlam.mc.dnmclobby.config.MessagesConfig;
import com.ericlam.mc.dnmclobby.manager.GUIManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class SettingsCommand extends SettingsCommandNode {


    private final GUIManager gui;

    public SettingsCommand(GUIManager guiManager) {
        super("setting", "打開設定界面", null);
        this.gui = guiManager;
    }

    @Override
    public void executeSettings(Player name, CommandSender sender, MessagesConfig config) {
        Inventory owngui = gui.getInventoryGUI(name);
        name.openInventory(owngui);
        if (sender != name)
            sender.sendMessage(config.getBeShow("GUI").replace("<player>", name.getName()));
        gui.changeStatus(name);
    }
}
