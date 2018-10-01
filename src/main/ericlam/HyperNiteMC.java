package main.ericlam;

import addon.ericlam.GUIBuilder;
import addon.ericlam.Variable;
import com.caxerx.mc.PlayerSettingManager;
import command.ericlam.*;
import functions.hypernite.mc.Functions;
import lobby.listener.BasicListener;
import lobby.listener.LobbyJoinItem;
import mysql.hypernite.mc.SQLDataSourceManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import playersettings.listener.*;

import java.sql.SQLException;

public class HyperNiteMC extends JavaPlugin {
    public static Plugin plugin;
    public void onEnable() {
        plugin = this;
        ConsoleCommandSender console = getServer().getConsoleSender();
        console.sendMessage(ChatColor.YELLOW + "===========================================");
        console.sendMessage(ChatColor.GOLD + "HyperNiteMC Lobby Enabled!");
        String[] commands = {"fly", "heal", "hidechat", "stacker", "speed", "hideplayer", "Settings", "setlobby", "spawn"};

        CommandExecutor[] cmdexecutor = {
                new FlyExe(this), new HealExe(this), new HideChatExe(this), new StackerExe(this), new SpeedExe(this),
                new HidePlayerExe(this), new SettingsExe(this), new SetLobbyExe(this), new SpawnExe(this)};

        for (int i = 0; i < commands.length; i++) {
            String cmd = commands[i];
            CommandExecutor cmdexe = cmdexecutor[i];
            this.getCommand(cmd).setExecutor(cmdexe);
        }

        Listener[] listeners = {
                new OnPlayerChat(), new OnPlayerInteract(), new OnPlayerInteractEntity(), new OnPlayerJoin(), new OnInventoryClick(), new OnPlayerLeave(),
                new BasicListener(), new LobbyJoinItem()};

        for (Listener listen : listeners) {
            getServer().getPluginManager().registerEvents(listen, this);
        }
        Functions f = new Functions(this);
        f.addNewFile("Messages.yml");
        f.addNewFile("config.yml");
        f.addNewFile("Lobby.yml");
        Variable var = new Variable();
        PlayerSettingManager psm = PlayerSettingManager.getInstance();
        try {
            psm.needMySQL();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (var.isYaml()) {
            console.sendMessage(ChatColor.AQUA + "Using YAML as saving Type.");
        }
        if (var.isMySQL()) {
            console.sendMessage(ChatColor.AQUA + "Using MYSQL as saving Type.");
            try {
                SQLDataSourceManager mysql = SQLDataSourceManager.getInstance();
                mysql.getFuckingConnection().createStatement().execute("CREATE TABLE IF NOT EXISTS `PS_stats` (`PlayerUUID` VARCHAR(40) NOT NULL PRIMARY KEY, `Fly` bit, `HideChat` bit, `Stacker` bit, `Speed` bit, `HidePlayer` bit)");
                mysql.getFuckingConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        GUIBuilder start = GUIBuilder.getInstance();
        start.getInventoryGUI();
        console.sendMessage(ChatColor.YELLOW + "===========================================");
    }

    public void onDisable() {
        Variable var = new Variable();
        if (var.isMySQL()) {
            try {
                PlayerSettingManager.getInstance().saveAll();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Can't save all player's data, a data loss can be cause.");
            }
        }
        getLogger().info("HyperNiteMC-Lobby Disabled.");
    }

}


