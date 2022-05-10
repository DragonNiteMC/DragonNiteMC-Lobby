package com.ericlam.mc.dnmclobby.command;

import com.ericlam.mc.dnmclobby.config.MainConfig;
import com.ericlam.mc.dnmclobby.config.MessagesConfig;
import com.ericlam.mc.dnmclobby.main.DNMCLobby;
import com.ericlam.mc.dnmclobby.manager.PlayerSettingManager;
import com.dragonite.mc.dnmc.core.main.DragoniteMC;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class StackerCommand extends SettingsCommandNode {
    public StackerCommand(PlayerSettingManager psm) {
        super("stacker", "切換層層疊", psm);
    }

    @Override
    public void executeSettings(Player name, CommandSender sender, MessagesConfig var) {
        if (DNMCLobby.getConfigManager().getConfigAs(MainConfig.class).stacker.enable) {
            UUID puuid = name.getUniqueId();
            boolean nostack = !psm.getPlayerSetting(puuid).isStacker();
            if (sender != name)
                sender.sendMessage(var.getStacker("be-" + (nostack ? "enable" : "disable")).replace("<player>", name.getName()));
            name.sendMessage(var.getStacker(nostack ? "enable" : "disable"));
            psm.getPlayerSetting(puuid).setStacker(nostack);
        } else sender.sendMessage(DragoniteMC.getAPI().getCoreConfig().getPrefix() + ChatColor.RED + "此功能已被管理員禁用。");
    }
}

