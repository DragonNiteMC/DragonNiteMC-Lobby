package command.ericlam;

import addon.ericlam.Variable;
import com.caxerx.mc.PlayerSettingManager;
import main.ericlam.PlayerSettings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

import static addon.ericlam.Variable.config;
import static addon.ericlam.Variable.messagefile;

public class StackerExe implements CommandExecutor{
    private final PlayerSettings plugin;
    private Variable var = Variable.getInstance();
    public StackerExe(PlayerSettings plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
            Player target;
            boolean terminal = commandSender instanceof ConsoleCommandSender;
            boolean perm = commandSender.hasPermission("settings.stacker");
            boolean permother = commandSender.hasPermission("settings.stacker.other");
            if (strings.length <= 0 && perm) {
                if (!terminal) {
                    Player player = (Player) commandSender;
                    try {
                        StackerOn(player, player);
                    } catch (IOException | SQLException e) {
                        e.printStackTrace();
                    }
                } else commandSender.sendMessage(ChatColor.RED + "Console can only use /stacker <player>");
            } else if (permother || terminal) {
                target = Bukkit.getPlayer(strings[0]);
                if (target == null)
                    commandSender.sendMessage(var.prefix() + var.getFs().returnColoredMessage(messagefile,"General.Player-Not-Found"));
                else {
                    try {
                        StackerOn(target, commandSender);
                    } catch (IOException | SQLException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                commandSender.sendMessage(var.prefix() + var.noperm());
            }
            return true;
    }
    public void StackerOn(Player name, CommandSender sender) throws IOException, SQLException {
        if (config.getBoolean("Stacker.Enable")) {
            Player player = name.getPlayer();
            UUID puuid = player.getUniqueId();
            PlayerSettingManager psm = PlayerSettingManager.getInstance();
            boolean nostack = !psm.getPlayerSetting(puuid).isStacker();
            if (sender != name)
                sender.sendMessage(var.prefix() + var.getFs().returnColoredMessage(messagefile,"Commands.Stacker.be-" + (nostack ? "enable" : "disable")).replace("<player>", name.getDisplayName()));
            name.sendMessage(var.prefix() + var.getFs().returnColoredMessage(messagefile,"Commands.Stacker." + (nostack ? "enable" : "disable")));
            psm.getPlayerSetting(puuid).setStacker(nostack);
            if (Variable.yaml) {
                Variable.setYml("Stacker", puuid, nostack);
            }
        } else sender.sendMessage(var.prefix() + ChatColor.RED + "此功能已被管理員禁用。");
    }
}

