package main.ericlam;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static addon.ericlam.Variable.noperm;
import static addon.ericlam.Variable.prefix;

public class HealExe implements CommandExecutor{
    private final PlayerSettings plugin;
    HealExe(PlayerSettings plugin){
        this.plugin = plugin;
    }
        @Override
        public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
                Player target;
                boolean terminal = commandSender instanceof ConsoleCommandSender;
                boolean perm = commandSender.hasPermission("ericlam.heal");
                boolean permother = commandSender.hasPermission("ericlam.heal.other");
                        if (strings.length <= 0 && perm) {
                            if (!terminal) {
                                Player player = (Player) commandSender;
                                healExector(player, player);
                            } else {commandSender.sendMessage(ChatColor.RED + "Console can only use /heal <player>");}
                        }else if(permother || terminal){
                            target = Bukkit.getServer().getPlayer(strings[0]);
                            if (target == null){
                                commandSender.sendMessage(prefix + PlayerSettings.returnColoredMessage("General.Player-Not-Found"));
                            }else{ healExector(target, commandSender);}
                        }else{commandSender.sendMessage(prefix + noperm);}
                    return true;
    }
    public static void healExector(Player name, CommandSender sender){
        int hp = (int) name.getHealth();
        int hg = name.getFoodLevel();
        if (hp < 20 || hg < 20){
            name.setHealth(20);
            name.setFoodLevel(20);
            name.sendMessage(prefix + PlayerSettings.returnColoredMessage("Commands.Heal.active"));
            if (name != sender){
                sender.sendMessage(prefix + PlayerSettings.returnColoredMessage("Commands.Heal.Be-active").replace("<player>", name.getDisplayName()));
            }
        } else {
            sender.sendMessage(prefix + PlayerSettings.returnColoredMessage(( name == sender ? "Commands.Heal.no-need" : "Commands.Heal.Be-no-need")).replace("<player>",name.getDisplayName()));
        }
    }
}

