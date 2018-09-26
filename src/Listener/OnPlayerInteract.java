package Listener;

import main.ericlam.PlayerSettings;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.List;

import static addon.ericlam.Variable.*;

public class OnPlayerInteract implements Listener {
    @EventHandler
    public  void onPlayerPush(PlayerInteractEvent event){
        if (event.getAction().equals(Action.LEFT_CLICK_AIR) && event.getPlayer().getPassengers().equals(EntityType.PLAYER)){
            Player thrower = event.getPlayer();
            List<Entity> toThrow = thrower.getPassengers();
            thrower.eject();
            for (Entity push : toThrow){
                Player toPush = (Player) push;
                toPush.eject();
                toPush.setVelocity(toPush.getLocation().getDirection().multiply(config.getInt("Stacker.Throw-Power")));
                toPush.setVelocity(new Vector(toPush.getVelocity().getX(), config.getDouble("Stacker.Throw-Y"), toPush.getVelocity().getZ()));
                thrower.sendMessage(prefix + returnColoredMessage("Commands.Stacker.pushed" + (toThrow.size() > 1 ? "-all" : "")).replace("<player>", toPush.getDisplayName()));
            }
        }
    }
}
