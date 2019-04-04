package com.ericlam.command;

import com.ericlam.addon.ConfigManager;
import com.ericlam.addon.TeleportLobby;
import main.HyperNiteMC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class SpawnExe implements CommandExecutor {
    public final HyperNiteMC plugin;
    public SpawnExe(HyperNiteMC plugin){
        this.plugin = plugin;
        var = HyperNiteMC.getConfigManager();
        lobby = TeleportLobby.getInstance();
    }

    private ConfigManager var;
    private TeleportLobby lobby;
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player target;
        boolean terminal = commandSender instanceof ConsoleCommandSender;
        boolean permother = commandSender.hasPermission("hypernite.spawn.other");
        if (strings.length <= 0) {
            if (!terminal) {
                Player player = (Player) commandSender;
                if (lobby.TeleportToLobby(player)) player.sendMessage(var.getMessage("Commands.spawn.send"));
            }else{commandSender.sendMessage(ChatColor.RED + "Console can only use /settings <player>");}
        } else if(permother || terminal){
            target = (Bukkit.getServer().getPlayer(strings[0]));
            if (target == null){
                commandSender.sendMessage(var.getMessage("General.Player-Not-Found"));
            }else {
                lobby.TeleportToLobby(target,commandSender);
            }
        }else{
            commandSender.sendMessage(var.noperm());
        }
        return true;
    }
}
