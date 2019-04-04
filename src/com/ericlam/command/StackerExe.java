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

public class StackerExe implements CommandExecutor{
    private final HyperNiteMC plugin;
    private ConfigManager var;
    public StackerExe(HyperNiteMC plugin) {
        this.plugin = plugin;
        var = HyperNiteMC.getConfigManager();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
            Player target;
            boolean terminal = commandSender instanceof ConsoleCommandSender;
            boolean perm = commandSender.hasPermission("settings.stacker");
            boolean permother = commandSender.hasPermission("settings.stacker.other");
            if (strings.length <= 0 && perm) {
                if (!terminal) {
                    Player player = (Player) commandSender;
                    try {
                        StackerOn(player, player);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else commandSender.sendMessage(ChatColor.RED + "Console can only use /stacker <player>");
            } else if (perm && permother || terminal) {
                target = Bukkit.getPlayer(strings[0]);
                if (target == null)
                    commandSender.sendMessage(var.getMessage("General.Player-Not-Found"));
                else {
                    try {
                        StackerOn(target, commandSender);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                commandSender.sendMessage(var.prefix() + var.noperm());
            }
            return true;
    }
    private void StackerOn(Player name, CommandSender sender) throws IOException {
        if (var.config.getBoolean("Stacker.Enable")) {
            Player player = name.getPlayer();
            UUID puuid = player.getUniqueId();
            PlayerSettingManager psm = PlayerSettingManager.getInstance();
            boolean nostack = !psm.getPlayerSetting(puuid).isStacker();
            if (sender != name)
                sender.sendMessage(var.getMessage("Commands.Stacker.be-" + (nostack ? "enable" : "disable")).replace("<player>", name.getName()));
            name.sendMessage(var.getMessage("Commands.Stacker." + (nostack ? "enable" : "disable")));
            psm.getPlayerSetting(puuid).setStacker(nostack);
            if (!var.isMySQL()) ConfigManager.setYml("Stacker", puuid, nostack);
        } else sender.sendMessage(var.prefix() + ChatColor.RED + "此功能已被管理員禁用。");
    }
}

