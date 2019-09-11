package com.hypernite.mc.hnmc.lobby.ericlam.command;

import com.hypernite.mc.hnmc.core.main.HyperNiteMC;
import com.hypernite.mc.hnmc.core.managers.CoreConfig;
import com.hypernite.mc.hnmc.lobby.main.HNMCLobby;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class SetLobbyCommand implements CommandExecutor {
    private final HNMCLobby plugin;
    public SetLobbyCommand(HNMCLobby plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String s, @Nonnull String[] strings) {
        CoreConfig msg = HyperNiteMC.getAPI().getCoreConfig();
        if (commandSender instanceof Player){
            if (commandSender.hasPermission("hypernite.setlobby")) {
                Player player = (Player) commandSender;
                Location set = player.getLocation();
                Double Y = set.getY();
                Double X = set.getX();
                Double Z = set.getZ();
                World setworld = set.getWorld();
                Double Yaw = (double) set.getYaw();
                Double Pitch = (double) set.getPitch();
                CompletableFuture.runAsync(()->{
                    FileConfiguration lobbyfile = HNMCLobby.getConfigManager().getFileConfig("Lobby.yml");
                    lobbyfile.set("spawntp.y", Y);
                    lobbyfile.set("spawntp.x", X);
                    lobbyfile.set("spawntp.z", Z);
                    lobbyfile.set("spawntp.yaw", Yaw);
                    lobbyfile.set("spawntp.pitch", Pitch);
                    lobbyfile.set("spawntp.world", setworld.getName());
                    try {
                        File lobby = new File(plugin.getDataFolder(), "Lobby.yml");
                        lobbyfile.save(lobby);
                        YamlConfiguration.loadConfiguration(lobby);
                    } catch (IOException e) {
                        throw new CompletionException(e);
                    }
                }).whenComplete((e,ex)->{
                    if (ex == null){
                        player.sendMessage(msg.getPrefix() + ChatColor.GREEN + "重生點保存成功!");
                    }else{
                        ex.getCause().printStackTrace();
                        player.sendMessage(ChatColor.RED + "出現問題, 我們無法保存你的記錄!");

                    }
                });
            }else {
                commandSender.sendMessage(msg.getPrefix() + msg.getNoPerm());
            }
        } else {
            commandSender.sendMessage(msg.getNotPlayer());
        }
        return true;
    }
}
