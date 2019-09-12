package com.hypernite.mc.hnmc.lobby.ericlam.command;

import com.hypernite.mc.hnmc.lobby.ericlam.addon.GUIBuilder;
import com.hypernite.mc.hnmc.lobby.ericlam.addon.config.MainConfig;
import com.hypernite.mc.hnmc.lobby.ericlam.addon.config.MessagesConfig;
import com.hypernite.mc.hnmc.lobby.main.HNMCLobby;
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
            sender.sendMessage(config.getCommandMSG().getGui().get("be-show").replace("<player>", name.getName()));
        if (HNMCLobby.getConfigManager().getConfigAs(MainConfig.class).isNotifyGUIOpen())
            name.sendMessage(config.getCommandMSG().getGui().get("show"));
        gui.changeStatus(name);
    }
}
