package com.ericlam.mc.hnmclobby.command;

import com.ericlam.mc.hnmclobby.config.MessagesConfig;
import com.ericlam.mc.hnmclobby.manager.PlayerSettingManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class HideChatCommand extends SettingsCommandNode {

    public HideChatCommand(PlayerSettingManager psm) {
        super("hidechat", "切換隱藏聊天", psm);
    }

    @Override
    public void executeSettings(Player name, CommandSender sender, MessagesConfig config) {
        UUID puuid = name.getUniqueId();
        boolean hide = !psm.getPlayerSetting(puuid).isHideChat();
        if (sender != name)
            sender.sendMessage((hide ? config.getBeHide("HideChat") : config.getBeShow("HideChat")).replace("<player>", name.getName()));
        name.sendMessage(hide ? config.getHide("HideChat") : config.getShow("HideChat"));
        psm.getPlayerSetting(puuid).setHideChat(hide);
    }
}

