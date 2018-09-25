package main.ericlam;

import addon.ericlam.MySQL;
import addon.ericlam.Variable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.PrepareAnvilEvent;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static addon.ericlam.Variable.*;

public class StackerExe implements CommandExecutor{
    public static Set<UUID> stackerenabled = new HashSet<>();
    private final PlayerSettings plugin;

    StackerExe(PlayerSettings plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (config.getBoolean("Stacker.Enable")) {
            Player target;
            boolean terminal = commandSender instanceof ConsoleCommandSender;
            boolean perm = commandSender.hasPermission("ericlam.stacker");
            boolean permother = commandSender.hasPermission("ericlam.stacker.other");
            if (strings.length <= 0 && perm) {
                if (!terminal) {
                    Player player = (Player) commandSender;
                    try {
                        StackerOn(player, player);
                    } catch (IOException | SQLException e) {
                        e.printStackTrace();
                    }
                } else commandSender.sendMessage(ChatColor.RED + "Console can only use /stacker <player>");
            } else if (permother || terminal) {
                target = Bukkit.getPlayer(strings[0]);
                if (target == null)
                    commandSender.sendMessage(prefix + PlayerSettings.returnColoredMessage("General.Player-Not-Found"));
                else {
                    try {
                        StackerOn(target, commandSender);
                    } catch (IOException | SQLException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                commandSender.sendMessage(prefix + noperm);
            }
            return true;
        }
        return false;
    }
    public static void StackerOn(Player name, CommandSender sender) throws IOException, SQLException {
        Player player = name.getPlayer();
        UUID puuid = player.getUniqueId();
        if (sender != name)  sender.sendMessage(prefix + PlayerSettings.returnColoredMessage("Commands.Stacker.be-" + (!stackerenabled.contains(puuid) ? "enable" : "disable")).replace("<player>", name.getDisplayName()));
        name.sendMessage(prefix + PlayerSettings.returnColoredMessage("Commands.Stacker." + (!stackerenabled.contains(puuid) ? "enable" : "disable")));
        if (!stackerenabled.contains(puuid)) {
            stackerenabled.add(puuid);
            if (Variable.yaml) {
                Variable.setYml("Stacker",puuid,true);
            }
            if (Variable.MYsql){
                MySQL mysql = MySQL.getinstance();
                PreparedStatement ps = mysql.connection.prepareStatement("UPDATE "+table+" SET Stacker=1 WHERE PlayerUUID = ?");
                ps.setString(1, puuid.toString());
                ps.execute();
            }
        }else{
            stackerenabled.remove(puuid);
            if (Variable.yaml) {
                Variable.setYml("Stacker",puuid,false);
            }
            if (Variable.MYsql){
                MySQL mysql = MySQL.getinstance();
                PreparedStatement ps = mysql.connection.prepareStatement("UPDATE "+table+" SET Stacker=0 WHERE PlayerUUID = ?");
                ps.setString(1, puuid.toString());
                ps.execute();
            }
        }
    }
}

