package com.hypernite.mc.hnmc.lobby.ericlam.command;

import com.hypernite.mc.hnmc.core.main.HyperNiteMC;
import com.hypernite.mc.hnmc.core.managers.CoreConfig;
import com.hypernite.mc.hnmc.core.misc.commands.CommandNode;
import com.hypernite.mc.hnmc.lobby.ericlam.addon.config.MessagesConfig;
import com.hypernite.mc.hnmc.lobby.main.HNMCLobby;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class SettingsCommandNode extends CommandNode {

    SettingsCommandNode(@Nonnull String command, @Nonnull String description) {
        super(null, command, "setting."+command, description, "[player]");
    }

    @Override
    public boolean executeCommand(@Nonnull CommandSender commandSender, @Nonnull List<String> list) {
        CoreConfig config = HyperNiteMC.getAPI().getCoreConfig();
        Player target;
        boolean terminal = commandSender instanceof ConsoleCommandSender;
        boolean permother = commandSender.hasPermission(this.getPermission()+".other");
        MessagesConfig messagesConfig = HNMCLobby.getConfigManager().getConfigAs(MessagesConfig.class);
        if (list.size() < 1) {
            if (!terminal) {
                Player player = (Player) commandSender;
                executeSettings(player, player, messagesConfig);
            } else {
                commandSender.sendMessage(config.getPrefix() + "Console can only use /"+this.getCommand()+" <player>");
            }
        }else if(permother || terminal){
            target = Bukkit.getServer().getPlayer(list.get(0));
            if (target == null){
                commandSender.sendMessage(config.getPrefix()+config.getNotFoundPlayer());
            }else{
                executeSettings(target, commandSender, messagesConfig);
            }
        }else{
            commandSender.sendMessage(config.getPrefix()+config.getNoPerm());
        }
        return true;
    }

    @Override
    public List<String> executeTabCompletion(@Nonnull CommandSender commandSender, @Nonnull List<String> list) {
        return null;
    }

    public abstract void executeSettings(Player name, CommandSender sender, MessagesConfig var);

}
