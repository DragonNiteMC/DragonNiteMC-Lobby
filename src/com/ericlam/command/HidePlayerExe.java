package com.ericlam.command;

import com.caxerx.mc.PlayerSettingManager;
import com.ericlam.addon.Variable;
import main.HyperNiteMC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

import static com.ericlam.addon.Variable.messagefile;

public class HidePlayerExe implements CommandExecutor {
    private final HyperNiteMC plugin;
    public HidePlayerExe(HyperNiteMC plugin){ this.plugin = plugin;}
    private Variable var = new Variable();
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player target;
        boolean terminal = commandSender instanceof ConsoleCommandSender;
        boolean perm = commandSender.hasPermission("settings.hideplayer");
        boolean permother = commandSender.hasPermission("settings.hideplayer.other");
        if (strings.length <= 0 && perm) {
            if (!terminal) {
                Player player = (Player) commandSender;
                try {
                    HidePlayer(player, player);
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            }else{commandSender.sendMessage(ChatColor.RED + "Console can only use /hideplayer <player>");}
        } else if(perm && permother || terminal){
            target = (Bukkit.getServer().getPlayer(strings[0]));
            if (target == null){
                commandSender.sendMessage(var.prefix() + var.getFs().returnColoredMessage(messagefile,"General.Player-Not-Found"));
            }else {
                try {
                    HidePlayer(target, commandSender);
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            }
        }else{
            commandSender.sendMessage(var.prefix() + var.noperm());
        }
        return true;
    }

    private void HidePlayer(Player name, CommandSender sender) throws IOException, SQLException {
        Player p = name.getPlayer();
        UUID puuid = p.getUniqueId();
        PlayerSettingManager psm = PlayerSettingManager.getInstance();
        boolean nohide = !psm.getPlayerSetting(puuid).isHidePlayer();
        if (sender != name) sender.sendMessage(var.prefix() + var.getFs().returnColoredMessage(messagefile,"Commands.HidePlayer.be-" + (nohide ? "hide":"show")).replace("<player>",name.getName()));
        name.sendMessage(var.prefix() + var.getFs().returnColoredMessage(messagefile,"Commands.HidePlayer." + (nohide ? "hide":"show")));
        psm.getPlayerSetting(puuid).setHidePlayer(nohide);
           for (Player onlinep : Bukkit.getServer().getOnlinePlayers()) {
               if (nohide) {
                   p.hidePlayer(HyperNiteMC.plugin, onlinep);
               } else {
                   p.showPlayer(HyperNiteMC.plugin, onlinep);
               }
           }
        if (!var.isMySQL()) Variable.setYml("HidePlayer",puuid,nohide);
    }
}
