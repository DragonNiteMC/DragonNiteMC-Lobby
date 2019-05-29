package com.hypernite.mc.hnmc.lobby.ericlam.command;


import com.hypernite.mc.hnmc.core.managers.ConfigManager;
import com.hypernite.mc.hnmc.lobby.caxerx.PlayerSettingManager;
import com.hypernite.mc.hnmc.lobby.ericlam.addon.LobbyConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.UUID;


public class FlyCommand extends SettingsCommandNode {


    public FlyCommand() {
        super("fly","切換飛行");
    }

    @Override
    public void executeSettings(Player name, CommandSender sender, ConfigManager var, boolean isMySQL) throws IOException {
        UUID puuid = name.getUniqueId();
        PlayerSettingManager psm = PlayerSettingManager.getInstance();
        boolean fly = !psm.getPlayerSetting(puuid).isFly();
        name.setAllowFlight(fly);
        name.setFlying(fly);
        if (!isMySQL) LobbyConfig.setYml("Flight", puuid, fly);
        psm.getPlayerSetting(puuid).setFly(fly);
        name.sendMessage(var.getMessage("Commands.Fly.Turn-" + (fly ? "On" : "Off")));
        if (name != sender) {
            sender.sendMessage(var.getMessage("Commands.Fly.Be-Turn-" + (fly ? "On" : "Off")).replace("<player>", name.getName()));
        }
    }
}

