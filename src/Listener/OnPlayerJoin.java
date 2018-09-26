package Listener;

import CmdExecute.ericlam.*;
import addon.ericlam.MySQL;
import main.ericlam.PlayerSettings;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.Hash;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static addon.ericlam.Variable.*;
import static addon.ericlam.Variable.config;
import static addon.ericlam.Variable.prefix;

public class OnPlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException, SQLException {
        Player player = event.getPlayer();
        UUID puuid = player.getUniqueId();
        if (MYsql){
            MySQL mysql = MySQL.getinstance();
            PreparedStatement ps = mysql.connection.prepareStatement("INSERT IGNORE "+table+" VALUES (?, ?, ?, ?, ?);");
            ps.setString(1, puuid.toString());
            ps.setInt(2, 0);
            ps.setInt(3, 0);
            ps.setInt(4, 0);
            ps.setInt(5,0);
            ps.execute();
            PreparedStatement ps2 =  mysql.connection.prepareStatement("SELECT * FROM "+table+" WHERE PlayerUUID=?");
            ps2.setString(1, puuid.toString());
            ResultSet result = ps2.executeQuery();
            if (result.getString("PlayerUUID") != null) {
                boolean speed = !player.hasPotionEffect(PotionEffectType.SPEED);
                boolean nohide = !HidePlayerExe.vanished.contains(player);
                if (result.getInt("Fly") == 1) {
                    FlyExe.flyExecutor(player, player);
                }
                if (result.getInt("Stacker") == 1) {
                    StackerExe.stackerenabled.add(puuid);
                }
                if (result.getInt("HideChat") == 1) {
                    HideChatExe.chatdisabled.add(puuid);
                }
                if (result.getInt("Speed") == 1){
                    if(speed) SpeedExe.SetSpeed(player,player);
                }else if (!speed) player.removePotionEffect(PotionEffectType.SPEED);
                if (result.getInt("HidePlayer") == 1){
                    if (nohide) {
                        HidePlayerExe.HidePlayer(player, player);
                    }
                }else if (!nohide) {
                    HidePlayerExe.HidePlayer(player, player);
                }
            } else {player.sendMessage(prefix + ChatColor.RED + "錯誤，我們無法讀取你的UUID數據!");}
        }
        if (yaml) {
            //Flight Stats
            renametoUUID(puuid, player);
            FileConfiguration uuidFileName = uuidYml(puuid);
            if (uuidFileName.contains("Flight") && uuidFileName.getBoolean("Flight")) {
                FlyExe.flyExecutor(player, player);
            }
            //HideChat Stats
            if (uuidFileName.contains("HideChat") && uuidFileName.getBoolean("HideChat")) {
                HideChatExe.chatdisabled.add(puuid);
            }
            //Stacker Stats
            if (uuidFileName.contains("Stacker") && uuidFileName.getBoolean("Stacker")) {
                StackerExe.stackerenabled.add(puuid);
            }
        }
        if (config.getBoolean("Join-Show-UUID.Enable"))
            player.sendMessage(prefix + returnColoredMessage("Functions.ShowUUID.JoinMessage").replace("<UUID>", puuid.toString()));
    }
    @EventHandler
    public void HidePlayerOnJoin(PlayerJoinEvent e){
        Player target = e.getPlayer();
        for (Player player : HidePlayerExe.vanished){
            player.hidePlayer(PlayerSettings.plugin, target);
        }
    }
}
