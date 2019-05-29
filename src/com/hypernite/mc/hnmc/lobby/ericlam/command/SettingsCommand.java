package com.hypernite.mc.hnmc.lobby.ericlam.command;

import com.hypernite.mc.hnmc.core.managers.ConfigManager;
import com.hypernite.mc.hnmc.lobby.ericlam.addon.GUIBuilder;
import com.hypernite.mc.hnmc.lobby.main.HNMCLobby;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.IOException;

public class SettingsCommand extends SettingsCommandNode {


    public SettingsCommand() {
        super("setting", "打開設定界面");
    }

    @Override
    public void executeSettings(Player name, CommandSender sender, ConfigManager var, boolean isMySQL) throws IOException {
        GUIBuilder gui = GUIBuilder.getInstance();
        Inventory owngui = gui.getInventoryGUI(name);
        name.openInventory(owngui);
        if (sender != name) sender.sendMessage(var.getMessage("Commands.GUI.be-show").replace("<player>", name.getName()));
        if (HNMCLobby.getLobbyConfig().config.getBoolean("GUI.Enable-Notify-On-OpenGUI")) name.sendMessage(var.getMessage("Commands.GUI.show"));
        gui.changeStatus(name);
    }
}
