package main.ericlam;
import addon.ericlam.MySQL;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;
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
        String[] commands = {"fly", "heal", "ping", "hidechat", "stacker"};
        CommandExecutor[] cmdexecutor = {new FlyExe(this), new HealExe(this), new PingExe(this), new HideChatExe(this), new StackerExe(this)};
        for (int i = 0; i < commands.length; i++) {
            String cmd = commands[i];
            CommandExecutor cmdexe = cmdexecutor[i];
            this.getCommand(cmd).setExecutor(cmdexe);
        }
        getServer().getPluginManager().registerEvents(new Listen(), this);
        addNewFile("Messages.yml");
        addNewFile("config.yml");
        if (yaml) {
            console.sendMessage(ChatColor.AQUA + "Using YAML as saving Type.");
        }
        if(MYsql) {
            MySQL mysql = MySQL.getinstance();
            console.sendMessage(ChatColor.AQUA + "Using MYSQL as saving Type.");
            try {
                mysql.openConnection();
                Statement statment = mysql.connection.createStatement();
                statment.execute("CREATE TABLE IF NOT EXISTS `"+ table+ "` (`PlayerUUID` VARCHAR(40) NOT NULL PRIMARY KEY, `Fly` bit NOT NULL, `HideChat` bit NOT NULL, `Stacker` bit NOT NULL)");
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        console.sendMessage(ChatColor.YELLOW + "===========================================");
    }

    public void onDisable() {
        getLogger().info("PlayerSettings Disabled.");
    }

    public static String returnColoredMessage(String messagePath) {
        String path = messagefile.getString(messagePath);
        return ChatColor.translateAlternateColorCodes('&', path);
    }

    public static void addNewFile(String pathname) {
        File filename = new File(plugin.getDataFolder(), pathname);
        if (!filename.exists()) plugin.saveResource(pathname, true);
        YamlConfiguration.loadConfiguration(filename);
    }

    public static void renametoUUID(UUID puuid, Player player) throws IOException {
        File folder = new File(plugin.getDataFolder(), "PlayerData" + File.separator);
        File filename = new File(plugin.getDataFolder(),  "PlayerData"+File.separator + puuid.toString() + ".yml");
        if (!folder.exists()) folder.mkdir();
        filename.createNewFile();
        YamlConfiguration.loadConfiguration(filename);
    }
}

