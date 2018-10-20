package main.ericlam;

import addon.ericlam.Variable;
import com.caxerx.mc.PlayerSettingManager;
import com.hypernite.functions.Functions;
import command.ericlam.*;
import lobby.listener.BasicListener;
import lobby.listener.LobbyJoinItem;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import playersettings.listener.*;

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
         * Load Config File and Initial
         */

        Functions f = new Functions(this);
        f.addNewFile("Messages.yml");
        f.addNewFile("config.yml");
        f.addNewFile("Lobby.yml");

        Variable var = new Variable();
        PlayerSettingManager psm = PlayerSettingManager.getInstance();

        if (var.isMySQL()) console.sendMessage(ChatColor.AQUA + "Using MYSQL as saving Type.");
        else console.sendMessage(ChatColor.AQUA + "Using YAML as saving Type.");

        console.sendMessage(ChatColor.YELLOW + "===========================================");
    }

    public void onDisable() {
        PlayerSettingManager.getInstance().saveAll();
        getLogger().info("HyperNiteMC-Lobby Disabled.");
    }

}


