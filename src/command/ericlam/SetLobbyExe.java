package command.ericlam;

import addon.ericlam.Variable;
import main.ericlam.HyperNiteMC;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class SetLobbyExe implements CommandExecutor {
    private final HyperNiteMC plugin;
    public SetLobbyExe(HyperNiteMC plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player){
            Variable var = new Variable();
            if (commandSender.hasPermission("hypernite.setlobby")) {
                Player player = (Player) commandSender;
                Location set = player.getLocation();
                Double Y = set.getY();
                Double X = set.getX();
                Double Z = set.getZ();
                World setworld = set.getWorld();
                Double Yaw = (double) set.getYaw();
                Double Pitch = (double) set.getPitch();
                Variable.lobbyfile.set("spawntp.y", Y);
                Variable.lobbyfile.set("spawntp.x", X);
                Variable.lobbyfile.set("spawntp.z", Z);
                Variable.lobbyfile.set("spawntp.yaw", Yaw);
                Variable.lobbyfile.set("spawntp.pitch", Pitch);
                Variable.lobbyfile.set("spawntp.world", setworld.getName());
                try {
                    File lobbyfile = new File(plugin.getDataFolder(), "Lobby.yml");
                    Variable.lobbyfile.save(lobbyfile);
                    YamlConfiguration.loadConfiguration(lobbyfile);
                } catch (IOException e) {
                    e.printStackTrace();
                    player.sendMessage(ChatColor.RED + "出現問題, 我們無法保存你的記錄!");
                }
                player.sendMessage(var.prefix() + ChatColor.GREEN + "重生點保存成功!");
            }else commandSender.sendMessage(var.prefix() + var.noperm());
        } else {commandSender.sendMessage("you are not player!");}
        return true;
    }
}
