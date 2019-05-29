package com.hypernite.mc.hnmc.lobby.ericlam.command;


import com.hypernite.mc.hnmc.core.managers.ConfigManager;
import com.hypernite.mc.hnmc.lobby.caxerx.PlayerSettingManager;
import com.hypernite.mc.hnmc.lobby.ericlam.addon.LobbyConfig;
import com.hypernite.mc.hnmc.lobby.main.HNMCLobby;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;
import java.util.UUID;

public class SpeedCommand extends SettingsCommandNode {

    public SpeedCommand() {
        super("speed", "切換速度");
    }

    @Override
    public void executeSettings(Player name, CommandSender sender, ConfigManager var, boolean isMySQL) throws IOException {
        UUID puuid = name.getUniqueId();
        PlayerSettingManager psm = PlayerSettingManager.getInstance();
        boolean speed = !psm.getPlayerSetting(puuid).isSpeed();
        int amplifier = HNMCLobby.getLobbyConfig().config.getInt("Speed.Level") - 1;
        if (sender != name) sender.sendMessage(var.getMessage("Commands.Speed.Be-Turn-" + (speed ? "On":"Off")).replace("<player>",name.getName()));
        name.sendMessage(var.getMessage("Commands.Speed.Turn-" + (speed ? "On":"Off")));
        if (speed) name.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999*20, amplifier));
        else name.removePotionEffect(PotionEffectType.SPEED);
        psm.getPlayerSetting(puuid).setSpeed(speed);
        if (!isMySQL) LobbyConfig.setYml("Speed", puuid, speed);
    }
}
