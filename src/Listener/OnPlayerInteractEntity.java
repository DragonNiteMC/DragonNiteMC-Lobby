package Listener;

import main.ericlam.PlayerSettings;
import main.ericlam.StackerExe;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.List;
import java.util.UUID;

import static addon.ericlam.Variable.config;

public class OnPlayerInteractEntity implements Listener {
    @EventHandler
    public void onPlayerStacker(PlayerInteractEntityEvent event) {
        if (config.getBoolean("Stacker.Enable")) {
            Player player = event.getPlayer();
            Entity entity = event.getRightClicked();
            UUID puuid = player.getUniqueId();
            if (entity instanceof Player && player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                List<Entity> rider = player.getPassengers();
                Player rideentity = (Player) entity;
                UUID rideruuid = rideentity.getUniqueId();
                if (StackerExe.stackerenabled.contains(puuid)) {
                    if (rider == null && StackerExe.stackerenabled.contains(rideruuid)) {
                        player.addPassenger(entity);
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(PlayerSettings.returnColoredMessage("Commands.Stacker.stacked").replace("<player>", ((Player) entity).getDisplayName())));
                    } else if (StackerExe.stackerenabled.contains(rideruuid)) {
                        for (Entity ride : rider) {
                            if (rider.size() >= (config.getInt("Stacker.Max-Stack"))) {
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(PlayerSettings.returnColoredMessage("Commands.Stacker.Max")));
                                break;
                            }
                            Player ridep = (Player) ride;
                            if (ridep.getPassengers() == null) ridep.addPassenger(entity);
                        }
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(PlayerSettings.returnColoredMessage("Commands.Stacker.stacked").replace("<player>", ((Player) entity).getDisplayName())));
                    } else {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(PlayerSettings.returnColoredMessage("Commands.Stacker.be-disactive")));
                    }
                } else {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(PlayerSettings.returnColoredMessage("Commands.Stacker.disactive")));
                }
            }
        }
    }
}
