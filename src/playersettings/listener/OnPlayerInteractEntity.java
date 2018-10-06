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
    private PlayerSettingManager psm = PlayerSettingManager.getInstance();
    private Functions fs = new Functions(HyperNiteMC.plugin);

    @EventHandler
    public void onPlayerStacker(PlayerInteractEntityEvent event) throws SQLException {
        if (event.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        if (!config.getBoolean("Stacker.Enable")) {
            return;
        }

        Player player = event.getPlayer();
        UUID puuid = player.getUniqueId();
        Entity entity = event.getRightClicked();

        if (!(entity instanceof Player) || !player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
            return;
        }

        Player target = (Player) entity;

        if (psm.getPlayerSetting(puuid).isStacker() && psm.getPlayerSetting(target.getUniqueId()).isStacker()) {
            player.sendMessage(var.prefix() + fs.returnColoredMessage(messagefile, "Commands.Stacker.disactive"));
            return;
        }

        int maxStack = config.getInt("Stacker.Max-Stack");
        int stack = 0;
        Player top = player;

        while (top.getPassengers().size() > 0) {
            if (top.getPassengers().size() != 1) {
                return;
            }
            Entity topEntity = top.getPassengers().get(0);
            if (!(topEntity instanceof Player)) {
                return;
            }
            top = (Player) topEntity;
            stack++;
            if (stack >= maxStack) {
                player.sendMessage(var.prefix() + fs.returnColoredMessage(messagefile, "Commands.Stacker.Max"));
                return;
            }
        }

        top.addPassenger(target);
        player.sendMessage(var.prefix() + fs.returnColoredMessage(messagefile, "Commands.Stacker.stacked").replace("<player>", ((Player) entity).getDisplayName()));
    }
}

