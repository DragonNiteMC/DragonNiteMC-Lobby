package com.ericlam.mc.hnmclobby.command;

import com.ericlam.mc.hnmclobby.config.LobbyConfig;
import com.ericlam.mc.hnmclobby.main.HNMCLobby;
import com.hypernite.mc.hnmc.core.main.HyperNiteMC;
import com.hypernite.mc.hnmc.core.managers.CoreConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.io.IOException;

public class SetLobbyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String s, @Nonnull String[] strings) {
        CoreConfig msg = HyperNiteMC.getAPI().getCoreConfig();
        LobbyConfig lobbyConfig = HNMCLobby.getConfigManager().getConfigAs(LobbyConfig.class);
        if (commandSender instanceof Player) {
            if (commandSender.hasPermission("hypernite.setlobby")) {
                Player player = (Player) commandSender;
                lobbyConfig.spawntp = player.getLocation();
                try {
                    lobbyConfig.save();
                    player.sendMessage(msg.getPrefix() + ChatColor.GREEN + "重生點保存成功!");
                } catch (IOException e) {
                    e.printStackTrace();
                    player.sendMessage(ChatColor.RED + "出現問題, 我們無法保存你的記錄!");
                }
            } else {
                commandSender.sendMessage(msg.getPrefix() + msg.getNoPerm());
            }
        } else {
            commandSender.sendMessage(msg.getNotPlayer());
        }
        return true;
    }
}
