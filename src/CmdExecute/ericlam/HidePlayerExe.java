package CmdExecute.ericlam;

import addon.ericlam.MySQL;
import addon.ericlam.Variable;
import main.ericlam.PlayerSettings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import static addon.ericlam.Variable.*;

public class HidePlayerExe implements CommandExecutor {
    private final PlayerSettings plugin;
    public HidePlayerExe(PlayerSettings plugin){ this.plugin = plugin;}

    public static HashSet<Player> vanished = new HashSet<>();
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player target;
        boolean terminal = commandSender instanceof ConsoleCommandSender;
        boolean perm = commandSender.hasPermission("ericlam.hideplayer");
        boolean permother = commandSender.hasPermission("ericlam.hideplayer.other");
        if (strings.length <= 0 && perm) {
            if (!terminal) {
                Player player = (Player) commandSender;
                try {
                    HidePlayer(player, player);
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            }else{commandSender.sendMessage(ChatColor.RED + "Console can only use /speed <player>");}
        } else if(permother || terminal){
            target = (Bukkit.getServer().getPlayer(strings[0]));
            if (target == null){
                commandSender.sendMessage(prefix + Variable.returnColoredMessage("General.Player-Not-Found"));
            }else {
                try {
                    HidePlayer(target, commandSender);
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            }
        }else{
            commandSender.sendMessage(prefix + noperm);
        }
        return true;
    }

    public static void HidePlayer(Player name, CommandSender sender) throws IOException, SQLException {
        Player p = name.getPlayer();
        UUID puuid = p.getUniqueId();
        boolean nohide = !vanished.contains(p);
        if (sender != name) sender.sendMessage(prefix + returnColoredMessage("Commands.HidePlayer.be-" + (nohide ? "hide":"show")).replace("<player>",name.getDisplayName()));
        name.sendMessage(prefix + returnColoredMessage("Commands.HidePlayer." + (nohide ? "hide":"show")));
        Collection<? extends Player> online = Bukkit.getServer().getOnlinePlayers();
        if (nohide) {
            vanished.add(p);
            for (Player onlinep : online) {
                p.hidePlayer(PlayerSettings.plugin, onlinep);
            }
        }
        else {
            vanished.remove(p);
            for (Player onlinep : online) {
                p.showPlayer(PlayerSettings.plugin, onlinep);
            }
        }
        if (yaml) Variable.setYml("HidePlayer",puuid,nohide);
        if (MYsql){
            MySQL mysql = MySQL.getinstance();
            PreparedStatement ps = mysql.connection.prepareStatement("UPDATE "+table+" SET HidePlayer=? WHERE PlayerUUID = ?");
            ps.setInt(1,(nohide ? 1 : 0));
            ps.setString(2, puuid.toString());
            ps.execute();
        }
    }
}
