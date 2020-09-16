package com.ericlam.mc.hnmclobby.main;

import com.ericlam.mc.hnmclobby.command.*;
import com.ericlam.mc.hnmclobby.config.LobbyConfig;
import com.ericlam.mc.hnmclobby.config.MainConfig;
import com.ericlam.mc.hnmclobby.config.MessagesConfig;
import com.ericlam.mc.hnmclobby.listener.BasicListener;
import com.ericlam.mc.hnmclobby.listener.LobbyJoinListener;
import com.ericlam.mc.hnmclobby.listener.OnPlayerListener;
import com.ericlam.mc.hnmclobby.manager.GUIManager;
import com.ericlam.mc.hnmclobby.manager.LobbyManager;
import com.ericlam.mc.hnmclobby.manager.PlayerSettingManager;
import com.ericlam.mc.hnmclobby.runnable.RestartRunnable;
import com.hypernite.mc.hnmc.core.main.HyperNiteMC;
import com.hypernite.mc.hnmc.core.managers.CommandRegister;
import com.hypernite.mc.hnmc.core.managers.YamlManager;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class HNMCLobby extends JavaPlugin {
    private static YamlManager yamlManager;
    private PlayerSettingManager playerSettingManager;

    public static YamlManager getConfigManager() {
        return yamlManager;
    }

    public void onEnable() {
        ConsoleCommandSender console = getServer().getConsoleSender();
        console.sendMessage(ChatColor.YELLOW + "===========================================");

        /*
         * Load ConfigManager File and Initial
         */

        yamlManager = HyperNiteMC.getAPI().getFactory().getConfigFactory(this)
                .register("config.yml", MainConfig.class)
                .register("Lobby.yml", LobbyConfig.class)
                .register("Messages.yml", MessagesConfig.class)
                .dump();

        /*
         * Register Command
         */

        final MainConfig mainConfig = yamlManager.getConfigAs(MainConfig.class);
        final LobbyConfig lobbyConfig = yamlManager.getConfigAs(LobbyConfig.class);
        final MessagesConfig messagesConfig = yamlManager.getConfigAs(MessagesConfig.class);
        playerSettingManager = new PlayerSettingManager();
        final GUIManager guiManager = new GUIManager(playerSettingManager);
        final LobbyManager lobbyManager = new LobbyManager(lobbyConfig);

        CommandRegister register = HyperNiteMC.getAPI().getCommandRegister();
        register.registerCommand(this, new FlyCommand(playerSettingManager));
        register.registerCommand(this, new HealCommand());
        register.registerCommand(this, new HideChatCommand(playerSettingManager));
        register.registerCommand(this, new HidePlayerCommand(playerSettingManager, this));
        register.registerCommand(this, new StackerCommand(playerSettingManager));
        register.registerCommand(this, new SpeedCommand(playerSettingManager));
        register.registerCommand(this, new HideChatCommand(playerSettingManager));
        register.registerCommand(this, new SettingsCommand(guiManager));
        register.registerCommand(this, new SpawnCommand(lobbyManager));
        this.getCommand("setlobby").setExecutor(new SetLobbyCommand());

        /*
         * Register Event Listener
         */

        final Listener[] listeners = {
                new OnPlayerListener(yamlManager, guiManager, this, playerSettingManager),
                new BasicListener(messagesConfig, lobbyManager, guiManager, this),
                new LobbyJoinListener(this)
        };

        for (Listener listen : listeners) {
            getServer().getPluginManager().registerEvents(listen, this);
        }

        console.sendMessage(ChatColor.AQUA + "Using MYSQL as saving Type.");
        console.sendMessage(ChatColor.GOLD + "HyperNiteMC Lobby Enabled!");
        console.sendMessage(ChatColor.YELLOW + "===========================================");
        new RestartRunnable(this).runTaskTimerAsynchronously(this, 0L, mainConfig.restart.checkInterval * 20L);
    }

    public void onDisable() {
        playerSettingManager.saveAll();
        getLogger().info("HyperNiteMC-Lobby Disabled.");
    }

}


