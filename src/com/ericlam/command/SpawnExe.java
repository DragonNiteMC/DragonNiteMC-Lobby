package com.ericlam.command;

import com.ericlam.addon.TeleportLobby;
import com.ericlam.addon.Variable;
import main.HyperNiteMC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static com.ericlam.addon.Variable.messagefile;

public class SpawnExe implements CommandExecutor {
    public final HyperNiteMC plugin;
    public SpawnExe(HyperNiteMC plugin){
        this.plugin = plugin;
    }
    private Variable var = new Variable();
    private TeleportLobby lobby = TeleportLobby.getInstance();
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player target;
        boolean terminal = commandSender instanceof ConsoleCommandSender;
        boolean permother = commandSender.hasPermission("hypernite.spawn.other");
        if (strings.length <= 0) {
            if (!terminal) {
                Player player = (Player) commandSender;
                if (lobby.TeleportToLobby(player)) player.sendMessage(var.prefix() + var.getFs().returnColoredMessage(messagefile, "Commands.spawn.send"));
            }else{commandSender.sendMessage(ChatColor.RED + "Console can only use /settings <player>");}
        } else if(permother || terminal){
            target = (Bukkit.getServer().getPlayer(strings[0]));
            if (target == null){
                commandSender.sendMessage(var.prefix() + var.getFs().returnColoredMessage(messagefile,"General.Player-Not-Found"));
            }else {
                lobby.TeleportToLobby(target,commandSender);
            }
        }else{
            commandSender.sendMessage(var.prefix() + var.noperm());
        }
        return true;
    }
}
