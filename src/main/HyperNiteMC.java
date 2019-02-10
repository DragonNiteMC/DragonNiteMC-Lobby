package main;

import com.caxerx.mc.PlayerSettingManager;
import com.ericlam.addon.ConfigManager;
import com.ericlam.command.*;
import com.ericlam.listener.lobby.BasicListener;
import com.ericlam.listener.lobby.LobbyJoinItem;
import com.ericlam.listener.playersettings.*;
import com.ericlam.restart.ScheduleRestart;
import com.hypernite.functions.Functions;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class HyperNiteMC extends JavaPlugin {
    public static Plugin plugin;

    public void onEnable() {
        plugin = this;
        ConsoleCommandSender console = getServer().getConsoleSender();
        console.sendMessage(ChatColor.YELLOW + "===========================================");
        console.sendMessage(ChatColor.GOLD + "HyperNiteMC Lobby Enabled!");

        /*
         * Register Command
         */

        String[] commands = {"fly", "heal", "hidechat", "stacker", "speed", "hideplayer", "Settings", "setlobby", "spawn"};
        CommandExecutor[] cmdexecutor = {
                new FlyExe(this), new HealExe(this), new HideChatExe(this), new StackerExe(this), new SpeedExe(this),
                new HidePlayerExe(this), new SettingsExe(this), new SetLobbyExe(this), new SpawnExe(this)};

        for (int i = 0; i < commands.length; i++) {
            String cmd = commands[i];
            CommandExecutor cmdexe = cmdexecutor[i];
            this.getCommand(cmd).setExecutor(cmdexe);
        }

        /*
         * Register Event Listener
         */

        Listener[] listeners = {
                new OnPlayerChat(), new OnPlayerInteract(), new OnPlayerInteractEntity(), new OnPlayerJoin(), new OnInventoryClick(), new OnPlayerLeave(),
                new BasicListener(), new LobbyJoinItem()};

        for (Listener listen : listeners) {
            getServer().getPluginManager().registerEvents(listen, this);
        }

        /*
         * Load ConfigManager File and Initial
         */

        Functions f = new Functions(this);
        f.addNewFile("Messages.yml");
        f.addNewFile("config.yml");
        f.addNewFile("Lobby.yml");

        ConfigManager var = ConfigManager.getInstance();

        if (var.isMySQL()) console.sendMessage(ChatColor.AQUA + "Using MYSQL as saving Type.");
        else console.sendMessage(ChatColor.AQUA + "Using YAML as saving Type.");

        console.sendMessage(ChatColor.YELLOW + "===========================================");
        new ScheduleRestart();
    }

    public void onDisable() {
        PlayerSettingManager.getInstance().saveAll();
        getLogger().info("HyperNiteMC-Lobby Disabled.");
    }

}


