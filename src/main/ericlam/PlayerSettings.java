package main.ericlam;

import addon.ericlam.Variable;
import command.ericlam.*;
import eventlistener.*;
import functions.hypernite.mc.Functions;
import mysql.hypernite.mc.SQLDataSourceManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import static addon.ericlam.Variable.MYsql;
import static addon.ericlam.Variable.yaml;

public class PlayerSettings extends JavaPlugin {
    public static Plugin plugin;
    public void onEnable() {
        plugin = this;
        ConsoleCommandSender console = getServer().getConsoleSender();
        console.sendMessage(ChatColor.YELLOW + "===========================================");
        console.sendMessage(ChatColor.GOLD + "PlayerSettings Enabled!");
        console.sendMessage(ChatColor.LIGHT_PURPLE + "Plugin Created by EricLam");
        console.sendMessage(ChatColor.GREEN + "Remember use /help for help!");
        String[] commands = {"fly", "heal", "ping", "hidechat", "stacker", "speed", "hideplayer", "Settings"};
        CommandExecutor[] cmdexecutor = {new FlyExe(this), new HealExe(this), new PingExe(this), new HideChatExe(this), new StackerExe(this), new SpeedExe(this), new HidePlayerExe(this), new SettingsExe(this)};
        for (int i = 0; i < commands.length; i++) {
            String cmd = commands[i];
            CommandExecutor cmdexe = cmdexecutor[i];
            this.getCommand(cmd).setExecutor(cmdexe);
        }
        Listener[] listeners = {new OnPlayerChat(), new OnPlayerInteract(), new OnPlayerInteractEntity(), new OnPlayerJoin(), new OnPlayerSneak(), new OnInventoryClick(), new OnPlayerLeave()};
        for (Listener listen : listeners) {
            getServer().getPluginManager().registerEvents(listen, this);
        }
        Functions f = new Functions(this);
        f.addNewFile("Messages.yml");
        f.addNewFile("config.yml");
        if (yaml) {
            console.sendMessage(ChatColor.AQUA + "Using YAML as saving Type.");
        }
        if(MYsql) {
            console.sendMessage(ChatColor.AQUA + "Using MYSQL as saving Type.");
            try {
                SQLDataSourceManager mysql = SQLDataSourceManager.getInstance();
                mysql.getFuckingConnection().createStatement().execute("CREATE TABLE IF NOT EXISTS `PS_stats` (`PlayerUUID` VARCHAR(40) NOT NULL PRIMARY KEY, `Fly` bit, `HideChat` bit, `Stacker` bit, `Speed` bit, `HidePlayer` bit)");
                mysql.getFuckingConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        SettingsExe start = SettingsExe.getInstance();
        start.getInventoryGUI();
        console.sendMessage(ChatColor.YELLOW + "===========================================");
    }

    public void onDisable() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            UUID puuid = player.getUniqueId();
            boolean Fly = player.getAllowFlight();
            boolean Speed = player.hasPotionEffect(PotionEffectType.SPEED);
            boolean HidePlayer = HidePlayerExe.vanished.contains(player);
            boolean HideChat = HideChatExe.chatdisabled.contains(puuid);
            boolean Stacker = StackerExe.stackerenabled.contains(puuid);
            if (Variable.MYsql) {
                try {
                    SQLDataSourceManager mysql = SQLDataSourceManager.getInstance();
                    PreparedStatement ps = mysql.getFuckingConnection().prepareStatement("INSERT IGNORE PS_stats VALUES (?, ?, ?, ?, ?, ?);");
                    ps.setString(1, puuid.toString());
                    ps.setInt(2, 0);
                    ps.setInt(3, 0);
                    ps.setInt(4, 0);
                    ps.setInt(5, 0);
                    ps.setInt(6, 0);
                    ps.execute();
                    ps.close();
                    mysql.getFuckingConnection().close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    SQLDataSourceManager mysql = SQLDataSourceManager.getInstance();
                    PreparedStatement ps = mysql.getFuckingConnection().prepareStatement("UPDATE PS_stats SET Fly=?, Speed=?, HidePlayer=?, HideChat=?, Stacker=? WHERE PlayerUUID = ?");
                    ps.setInt(1, (Fly ? 1 : 0));
                    ps.setInt(2, (Speed ? 1 : 0));
                    ps.setInt(3, (HidePlayer ? 1 : 0));
                    ps.setInt(4, (HideChat ? 1 : 0));
                    ps.setInt(5, (Stacker ? 1 : 0));
                    ps.setString(6, puuid.toString());
                    ps.execute();
                    ps.close();
                    mysql.getFuckingConnection().close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        getLogger().info("PlayerSettings Disabled.");
        }

    }


