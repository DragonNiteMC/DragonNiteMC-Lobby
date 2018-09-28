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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import static addon.ericlam.Variable.*;

public class SpeedExe implements CommandExecutor {
    private final PlayerSettings plugin;
    public SpeedExe(PlayerSettings plugin){ this.plugin = plugin;}
    private Variable var = Variable.getInstance();
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player target;
        boolean terminal = commandSender instanceof ConsoleCommandSender;
        boolean perm = commandSender.hasPermission("settings.speed");
        boolean permother = commandSender.hasPermission("settings.speed.other");
        if (strings.length <= 0 && perm) {
            if (!terminal) {
                Player player = (Player) commandSender;
                try {
                    SetSpeed(player, player);
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            }else{commandSender.sendMessage(ChatColor.RED + "Console can only use /speed <player>");}
        } else if(permother || terminal){
            target = (Bukkit.getServer().getPlayer(strings[0]));
            if (target == null){
                commandSender.sendMessage(var.prefix() + var.getFs().returnColoredMessage(messagefile,"General.Player-Not-Found"));
            }else {
                try {
                    SetSpeed(target, commandSender);
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            }
        }else{
            commandSender.sendMessage(var.prefix() + var.noperm());
        }
        return true;
    }

    public void SetSpeed(Player name, CommandSender sender) throws IOException, SQLException {
        Player p = name.getPlayer();
        UUID puuid = p.getUniqueId();
        boolean speed = !p.hasPotionEffect(PotionEffectType.SPEED);
        int amplifier = config.getInt("Speed.Level") - 1;
        if (sender != name) sender.sendMessage(var.prefix() + var.getFs().returnColoredMessage(messagefile,"Commands.Speed.Be-Turn-" + (speed ? "On":"Off")).replace("<player>",name.getDisplayName()));
        name.sendMessage(var.prefix() + var.getFs().returnColoredMessage(messagefile,"Commands.Speed.Turn-" + (speed ? "On":"Off")));
        if (speed) p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, amplifier));
        else p.removePotionEffect(PotionEffectType.SPEED);
        if (yaml) Variable.setYml("Speed",puuid,speed);
        if (MYsql){
            SQLDataSourceManager mysql = SQLDataSourceManager.getInstance();
            PreparedStatement ps = mysql.getFuckingConnection().prepareStatement("UPDATE PS_stats SET Speed=? WHERE PlayerUUID = ?");
            ps.setInt(1,(speed ? 1 : 0));
            ps.setString(2, puuid.toString());
            ps.execute();
            mysql.getFuckingConnection().close();
            ps.close();
        }
    }
}
