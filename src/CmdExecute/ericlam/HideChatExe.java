package CmdExecute.ericlam;

import MySQL.HyperNite.SQLDataSourceManager;
import addon.ericlam.MySQL;
import addon.ericlam.Variable;
import main.ericlam.PlayerSettings;
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

    public HideChatExe(PlayerSettings plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player target;
        boolean terminal = commandSender instanceof ConsoleCommandSender;
        boolean perm = commandSender.hasPermission("settings.hidechat");
        boolean permother = commandSender.hasPermission("settings.hidechat.other");
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
            if (target == null) commandSender.sendMessage(prefix + returnColoredMessage("General.Player-Not-Found"));
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
        boolean hide = !chatdisabled.contains(puuid);
        if (sender != name)  sender.sendMessage(prefix + returnColoredMessage("Commands.HideChat.be-" + (hide ? "hide" : "show")).replace("<player>", name.getDisplayName()));
        name.sendMessage(prefix + returnColoredMessage("Commands.HideChat." + (hide ? "hide" : "show")));
        if (hide) chatdisabled.add(puuid);
        else chatdisabled.remove(puuid);
        if (Variable.yaml) {
            Variable.setYml("HideChat",puuid,hide);
        }
        if (Variable.MYsql){
            SQLDataSourceManager mysql = SQLDataSourceManager.getInstance();
            PreparedStatement ps = mysql.getFuckingConnection().prepareStatement("UPDATE PS_stats SET HideChat=? WHERE PlayerUUID = ?");
            ps.setInt(1,(hide ? 1 : 0));
            ps.setString(2, puuid.toString());
            ps.execute();
        }
    }
}

