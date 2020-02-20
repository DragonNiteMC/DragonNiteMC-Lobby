package com.hypernite.mc.hnmc.lobby.ericlam.command;

import com.hypernite.mc.hnmc.lobby.ericlam.addon.config.MessagesConfig;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class HealCommand extends SettingsCommandNode {

    public HealCommand() {
        super("heal", "治愈血量");
    }

    @Override
    public void executeSettings(Player name, CommandSender sender, MessagesConfig var) {
        int hp = (int) name.getHealth();
        int hg = name.getFoodLevel();
        double maxHealth = Optional.ofNullable(name.getAttribute(Attribute.GENERIC_MAX_HEALTH)).map(AttributeInstance::getBaseValue).orElse(20.0);
        if (hp < maxHealth || hg < 20){
            name.setHealth(maxHealth);
            name.setFoodLevel(20);
            name.sendMessage(var.get("Heal.active"));
            if (name != sender){
                sender.sendMessage(var.get("Heal.Be-active").replace("<player>", name.getName()));
            }
        } else {
            sender.sendMessage(var.get("Heal." + (name == sender ? "no-need" : "Be-no-need")).replace("<player>", name.getName()));
        }
    }
}

