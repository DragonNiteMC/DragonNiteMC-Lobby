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
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import static addon.ericlam.Variable.*;
import static main.ericlam.PlayerSettings.plugin;


public class FlyExe implements CommandExecutor {

    private final PlayerSettings plugin;
    public FlyExe(PlayerSettings plugin){
        this.plugin = plugin;
    }
        @Override
        public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
            Player target;
            boolean terminal = commandSender instanceof ConsoleCommandSender;
            boolean perm = commandSender.hasPermission("ericlam.fly");
            boolean permother = commandSender.hasPermission("ericlam.fly.other");
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
                    commandSender.sendMessage(prefix + Variable.returnColoredMessage("General.Player-Not-Found"));
                }else {
                    try {
                        flyExecutor(target, commandSender);
                    } catch (IOException | SQLException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                commandSender.sendMessage(prefix + noperm);
            }
            return true;
    }
    public static void flyExecutor(Player name,CommandSender sender) throws IOException, SQLException {
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
        name.sendMessage(prefix + Variable.returnColoredMessage("Commands.Fly.Turn-" + (fly ? "On":"Off")));
        if (name != sender) {
            sender.sendMessage(prefix + Variable.returnColoredMessage("Commands.Fly.Be-Turn-" + (fly ? "On" : "Off")).replace("<player>", name.getDisplayName()));
        }
    }
}

