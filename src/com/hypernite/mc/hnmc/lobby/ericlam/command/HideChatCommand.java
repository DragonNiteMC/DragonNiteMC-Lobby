package com.hypernite.mc.hnmc.lobby.ericlam.command;

import com.hypernite.mc.hnmc.core.managers.ConfigManager;
import com.hypernite.mc.hnmc.lobby.caxerx.PlayerSettingManager;
import com.hypernite.mc.hnmc.lobby.ericlam.addon.LobbyConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.UUID;

public class HideChatCommand extends SettingsCommandNode {

    public HideChatCommand() {
        super("hidechat", "切換隱藏聊天");
    }

    @Override
    public void executeSettings(Player name, CommandSender sender, ConfigManager var, boolean isMySQL) throws IOException {
        UUID puuid = name.getUniqueId();
        PlayerSettingManager psm = PlayerSettingManager.getInstance();
        boolean hide = !psm.getPlayerSetting(puuid).isHideChat();
        if (sender != name)  sender.sendMessage(var.getMessage("Commands.HideChat.be-" + (hide ? "hide" : "show")).replace("<player>", name.getName()));
        name.sendMessage(var.getMessage("Commands.HideChat." + (hide ? "hide" : "show")));
        psm.getPlayerSetting(puuid).setHideChat(hide);
        if (!isMySQL) LobbyConfig.setYml("HideChat", puuid, hide);
    }
}

