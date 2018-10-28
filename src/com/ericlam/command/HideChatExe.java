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
import java.util.UUID;

import static com.ericlam.addon.Variable.messagefile;

public class HideChatExe implements CommandExecutor{
    private final HyperNiteMC plugin;
    private Variable var = new Variable();
    public HideChatExe(HyperNiteMC plugin) {
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else commandSender.sendMessage(ChatColor.RED + "Console can only use /hidechat <player>");
        } else if (perm && permother || terminal) {
            target = Bukkit.getPlayer(strings[0]);
            if (target == null) commandSender.sendMessage(var.prefix() + var.getFs().returnColoredMessage(messagefile,"General.Player-Not-Found"));
            else {
                try {
                    HideChat(target,commandSender);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{commandSender.sendMessage(var.prefix() + var.noperm());}
        return true;
    }

    private void HideChat(Player name, CommandSender sender) throws IOException {
        Player player = name.getPlayer();
        UUID puuid = player.getUniqueId();
        PlayerSettingManager psm = PlayerSettingManager.getInstance();
        boolean hide = !psm.getPlayerSetting(puuid).isHideChat();
        if (sender != name)  sender.sendMessage(var.prefix() + var.getFs().returnColoredMessage(messagefile,"Commands.HideChat.be-" + (hide ? "hide" : "show")).replace("<player>", name.getName()));
        name.sendMessage(var.prefix() + var.getFs().returnColoredMessage(messagefile,"Commands.HideChat." + (hide ? "hide" : "show")));
        psm.getPlayerSetting(puuid).setHideChat(hide);
        if (!var.isMySQL()) Variable.setYml("HideChat",puuid,hide);
    }
}

