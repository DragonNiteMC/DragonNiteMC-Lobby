package com.hypernite.mc.hnmc.lobby.main;

import com.hypernite.mc.hnmc.core.main.HyperNiteMC;
import com.hypernite.mc.hnmc.core.managers.CommandRegister;
import com.hypernite.mc.hnmc.core.managers.ConfigManager;
import com.hypernite.mc.hnmc.lobby.caxerx.PlayerSettingManager;
import com.hypernite.mc.hnmc.lobby.ericlam.addon.LobbyConfig;
import com.hypernite.mc.hnmc.lobby.ericlam.command.*;
import com.hypernite.mc.hnmc.lobby.ericlam.listener.lobby.BasicListener;
import com.hypernite.mc.hnmc.lobby.ericlam.listener.lobby.LobbyJoinItem;
import com.hypernite.mc.hnmc.lobby.ericlam.listener.playersettings.*;
import com.hypernite.mc.hnmc.lobby.ericlam.restart.ScheduleRestart;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class HNMCLobby extends JavaPlugin {
    public static Plugin plugin;
    private static ConfigManager configManager;
    private static LobbyConfig lobbyConfig;

    public static ConfigManager getConfigManager() {
        return configManager;
    }

    public static LobbyConfig getLobbyConfig() {
        return lobbyConfig;
    }

    public static boolean isMySQL;

    public void onEnable() {
        plugin = this;
        ConsoleCommandSender console = getServer().getConsoleSender();
        console.sendMessage(ChatColor.YELLOW + "===========================================");

        /*
         * Load ConfigManager File and Initial
         */

        lobbyConfig = new LobbyConfig(this);
        configManager = HyperNiteMC.getAPI().registerConfig(lobbyConfig);
        configManager.setMsgConfig("Messages.yml");
        isMySQL = lobbyConfig.isMySQL();

        /*
         * Register Command
         */

        CommandRegister register = HyperNiteMC.getAPI().getCommandRegister();
        register.registerCommand(this,new FlyCommand());
        register.registerCommand(this,new HealCommand());
        register.registerCommand(this,new HideChatCommand());
        register.registerCommand(this,new HidePlayerCommand());
        register.registerCommand(this,new StackerCommand());
        register.registerCommand(this,new SpeedCommand());
        register.registerCommand(this,new HideChatCommand());
        register.registerCommand(this,new SettingsCommand());
        register.registerCommand(this,new SpawnCommand());
        this.getCommand("setlobby").setExecutor(new SetLobbyCommand(this));

        /*
         * Register Event Listener
         */

        Listener[] listeners = { new OnPlayerEvent(), new BasicListener(), new LobbyJoinItem() };

        for (Listener listen : listeners) {
            getServer().getPluginManager().registerEvents(listen, this);
        }

        if (lobbyConfig.isMySQL()) console.sendMessage(ChatColor.AQUA + "Using MYSQL as saving Type.");
        else console.sendMessage(ChatColor.AQUA + "Using YAML as saving Type.");


        console.sendMessage(ChatColor.GOLD + "HyperNiteMC Lobby Enabled!");
        console.sendMessage(ChatColor.YELLOW + "===========================================");
        new ScheduleRestart();
    }

    public void onDisable() {
        PlayerSettingManager.getInstance().saveAll();
        getLogger().info("HyperNiteMC-Lobby Disabled.");
    }

}


