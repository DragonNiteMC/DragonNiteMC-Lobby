package com.hypernite.mc.hnmc.lobby.ericlam.command;

import com.hypernite.mc.hnmc.core.main.HyperNiteMC;
import com.hypernite.mc.hnmc.lobby.caxerx.PlayerSettingManager;
import com.hypernite.mc.hnmc.lobby.ericlam.addon.config.MainConfig;
import com.hypernite.mc.hnmc.lobby.ericlam.addon.config.MessagesConfig;
import com.hypernite.mc.hnmc.lobby.main.HNMCLobby;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class StackerCommand extends SettingsCommandNode {
    public StackerCommand() {
        super("stacker", "切換層層疊");
    }

    @Override
    public void executeSettings(Player name, CommandSender sender, MessagesConfig var) {
        if (HNMCLobby.getConfigManager().getConfigAs("config.yml", MainConfig.class).getStacker().isEnable()) {
            UUID puuid = name.getUniqueId();
            PlayerSettingManager psm = PlayerSettingManager.getInstance();
            boolean nostack = !psm.getPlayerSetting(puuid).isStacker();
            if (sender != name)
                sender.sendMessage(var.getCommandMSG().getStacker().get("be-" + (nostack ? "enable" : "disable")).replace("<player>", name.getName()));
            name.sendMessage(var.getCommandMSG().getStacker().get(nostack ? "enable" : "disable"));
            psm.getPlayerSetting(puuid).setStacker(nostack);
        } else sender.sendMessage(HyperNiteMC.getAPI().getCoreConfig().getPrefix() + ChatColor.RED + "此功能已被管理員禁用。");
    }
}

