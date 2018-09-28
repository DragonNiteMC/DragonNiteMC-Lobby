package main.ericlam;

import command.ericlam.*;
import functions.hypernite.mc.Functions;
import listener.*;
import mysql.hypernite.mc.SQLDataSourceManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

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
        Listener[] listeners = {new OnPlayerChat(), new OnPlayerInteract(), new OnPlayerInteractEntity(), new OnPlayerJoin(), new OnPlayerSneak(), new OnInventoryClick()};
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
        getLogger().info("PlayerSettings Disabled.");
    }

}

