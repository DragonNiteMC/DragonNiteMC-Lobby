package com.ericlam.command;

import com.ericlam.addon.ConfigManager;
import com.ericlam.addon.GUIBuilder;
import main.HyperNiteMC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.sql.SQLException;

public class SettingsExe implements CommandExecutor {
    private ConfigManager var;
    private GUIBuilder gui = GUIBuilder.getInstance();
    private final HyperNiteMC plugin;
    public SettingsExe(HyperNiteMC plugin){
        this.plugin = plugin;
        var = HyperNiteMC.getConfigManager();
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player target;
        boolean terminal = commandSender instanceof ConsoleCommandSender;
        boolean perm = commandSender.hasPermission("settings.gui");
        boolean permother = commandSender.hasPermission("settings.gui.other");
        if (strings.length <= 0 && perm) {
            if (!terminal) {
                Player player = (Player) commandSender;
                try {
                    OpenGUI(player, player);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }else{commandSender.sendMessage(ChatColor.RED + "Console can only use /settings <player>");}
        } else if(perm && permother || terminal){
            target = (Bukkit.getServer().getPlayer(strings[0]));
            if (target == null){
                commandSender.sendMessage(var.getMessage("General.Player-Not-Found"));
            }else {
                try {
                    OpenGUI(target, commandSender);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }else{
            commandSender.sendMessage(var.noperm());
        }
        return true;
    }

    private void OpenGUI(Player name, CommandSender sender) throws SQLException {
        Player p = name.getPlayer();
        if (gui.getGUIFromPlayerMap(name) == null) {
            name.sendMessage("DEBUG: Your Inventory is null");
            return;
        }
        Inventory owngui = gui.getGUIFromPlayerMap(name);
        p.openInventory(owngui);
        if (sender != name) sender.sendMessage(var.getMessage("Commands.GUI.be-show").replace("<player>", name.getName()));
        if (var.config.getBoolean("GUI.Enable-Notify-On-OpenGUI")) name.sendMessage(var.getMessage("Commands.GUI.show"));
        gui.changeStatus(name);
    }


}
