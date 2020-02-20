package com.hypernite.mc.hnmc.lobby.ericlam.command;


import com.hypernite.mc.hnmc.lobby.caxerx.PlayerSettingManager;
import com.hypernite.mc.hnmc.lobby.ericlam.addon.config.MainConfig;
import com.hypernite.mc.hnmc.lobby.ericlam.addon.config.MessagesConfig;
import com.hypernite.mc.hnmc.lobby.main.HNMCLobby;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class SpeedCommand extends SettingsCommandNode {

    public SpeedCommand() {
        super("speed", "切換速度");
    }

    @Override
    public void executeSettings(Player name, CommandSender sender, MessagesConfig config) {
        UUID puuid = name.getUniqueId();
        PlayerSettingManager psm = PlayerSettingManager.getInstance();
        boolean speed = !psm.getPlayerSetting(puuid).isSpeed();
        int amplifier = HNMCLobby.getConfigManager().getConfigAs(MainConfig.class).speedLevel - 1;
        if (sender != name)
            sender.sendMessage(config.getBeTurnOff("Stacker").replace("<player>", name.getName()));
        name.sendMessage(config.getTurnOff("Speed"));
        if (speed) name.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999 * 20, amplifier));
        else name.removePotionEffect(PotionEffectType.SPEED);
        psm.getPlayerSetting(puuid).setSpeed(speed);
    }
}
