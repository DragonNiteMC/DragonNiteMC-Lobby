package com.hypernite.mc.hnmc.lobby.ericlam.command;

import com.hypernite.mc.hnmc.core.main.HyperNiteMC;
import com.hypernite.mc.hnmc.core.managers.ConfigManager;
import com.hypernite.mc.hnmc.lobby.caxerx.PlayerSettingManager;
import com.hypernite.mc.hnmc.lobby.ericlam.addon.LobbyConfig;
import com.hypernite.mc.hnmc.lobby.main.HNMCLobby;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.UUID;

public class StackerCommand extends SettingsCommandNode {
    public StackerCommand() {
        super("stacker", "切換層層疊");
    }

    @Override
    public void executeSettings(Player name, CommandSender sender, ConfigManager var, boolean isMySQL) throws IOException {
        if (HNMCLobby.getLobbyConfig().config.getBoolean("Stacker.Enable")) {
            UUID puuid = name.getUniqueId();
            PlayerSettingManager psm = PlayerSettingManager.getInstance();
            boolean nostack = !psm.getPlayerSetting(puuid).isStacker();
            if (sender != name)
                sender.sendMessage(var.getMessage("Commands.Stacker.be-" + (nostack ? "enable" : "disable")).replace("<player>", name.getName()));
            name.sendMessage(var.getMessage("Commands.Stacker." + (nostack ? "enable" : "disable")));
            psm.getPlayerSetting(puuid).setStacker(nostack);
            if (!isMySQL) LobbyConfig.setYml("Stacker", puuid, nostack);
        } else sender.sendMessage(HyperNiteMC.getAPI().getCoreConfig().getPrefix() + ChatColor.RED + "此功能已被管理員禁用。");
    }
}

