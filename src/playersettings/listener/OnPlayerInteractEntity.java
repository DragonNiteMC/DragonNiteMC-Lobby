package playersettings.listener;

import addon.ericlam.Variable;
import com.caxerx.mc.PlayerSettingManager;
import functions.hypernite.mc.Functions;
import main.ericlam.HyperNiteMC;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static addon.ericlam.Variable.config;
import static addon.ericlam.Variable.messagefile;

public class OnPlayerInteractEntity implements Listener {
    private Variable var = new Variable();
    @EventHandler
    public void onPlayerStacker(PlayerInteractEntityEvent event) throws SQLException {
        if (event.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        if (config.getBoolean("Stacker.Enable")) {
            Player player = event.getPlayer();
            Entity entity = event.getRightClicked();
            UUID puuid = player.getUniqueId();
            Functions fs = new Functions(HyperNiteMC.plugin);
            PlayerSettingManager psm = PlayerSettingManager.getInstance();
            if (entity instanceof Player && player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                List<Entity> rider = player.getPassengers();
                Player rideentity = (Player) entity;
                UUID rideruuid = rideentity.getUniqueId();
                if (psm.getPlayerSetting(puuid).isStacker()) {
                    if (rider.isEmpty() && psm.getPlayerSetting(rideruuid).isStacker()) {
                        player.addPassenger(entity);
                        player.sendMessage(var.prefix() + fs.returnColoredMessage(messagefile,"Commands.Stacker.stacked").replace("<player>", ((Player) entity).getDisplayName()));
                    } else if (psm.getPlayerSetting(rideruuid).isStacker()) {
                        for (Entity ride : rider) {
                            if (player.getPassengers().contains(ride)) return;
                            if (rider.size() >= (config.getInt("Stacker.Max-Stack")) ) {
                                player.sendMessage(var.prefix() + fs.returnColoredMessage(messagefile,"Commands.Stacker.Max"));
                                return;
                            }
                            Player ridep = (Player) ride;
                            if (ridep.getPassengers().isEmpty()) ridep.addPassenger(entity); HyperNiteMC.plugin.getLogger().info("condition 1.5 reached");
                        }
                        player.sendMessage(var.prefix() +  fs.returnColoredMessage(messagefile,"Commands.Stacker.stacked").replace("<player>", ((Player) entity).getDisplayName()));
                    } else {
                        player.sendMessage(var.prefix() + fs.returnColoredMessage(messagefile,"Commands.Stacker.be-disactive"));
                    }
                } else {
                    player.sendMessage(var.prefix() + fs.returnColoredMessage(messagefile,"Commands.Stacker.disactive"));
                }
            }
        }
    }
}
