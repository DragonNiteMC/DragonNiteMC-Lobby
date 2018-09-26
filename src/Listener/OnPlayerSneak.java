package Listener;

import main.ericlam.PlayerSettings;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import static addon.ericlam.Variable.config;

public class OnPlayerSneak implements Listener {
    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        if (config.getBoolean("SneakShowLocation.Enable")) {
            Player player = event.getPlayer();
            Location loc = player.getLocation();
            boolean perm = player.hasPermission("ericlam.sneak.showlocation");
            int x = (int) loc.getX();
            int y = (int) loc.getY();
            int z = (int) loc.getZ();
            String xyz = x + " " + y + " " + z;
            if (player.isSneaking() && player.getWalkSpeed() < 0.25 && player.isOnGround() && perm) {
                player.sendTitle(PlayerSettings.returnColoredMessage("Functions.Sneak.show-location-title"), xyz, 5, 30, 5);
            }
        }
    }
}
