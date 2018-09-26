package main.ericlam;

import addon.ericlam.MySQL;
import addon.ericlam.Variable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import static addon.ericlam.Variable.*;

public class HideChatExe implements CommandExecutor{
    public static Set<UUID> chatdisabled = new HashSet<>();
    private final PlayerSettings plugin;

    HideChatExe(PlayerSettings plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player target;
        boolean terminal = commandSender instanceof ConsoleCommandSender;
        boolean perm = commandSender.hasPermission("ericlam.hidechat");
        boolean permother = commandSender.hasPermission("ericlam.hidechat.other");
        if (strings.length <= 0 && perm) {
            if (!terminal) {
                Player player = (Player) commandSender;
                try {
                    HideChat(player, player);
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            }
            else commandSender.sendMessage(ChatColor.RED + "Console can only use /hidechat <player>");
        } else if (permother || terminal) {
            target = Bukkit.getPlayer(strings[0]);
            if (target == null) commandSender.sendMessage(prefix + PlayerSettings.returnColoredMessage("General.Player-Not-Found"));
            else {
                try {
                    HideChat(target,commandSender);
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            }
        }else{commandSender.sendMessage(prefix + noperm);}
        return true;
    }
    public static void HideChat(Player name, CommandSender sender) throws IOException, SQLException {
        Player player = name.getPlayer();
        UUID puuid = player.getUniqueId();
        if (sender != name)  sender.sendMessage(prefix + PlayerSettings.returnColoredMessage("Commands.HideChat.be-" + (!chatdisabled.contains(puuid) ? "hide" : "show")).replace("<player>", name.getDisplayName()));
        name.sendMessage(prefix + PlayerSettings.returnColoredMessage("Commands.HideChat." + (!chatdisabled.contains(puuid) ? "hide" : "show")));
        if (!chatdisabled.contains(puuid)) {
            chatdisabled.add(puuid);
            if (Variable.yaml) {
                Variable.setYml("HideChat",puuid,true);
            }
            if (Variable.MYsql){
                MySQL mysql = MySQL.getinstance();
                PreparedStatement ps = mysql.connection.prepareStatement("UPDATE "+table+" SET HideChat=1 WHERE PlayerUUID = ?");
                ps.setString(1, puuid.toString());
                ps.execute();
            }
        }else{
            chatdisabled.remove(puuid);
            if (Variable.yaml) {
                Variable.setYml("HideChat",puuid,false);
            }
            if (Variable.MYsql){
                MySQL mysql = MySQL.getinstance();
                PreparedStatement ps = mysql.connection.prepareStatement("UPDATE "+table+" SET HideChat=0 WHERE PlayerUUID = ?");
                ps.setString(1, puuid.toString());
                ps.execute();
            }
        }
    }
}

