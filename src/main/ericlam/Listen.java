package main.ericlam;

import addon.ericlam.MySQL;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import addon.ericlam.Variable;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.List;
import org.bukkit.util.Vector;

import javax.xml.transform.Result;

import static addon.ericlam.Variable.*;
import static main.ericlam.PlayerSettings.addNewFile;
import static main.ericlam.PlayerSettings.plugin;
import static main.ericlam.PlayerSettings.renametoUUID;


public class Listen implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (config.getBoolean("ChatFilter.Enable")) {
            Player player = event.getPlayer();
            String msg = event.getMessage();
            BukkitScheduler scheduler = Bukkit.getScheduler();
            List<String> keys = config.getStringList("ChatFilter.List");
            boolean chatbypass = player.hasPermission("ericlam.chat.bypass");
            for (String keyword : keys) {
                if (msg.contains(keyword) && !chatbypass) {
                    event.setCancelled(true);
                    player.sendMessage(prefix + PlayerSettings.returnColoredMessage("Functions.ChatResponse.block-message"));
                    break;
                }
            }
            if (msg.contains("fuck you") && !chatbypass) {
                event.setCancelled(true);
                player.sendMessage(prefix + ChatColor.RED + "Fuck you too!");
            }
            if ((msg.contains("i want to die")) && !chatbypass) {
                scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        player.setHealth(0);
                        player.sendMessage(prefix + ChatColor.RED + "Here you go!");
                        event.setCancelled(true);
                    }
                });


            }
        }
    }

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        if (config.getBoolean("SneakShowLocation.Enable")) {
            Player player = event.getPlayer();
            Location loc = player.getLocation();
            boolean perm = player.hasPermission("ericlam.sneak.showlocation");
            int x = (int) loc.getX();
            int y = (int) loc.getY();
            int z = (int) loc.getZ();
            String xyz = x + " " + y + " " + z;
            if (player.isSneaking() && player.getWalkSpeed() < 0.25 && player.isOnGround() && perm) {
                player.sendTitle(PlayerSettings.returnColoredMessage("Functions.Sneak.show-location-title"), xyz, 5, 30, 5);
            }
        }
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException, SQLException {
        Player player = event.getPlayer();
        UUID puuid = player.getUniqueId();
        if (MYsql){
            MySQL mysql = MySQL.getinstance();
            PreparedStatement ps = mysql.connection.prepareStatement("INSERT IGNORE "+table+" (PlayerUUID, Fly, HideChat, Stacker) WHERE VALUES (?, ?, ?, ?);");
            ps.setString(1, puuid.toString());
            ps.setInt(2, 0);
            ps.setInt(3, 0);
            ps.setInt(4, 0);
            PreparedStatement ps2 =  mysql.connection.prepareStatement("SELECT * FROM "+table+" WHERE PlayerUUID=?");
            ps2.setString(1, puuid.toString());
            ResultSet result = ps2.executeQuery();
            if (result.getInt("Fly") == 1){
                FlyExe.flyExecutor(player,player);
            }
            if (result.getInt("Stacker") == 1){
                StackerExe.stackerenabled.add(puuid);
            }
            if (result.getInt("HideChat") == 1){
                HideChatExe.chatdisabled.add(puuid);
            }
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
            player.sendMessage(prefix + PlayerSettings.returnColoredMessage("Functions.ShowUUID.JoinMessage").replace("<UUID>", puuid.toString()));
    }

    @EventHandler
    public void onHideChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID puuid = player.getUniqueId();
        if (HideChatExe.chatdisabled.contains(puuid)) {
            event.setCancelled(true);
            event.getRecipients().remove(player);
            player.sendMessage(prefix + PlayerSettings.returnColoredMessage("Commands.HideChat.hidden"));
        }
        for (Player pl : event.getRecipients()) {
            if (HideChatExe.chatdisabled.contains(pl.getUniqueId())) event.getRecipients().remove(pl);
        }
    }

    @EventHandler
    public void onPlayerStacker(PlayerInteractEntityEvent event) {
        if (config.getBoolean("Stacker.Enable")) {
            Player player = event.getPlayer();
            Entity entity = event.getRightClicked();
            UUID puuid = player.getUniqueId();
            if (entity instanceof Player && player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
                List<Entity> rider = player.getPassengers();
                Player rideentity = (Player) entity;
                UUID rideruuid = rideentity.getUniqueId();
                if (StackerExe.stackerenabled.contains(puuid)) {
                    if (rider == null && StackerExe.stackerenabled.contains(rideruuid)) {
                        player.addPassenger(entity);
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(PlayerSettings.returnColoredMessage("Commands.Stacker.stacked").replace("<player>", ((Player) entity).getDisplayName())));
                    } else if (StackerExe.stackerenabled.contains(rideruuid)) {
                        for (Entity ride : rider) {
                            if (rider.size() >= (config.getInt("Stacker.Max-Stack"))) {
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(PlayerSettings.returnColoredMessage("Commands.Stacker.Max")));
                                break;
                            }
                            Player ridep = (Player) ride;
                            if (ridep.getPassengers() == null) ridep.addPassenger(entity);
                        }
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(PlayerSettings.returnColoredMessage("Commands.Stacker.stacked").replace("<player>", ((Player) entity).getDisplayName())));
                    } else {
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(PlayerSettings.returnColoredMessage("Commands.Stacker.be-disactive")));
                    }
                } else {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(PlayerSettings.returnColoredMessage("Commands.Stacker.disactive")));
                }
            }
        }
    }
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
                thrower.sendMessage(prefix + PlayerSettings.returnColoredMessage("Commands.Stacker.pushed" + (toThrow.size() > 1 ? "-all" : "")).replace("<player>", toPush.getDisplayName()));
            }
        }
    }
}
