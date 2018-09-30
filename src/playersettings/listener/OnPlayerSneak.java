package playersettings.listener;

import addon.ericlam.Variable;
import functions.hypernite.mc.Functions;
import main.ericlam.HyperNiteMC;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import static addon.ericlam.Variable.config;
import static addon.ericlam.Variable.messagefile;

public class OnPlayerSneak implements Listener {
    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        Functions fs = new Functions(HyperNiteMC.plugin);
        Variable var = new Variable();
        if (config.getBoolean("SneakShowLocation.Enable")) {
            Player player = event.getPlayer();
            Location loc = player.getLocation();
            boolean perm = player.hasPermission("settings.sneak.showlocation");
            int x = (int) loc.getX();
            int y = (int) loc.getY();
            int z = (int) loc.getZ();
            String xyz = x + " " + y + " " + z;
            if (player.isSneaking() && player.getWalkSpeed() < 0.25 && player.isOnGround() && perm) {
                player.sendTitle(fs.returnColoredMessage(messagefile,"Functions.Sneak.show-location-title"), xyz, 5, 30, 5);
            }
        }
    }
}
