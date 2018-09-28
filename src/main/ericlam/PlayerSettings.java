package main.ericlam;
import CmdExecute.ericlam.*;
import Listener.*;
import MySQL.HyperNite.SQLDataSourceManager;
import addon.ericlam.MySQL;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import static addon.ericlam.Variable.*;

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
        addNewFile("Messages.yml");
        addNewFile("config.yml");
        if (yaml) {
            console.sendMessage(ChatColor.AQUA + "Using YAML as saving Type.");
        }
        if(MYsql) {
            console.sendMessage(ChatColor.AQUA + "Using MYSQL as saving Type.");
            try {
                SQLDataSourceManager mysql = SQLDataSourceManager.getInstance();
                mysql.getFuckingConnection().createStatement().execute("CREATE TABLE IF NOT EXISTS `PS_stats` (`PlayerUUID` VARCHAR(40) NOT NULL PRIMARY KEY, `Fly` bit, `HideChat` bit, `Stacker` bit, `Speed` bit, `HidePlayer` bit)");
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

    private static void addNewFile(String pathname) {
        File filename = new File(plugin.getDataFolder(), pathname);
        if (!filename.exists()) plugin.saveResource(pathname, true);
        YamlConfiguration.loadConfiguration(filename);
    }
}

