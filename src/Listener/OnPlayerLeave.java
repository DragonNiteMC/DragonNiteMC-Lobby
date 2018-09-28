package listener;

import addon.ericlam.Variable;
import command.ericlam.HideChatExe;
import command.ericlam.HidePlayerExe;
import command.ericlam.StackerExe;
import mysql.hypernite.mc.SQLDataSourceManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import static main.ericlam.PlayerSettings.plugin;

public class OnPlayerLeave implements Listener {
    private BukkitScheduler scheduler = Bukkit.getScheduler();
    @EventHandler
    public void PlayerLeaveMySQL(PlayerQuitEvent event) throws SQLException {
        Player player = event.getPlayer();
        UUID puuid = player.getUniqueId();
        boolean Fly = player.getAllowFlight();
        boolean Speed = player.hasPotionEffect(PotionEffectType.SPEED);
        boolean HidePlayer = HidePlayerExe.vanished.contains(player);
        boolean HideChat = HideChatExe.chatdisabled.contains(puuid);
        boolean Stacker = StackerExe.stackerenabled.contains(puuid);
        if (Variable.MYsql){
            scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    try {
                        SQLDataSourceManager mysql = SQLDataSourceManager.getInstance();
                        PreparedStatement ps = mysql.getFuckingConnection().prepareStatement("INSERT IGNORE PS_stats VALUES (?, ?, ?, ?, ?, ?);");
                        ps.setString(1, puuid.toString());
                        ps.setInt(2, 0);
                        ps.setInt(3, 0);
                        ps.setInt(4, 0);
                        ps.setInt(5, 0);
                        ps.setInt(6, 0);
                        ps.execute();
                        ps.close();
                        mysql.getFuckingConnection().close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
            scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    try {
                        SQLDataSourceManager mysql = SQLDataSourceManager.getInstance();
                        PreparedStatement ps = mysql.getFuckingConnection().prepareStatement("UPDATE PS_stats SET Fly=?, Spped=?, HidePlayer=?, HideChat=?, Stacker=? WHERE PlayerUUID = ?");
                        ps.setInt(1, (Fly ? 1 : 0));
                        ps.setInt(2, (Speed ? 1 : 0));
                        ps.setInt(3, (HidePlayer ? 1 : 0));
                        ps.setInt(4, (HideChat ? 1 : 0));
                        ps.setInt(5, (Stacker ? 1 : 0));
                        ps.setString(6, puuid.toString());
                        ps.execute();
                        ps.close();
                        mysql.getFuckingConnection().close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            },20);
        }
    }
}
