package com.ericlam.addon;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.ericlam.addon.ConfigManager.lobbyfile;
import static com.ericlam.addon.ConfigManager.messagefile;

public class TeleportLobby {
    private static TeleportLobby tplobby;
    private ConfigManager var = ConfigManager.getInstance();
    private double X;
    private double Y;
    private double Z;
    private World lobby;
    private double Pitch;
    private double Yaw;
    private Location spawn;


    public static TeleportLobby getInstance() {
        if(tplobby == null) tplobby = new TeleportLobby();
        return tplobby;
    }
    private void getNewSpawn(){
        if (needchangeSpawn()) {
            lobby = Bukkit.getWorld(ConfigManager.lobbyfile.getString("spawntp.world"));
            X = ConfigManager.lobbyfile.getDouble("spawntp.x");
            Y = ConfigManager.lobbyfile.getDouble("spawntp.y");
            Z = ConfigManager.lobbyfile.getDouble("spawntp.z");
            Pitch = ConfigManager.lobbyfile.getDouble("spawntp.pitch");
            Yaw = ConfigManager.lobbyfile.getDouble("spawntp.yaw");
            spawn = new Location(lobby, X, Y, Z, (float) Yaw, (float) Pitch);
        }
    }

    private boolean isNotNull(){
        return lobbyfile.contains("spawntp.world");
    }

    private boolean needchangeSpawn(){
        boolean changeX = X != ConfigManager.lobbyfile.getDouble("spawntp.x");
        boolean changeY = Y != ConfigManager.lobbyfile.getDouble("spawntp.y");
        boolean changeZ = Z != ConfigManager.lobbyfile.getDouble("spawntp.z");
        boolean changeW = lobby != Bukkit.getWorld(ConfigManager.lobbyfile.getString("spawntp.world"));
        boolean changeYaw = Yaw != ConfigManager.lobbyfile.getDouble("spawntp.yaw");
        boolean changePitch = Pitch != ConfigManager.lobbyfile.getDouble("spawntp.pitch");
        return changeX || changeY || changeZ || changeW || changeYaw || changePitch;
    }

    public boolean TeleportToLobby(Player player){
        if (isNotNull()) getNewSpawn();
        if (lobby == null || Y == 0) {
            player.sendMessage(var.prefix() + ChatColor.RED+ "伺服器尚未設置重生點，因此無法傳送");
            return false;
        }
        player.teleport(spawn);
        return true;
    }
    public void TeleportToLobby(Player player, CommandSender sender){
        if (isNotNull()) getNewSpawn();
        if (lobby == null || Y == 0){
            sender.sendMessage(var.prefix() + ChatColor.RED + "由於重生點尚未設置，因此無法傳送 " + ChatColor.YELLOW + player.getName() + ChatColor.RED + " 。");
            return;
        }
        player.teleport(spawn);
        sender.sendMessage(var.prefix() + var.getFs().returnColoredMessage(messagefile, "Commands.spawn.be-send").replace("<player>",player.getName()));
        player.sendMessage(var.prefix() + var.getFs().returnColoredMessage(messagefile, "Commands.spawn.send"));
    }
}
