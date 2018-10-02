package playersettings.listener;

import addon.ericlam.Variable;
import com.caxerx.mc.PlayerSettingManager;
import main.ericlam.HyperNiteMC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.sql.SQLException;
import java.util.UUID;

public class OnPlayerLeave implements Listener {
    private BukkitScheduler scheduler = Bukkit.getScheduler();
    @EventHandler
    public void PlayerLeaveMySQL(PlayerQuitEvent event) throws SQLException {
        Player player = event.getPlayer();
        UUID puuid = player.getUniqueId();
        if (new Variable().isMySQL())
        Bukkit.getScheduler().runTaskAsynchronously(HyperNiteMC.plugin, () ->{
            try {
                PlayerSettingManager.getInstance().savePlayerSetting(puuid, 0 );
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        }
    }


