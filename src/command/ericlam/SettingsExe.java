package command.ericlam;

import addon.ericlam.GUIBuilder;
import addon.ericlam.Variable;
import main.ericlam.HyperNiteMC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

import static addon.ericlam.Variable.config;
import static addon.ericlam.Variable.messagefile;

public class SettingsExe implements CommandExecutor {
    private Variable var = new Variable();
    private GUIBuilder gui = GUIBuilder.getInstance();
    private final HyperNiteMC plugin;
    public SettingsExe(HyperNiteMC plugin){
        this.plugin = plugin;
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
                commandSender.sendMessage(var.prefix() + var.getFs().returnColoredMessage(messagefile,"General.Player-Not-Found"));
            }else {
                try {
                    OpenGUI(target, commandSender);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }else{
            commandSender.sendMessage(var.prefix() + var.noperm());
        }
        return true;
    }

    public void OpenGUI(Player name, CommandSender sender) throws SQLException {
        Player p = name.getPlayer();
        p.openInventory(gui.getGUI());
        if (sender != name) sender.sendMessage(var.prefix() + var.getFs().returnColoredMessage(messagefile,"Commands.GUI.be-show").replace("<player>", name.getName()));
        if (config.getBoolean("GUI.Enable-Notify-On-OpenGUI")) name.sendMessage(var.prefix() + var.getFs().returnColoredMessage(messagefile,"Commands.GUI.show"));
        gui.changeStatus(p);
    }


}
