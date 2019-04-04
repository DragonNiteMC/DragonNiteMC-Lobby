package com.ericlam.command;


import com.caxerx.mc.PlayerSettingManager;
import com.ericlam.addon.ConfigManager;
import main.HyperNiteMC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.UUID;



public class FlyExe implements CommandExecutor {
    private final HyperNiteMC plugin;
    private ConfigManager var;
    public FlyExe(HyperNiteMC plugin){
        this.plugin = plugin;
        var = HyperNiteMC.getConfigManager();
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
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{commandSender.sendMessage(ChatColor.RED + "Console can only use /fly <player>");}
            } else if(perm && permother || terminal){
                target = (Bukkit.getServer().getPlayer(strings[0]));
                if (target == null){
                    commandSender.sendMessage(var.getMessage("General.Player-Not-Found"));
                }else {
                    try {
                        flyExecutor(target, commandSender);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                commandSender.sendMessage(var.noperm());
            }
            return true;
    }
    private void flyExecutor(Player name, CommandSender sender) throws IOException {
        Player p = name.getPlayer();
        UUID puuid = p.getUniqueId();
        PlayerSettingManager psm = PlayerSettingManager.getInstance();
        boolean fly = !psm.getPlayerSetting(puuid).isFly();
        name.setAllowFlight(fly);
        name.setFlying(fly);
        if (!var.isMySQL()) ConfigManager.setYml("Flight", puuid, fly);
        psm.getPlayerSetting(puuid).setFly(fly);
        name.sendMessage(var.getMessage("Commands.Fly.Turn-" + (fly ? "On":"Off")));
        if (name != sender) {
            sender.sendMessage(var.getMessage("Commands.Fly.Be-Turn-" + (fly ? "On" : "Off")).replace("<player>", name.getName()));
        }
    }
}

