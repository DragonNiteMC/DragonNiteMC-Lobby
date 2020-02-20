package com.hypernite.mc.hnmc.lobby.ericlam.command;

import com.hypernite.mc.hnmc.lobby.caxerx.PlayerSettingManager;
import com.hypernite.mc.hnmc.lobby.ericlam.addon.config.MessagesConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class HideChatCommand extends SettingsCommandNode {

    public HideChatCommand() {
        super("hidechat", "切換隱藏聊天");
    }

    @Override
    public void executeSettings(Player name, CommandSender sender, MessagesConfig config) {
        UUID puuid = name.getUniqueId();
        PlayerSettingManager psm = PlayerSettingManager.getInstance();
        boolean hide = !psm.getPlayerSetting(puuid).isHideChat();
        if (sender != name)
            sender.sendMessage((hide ? config.getBeHide("HideChat") : config.getBeShow("HideChat")).replace("<player>", name.getName()));
        name.sendMessage(hide ? config.getHide("HideChat") : config.getShow("HideChat"));
        psm.getPlayerSetting(puuid).setHideChat(hide);
    }
}

