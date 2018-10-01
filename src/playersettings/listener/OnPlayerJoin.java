package playersettings.listener;

import addon.ericlam.Variable;
import com.caxerx.mc.PlayerSettingManager;
import functions.hypernite.mc.Functions;
import main.ericlam.HyperNiteMC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.UUID;

import static addon.ericlam.Variable.config;
import static addon.ericlam.Variable.messagefile;

public class OnPlayerJoin implements Listener {
    private Variable var = new Variable();
    private Functions fs = new Functions(HyperNiteMC.plugin);
    private PlayerSettingManager psm = PlayerSettingManager.getInstance();
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException, SQLException {
        Player player = event.getPlayer();
        UUID puuid = player.getUniqueId();
        int amplifier = var.config.getInt("Speed.Level") - 1;
        if (var.isMySQL()){
            Bukkit.getScheduler().runTaskAsynchronously(HyperNiteMC.plugin, () -> {
                try {
                    psm.getPlayerSetting(puuid);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            });
            boolean speed = psm.getPlayerSetting(puuid).isSpeed() && player.hasPermission("settings.speed");
            boolean fly = psm.getPlayerSetting(puuid).isFly() && player.hasPermission("settings.fly");
            player.setAllowFlight(fly);
            player.setFlying(fly);
            if (speed) player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, amplifier));
            else player.removePotionEffect(PotionEffectType.SPEED);
        }
        if (var.isYaml()) {
            //Flight Stats
            psm.getPlayerSettingFromYaml(puuid);
            fs.renametoUUID(puuid, player);
            boolean speed = psm.getPlayerSettingFromYaml(puuid).isSpeed() && player.hasPermission("settings.speed");
            boolean fly = psm.getPlayerSettingFromYaml(puuid).isFly() && player.hasPermission("settings.fly");
            player.setAllowFlight(fly);
            player.setFlying(fly);
            if (speed) player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, amplifier));
            else player.removePotionEffect(PotionEffectType.SPEED);
        }
        if (config.getBoolean("Join-Show-UUID.Enable"))
            player.sendMessage(var.prefix() + fs.returnColoredMessage(messagefile,"Functions.ShowUUID.JoinMessage").replace("<UUID>", puuid.toString()));
    }
    @EventHandler
    public void HidePlayerOnJoin(PlayerJoinEvent e) throws SQLException {
        Player target = e.getPlayer();
        Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();
        for (Player player : players){
            if (player == target) continue;
            if (psm.getPlayerSetting(player.getUniqueId()).isHidePlayer()) player.hidePlayer(HyperNiteMC.plugin, target);
        }
    }
}
