package listener;

import addon.ericlam.Variable;
import command.ericlam.*;
import functions.hypernite.mc.Functions;
import main.ericlam.PlayerSettings;
import mysql.hypernite.mc.SQLDataSourceManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static addon.ericlam.Variable.*;

public class OnPlayerJoin implements Listener {
    private Variable var = Variable.getInstance();
    private Functions fs = new Functions(PlayerSettings.plugin);
    private PlayerSettings ps = (PlayerSettings) PlayerSettings.plugin;
    private FlyExe FlyExe = new FlyExe(ps);
    private SpeedExe SpeedExe = new SpeedExe(ps);
    private HidePlayerExe hidePlayerExe = new HidePlayerExe(ps);
    private HideChatExe hideChatExe = new HideChatExe(ps);
    private StackerExe stackerExe = new StackerExe(ps);
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException, SQLException {
        Player player = event.getPlayer();
        UUID puuid = player.getUniqueId();
        boolean speed = !player.hasPotionEffect(PotionEffectType.SPEED);
        boolean nohide = !HidePlayerExe.vanished.contains(player);
        if (MYsql){
            SQLDataSourceManager mysql = SQLDataSourceManager.getInstance();
            Connection connection = mysql.getFuckingConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT IGNORE PS_stats VALUES (?, ?, ?, ?, ?, ?);");
            ps.setString(1, puuid.toString());
            ps.setInt(2, 0);
            ps.setInt(3, 0);
            ps.setInt(4, 0);
            ps.setInt(5, 0);
            ps.setInt(6, 0);
            ps.execute();
            PreparedStatement ps2 = connection.prepareStatement("SELECT * FROM PS_stats WHERE PlayerUUID=?");
            ps2.setString(1, puuid.toString());
            ResultSet result = ps2.executeQuery();
            if (result.next()) {
                //Flight Stats
                if (result.getInt("Fly") == 1) {
                    FlyExe.flyExecutor(player, player);
                }
                //Stacker Stats
                if (result.getInt("Stacker") == 1 && config.getBoolean("Stacker.Enable")) {
                    StackerExe.stackerenabled.add(puuid);
                }else StackerExe.stackerenabled.remove(puuid);
                //HideChat Stats
                if (result.getInt("HideChat") == 1) {
                    HideChatExe.chatdisabled.add(puuid);
                }else HideChatExe.chatdisabled.remove(puuid);
                //Speed Stats
                if (result.getInt("Speed") == 1){
                    if(speed) SpeedExe.SetSpeed(player,player);
                }else if (!speed) player.removePotionEffect(PotionEffectType.SPEED);
                //HidePlayer Stats
                if (result.getInt("HidePlayer") == 1){
                    if (nohide) hidePlayerExe.HidePlayer(player, player);
                }else if (!nohide) {
                    hidePlayerExe.HidePlayer(player, player);
                }
            } else {player.sendMessage(var.prefix() + ChatColor.RED + "錯誤，我們無法讀取你的UUID數據!");}
        }
        if (yaml) {
            //Flight Stats
            fs.renametoUUID(puuid, player);
            FileConfiguration uuidFileName = uuidYml(puuid);
            if (uuidFileName.contains("Flight") && uuidFileName.getBoolean("Flight")) {
                FlyExe.flyExecutor(player, player);
            }
            //HideChat Stats
            if (uuidFileName.contains("HideChat") && uuidFileName.getBoolean("HideChat")) {
                HideChatExe.chatdisabled.add(puuid);
            }else HideChatExe.chatdisabled.remove(puuid);
            //Stacker Stats
            if (uuidFileName.contains("Stacker") && uuidFileName.getBoolean("Stacker") && config.getBoolean("Stacker.Enable")) {
                StackerExe.stackerenabled.add(puuid);
            }else StackerExe.stackerenabled.remove(puuid);
            //Speed Stats
            if (uuidFileName.contains("Speed") && uuidFileName.getBoolean("Speed")){
                if(speed) SpeedExe.SetSpeed(player,player);
            }else if (!speed) player.removePotionEffect(PotionEffectType.SPEED);
            //HidePlayer Stats
            if (uuidFileName.contains("HidePlayer") && uuidFileName.getBoolean("HidePlayer")){
                if (nohide) hidePlayerExe.HidePlayer(player,player);
            } else if (!nohide) hidePlayerExe.HidePlayer(player,player);
        }
        if (config.getBoolean("Join-Show-UUID.Enable"))
            player.sendMessage(var.prefix() + fs.returnColoredMessage(messagefile,"Functions.ShowUUID.JoinMessage").replace("<UUID>", puuid.toString()));
    }
    @EventHandler
    public void HidePlayerOnJoin(PlayerJoinEvent e){
        Player target = e.getPlayer();
        for (Player player : HidePlayerExe.vanished){
            player.hidePlayer(PlayerSettings.plugin, target);
        }
    }
}
