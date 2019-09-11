package com.hypernite.mc.hnmc.lobby.ericlam.command;


import com.hypernite.mc.hnmc.lobby.caxerx.PlayerSettingManager;
import com.hypernite.mc.hnmc.lobby.ericlam.addon.config.MessagesConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;


public class FlyCommand extends SettingsCommandNode {


    public FlyCommand() {
        super("fly","切換飛行");
    }

    @Override
    public void executeSettings(Player name, CommandSender sender, MessagesConfig var) {
        UUID puuid = name.getUniqueId();
        PlayerSettingManager psm = PlayerSettingManager.getInstance();
        boolean fly = !psm.getPlayerSetting(puuid).isFly();
        name.setAllowFlight(fly);
        name.setFlying(fly);
        psm.getPlayerSetting(puuid).setFly(fly);
        name.sendMessage(var.getCommandMSG().getFly().get("Turn-" + (fly ? "On" : "Off")));
        if (name != sender) {
            sender.sendMessage(var.getCommandMSG().getFly().get("Be-Turn-" + (fly ? "On" : "Off")).replace("<player>", name.getName()));
        }
    }
}

