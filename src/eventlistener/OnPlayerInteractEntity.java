package eventlistener;

import addon.ericlam.Variable;
import com.caxerx.mc.PlayerSettingManager;
import functions.hypernite.mc.Functions;
import main.ericlam.PlayerSettings;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static addon.ericlam.Variable.config;
import static addon.ericlam.Variable.messagefile;

public class OnPlayerInteractEntity implements Listener {
    private Variable var = Variable.getInstance();
    @EventHandler
    public void onPlayerStacker(PlayerInteractEntityEvent event) throws SQLException {
        if (config.getBoolean("Stacker.Enable")) {
            Player player = event.getPlayer();
            Entity entity = event.getRightClicked();
            UUID puuid = player.getUniqueId();
            Functions fs = new Functions(PlayerSettings.plugin);
            PlayerSettingManager psm = PlayerSettingManager.getInstance();
            if (entity instanceof Player && player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                List<Entity> rider = player.getPassengers();
                Player rideentity = (Player) entity;
                UUID rideruuid = rideentity.getUniqueId();
                if (psm.getPlayerSetting(puuid).isStacker()) {
                    if (rider == null && psm.getPlayerSetting(rideruuid).isStacker()) {
                        player.addPassenger(entity);
                        fs.sendActionBarMessage(player, fs.returnColoredMessage(messagefile,"Commands.Stacker.stacked").replace("<player>", ((Player) entity).getDisplayName()));
                    } else if (psm.getPlayerSetting(rideruuid).isStacker()) {
                        for (Entity ride : rider) {
                            if (rider.size() >= (config.getInt("Stacker.Max-Stack"))) {
                                fs.sendActionBarMessage(player, fs.returnColoredMessage(messagefile,"Commands.Stacker.Max"));
                                break;
                            }
                            Player ridep = (Player) ride;
                            if (ridep.getPassengers() == null) ridep.addPassenger(entity);
                        }
                        fs.sendActionBarMessage(player, fs.returnColoredMessage(messagefile,"Commands.Stacker.stacked").replace("<player>", ((Player) entity).getDisplayName()));
                    } else {
                        fs.sendActionBarMessage(player, fs.returnColoredMessage(messagefile,"Commands.Stacker.be-disactive"));
                    }
                } else {
                    fs.sendActionBarMessage(player, fs.returnColoredMessage(messagefile,"Commands.Stacker.disactive"));
                }
            }
        }
    }
}
