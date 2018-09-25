package main.ericlam;


import net.minecraft.server.v1_13_R2.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import static addon.ericlam.Variable.noperm;
import static addon.ericlam.Variable.prefix;

public class PingExe implements CommandExecutor {

    private final PlayerSettings plugin;

    PingExe(PlayerSettings plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        boolean terminal = commandSender instanceof ConsoleCommandSender;
        boolean perm = commandSender.hasPermission("ericlam.ping");
        boolean permother = commandSender.hasPermission("ericlam.ping.other");
        if (strings.length <= 0 && perm) {
            if (!terminal) {
                Player player = (Player) commandSender;
                player.sendMessage(prefix + PlayerSettings.returnColoredMessage("Commands.Ping.self").replace("<ping>", "" + getPing(player)));
            }
             else commandSender.sendMessage(ChatColor.RED + "Console can only use /ping <player>");
        } else if (permother || terminal) {
            Player target = Bukkit.getServer().getPlayer(strings[0]);
            if (target == null) {
                commandSender.sendMessage(prefix + PlayerSettings.returnColoredMessage("General.Player-Not-Found"));
            } else {
                commandSender.sendMessage(prefix + PlayerSettings.returnColoredMessage("Commands.Ping.other").replace("<ping>", "" + getPing(target)).replace("<player>", target.getDisplayName()));
            }
        } else {
            commandSender.sendMessage(prefix + noperm);
        }
        return true;
    }

    public int getPing(Player p) {
        CraftPlayer cp = (CraftPlayer) p;
        EntityPlayer ep = cp.getHandle();
        return ep.ping;
    }
}

