package command.ericlam;


import addon.ericlam.Variable;
import main.ericlam.PlayerSettings;
import mysql.hypernite.mc.SQLDataSourceManager;
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
import java.util.UUID;

import static addon.ericlam.Variable.messagefile;
import static addon.ericlam.Variable.yaml;


public class FlyExe implements CommandExecutor {
    private Variable var = Variable.getInstance();
    private final PlayerSettings plugin;
    public FlyExe(PlayerSettings plugin){
        this.plugin = plugin;
    }
        @Override
        public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
            Player target;
            boolean terminal = commandSender instanceof ConsoleCommandSender;
            boolean perm = commandSender.hasPermission("settings.fly");
            boolean permother = commandSender.hasPermission("settings.fly.other");
            if (strings.length <= 0 && perm) {
                if (!terminal) {
                    Player player = (Player) commandSender;
                    try {
                        flyExecutor(player, player);
                    } catch (IOException | SQLException e) {
                        e.printStackTrace();
                    }
                }else{commandSender.sendMessage(ChatColor.RED + "Console can only use /fly <player>");}
            } else if(permother || terminal){
                target = (Bukkit.getServer().getPlayer(strings[0]));
                if (target == null){
                    commandSender.sendMessage(var.prefix() + var.getFs().returnColoredMessage(messagefile, "General.Player-Not-Found"));
                }else {
                    try {
                        flyExecutor(target, commandSender);
                    } catch (IOException | SQLException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                commandSender.sendMessage(var.prefix() + var.noperm());
            }
            return true;
    }
    public void flyExecutor(Player name,CommandSender sender) throws IOException, SQLException {
        Player p = name.getPlayer();
        UUID puuid = p.getUniqueId();
        boolean fly = !p.getAllowFlight();
        name.setAllowFlight(fly);
        name.setFlying(fly);
        if (yaml) {
            Variable.setYml("Flight",puuid,fly);
        }
        if (Variable.MYsql){
            SQLDataSourceManager mysql = SQLDataSourceManager.getInstance();
            PreparedStatement ps = mysql.getFuckingConnection().prepareStatement("UPDATE PS_stats SET Fly=? WHERE PlayerUUID = ?");
            ps.setInt(1, (fly ? 1 : 0));
            ps.setString(2, puuid.toString());
            ps.execute();
        }
        name.sendMessage(var.prefix() + var.getFs().returnColoredMessage(messagefile,"Commands.Fly.Turn-" + (fly ? "On":"Off")));
        if (name != sender) {
            sender.sendMessage(var.prefix() + var.getFs().returnColoredMessage(messagefile,"Commands.Fly.Be-Turn-" + (fly ? "On" : "Off")).replace("<player>", name.getDisplayName()));
        }
    }
}

