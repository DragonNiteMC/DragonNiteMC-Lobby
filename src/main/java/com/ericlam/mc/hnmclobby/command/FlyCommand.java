package com.ericlam.mc.hnmclobby.command;


import com.ericlam.mc.hnmclobby.config.MessagesConfig;
import com.ericlam.mc.hnmclobby.manager.PlayerSettingManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;


public class FlyCommand extends SettingsCommandNode {


    public FlyCommand(PlayerSettingManager psm) {
        super("fly", "切換飛行", psm);
    }

    @Override
    public void executeSettings(Player name, CommandSender sender, MessagesConfig var) {
        UUID puuid = name.getUniqueId();
        boolean fly = !psm.getPlayerSetting(puuid).isFly();
        name.setAllowFlight(fly);
        name.setFlying(fly);
        psm.getPlayerSetting(puuid).setFly(fly);
        name.sendMessage(fly ? var.getTurnOn("Fly") : var.getTurnOff("Fly"));
        if (name != sender) {
            sender.sendMessage((fly ? var.getBeTurnOn("Fly") : var.getBeTurnOff("Fly")).replace("<player>", name.getName()));
        }
    }
}

