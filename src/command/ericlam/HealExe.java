package command.ericlam;

import addon.ericlam.Variable;
import main.ericlam.HyperNiteMC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static addon.ericlam.Variable.messagefile;

public class HealExe implements CommandExecutor{
    private final HyperNiteMC plugin;
    private Variable var = new Variable();
    public HealExe(HyperNiteMC plugin){
        this.plugin = plugin;
    }
        @Override
        public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
                Player target;
                boolean terminal = commandSender instanceof ConsoleCommandSender;
                boolean perm = commandSender.hasPermission("settings.heal");
                boolean permother = commandSender.hasPermission("settings.heal.other");
                        if (strings.length <= 0 && perm) {
                            if (!terminal) {
                                Player player = (Player) commandSender;
                                healExector(player, player);
                            } else {commandSender.sendMessage(ChatColor.RED + "Console can only use /heal <player>");}
                        }else if(perm && permother || terminal){
                            target = Bukkit.getServer().getPlayer(strings[0]);
                            if (target == null){
                                commandSender.sendMessage(var.prefix() + var.getFs().returnColoredMessage(messagefile,"General.Player-Not-Found"));
                            }else{ healExector(target, commandSender);}
                        }else{commandSender.sendMessage(var.prefix() + var.noperm());}
                    return true;
    }
    public void healExector(Player name, CommandSender sender){
        int hp = (int) name.getHealth();
        int hg = name.getFoodLevel();
        if (hp < 20 || hg < 20){
            name.setHealth(20);
            name.setFoodLevel(20);
            name.sendMessage(var.prefix() + var.getFs().returnColoredMessage(messagefile,"Commands.Heal.active"));
            if (name != sender){
                sender.sendMessage(var.prefix() + var.getFs().returnColoredMessage(messagefile,"Commands.Heal.Be-active").replace("<player>", name.getName()));
            }
        } else {
            sender.sendMessage(var.prefix() + var.getFs().returnColoredMessage(messagefile,( name == sender ? "Commands.Heal.no-need" : "Commands.Heal.Be-no-need")).replace("<player>",name.getName()));
        }
    }
}

