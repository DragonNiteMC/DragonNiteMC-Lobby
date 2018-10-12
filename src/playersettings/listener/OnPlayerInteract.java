package playersettings.listener;

import addon.ericlam.Variable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

import static addon.ericlam.Variable.config;
import static addon.ericlam.Variable.messagefile;

public class OnPlayerInteract implements Listener {
    private Variable var = new Variable();
    @EventHandler
    public  void onPlayerPush(PlayerInteractEvent event){
        if (event.getAction().equals(Action.LEFT_CLICK_AIR) && !event.getPlayer().getPassengers().isEmpty()){
            Player thrower = event.getPlayer();
            Set<Player> toThrow = new HashSet<>();
            Player top = thrower;
            while (top.getPassengers().size() > 0){
                if (top.getPassengers().size() != 1){
                    return;
                }
                Entity topentity = top.getPassengers().get(0);
                if (!(topentity instanceof Player)){
                    return;
                }
                top = (Player) topentity;
                toThrow.add(top);
            }
            thrower.eject();
            for (Player toPush : toThrow){
                toPush.eject();
                toPush.setVelocity(thrower.getLocation().getDirection().multiply(config.getInt("Stacker.Throw-Power")));
                toPush.setVelocity(new Vector(toPush.getVelocity().getX(), config.getDouble("Stacker.Throw-Y"), toPush.getVelocity().getZ()));
                if (toThrow.size() == 1) thrower.sendMessage(var.prefix() + var.getFs().returnColoredMessage(messagefile, "Commands.Stacker.pushed").replace("<player>", toPush.getName()));
            }
            if (toThrow.size() > 1)thrower.sendMessage(var.prefix() + var.getFs().returnColoredMessage(messagefile,"Commands.Stacker.pushed-all"));
        }
    }
}
