package com.hypernite.mc.hnmc.lobby.ericlam.addon;

import com.hypernite.mc.hnmc.core.main.HyperNiteMC;
import com.hypernite.mc.hnmc.core.managers.ConfigManager;
import com.hypernite.mc.hnmc.core.managers.CoreConfig;
import com.hypernite.mc.hnmc.lobby.main.HNMCLobby;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportLobby {
    private static TeleportLobby tplobby;
    private LobbyConfig var;
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
    
    private TeleportLobby(){
        var = HNMCLobby.getLobbyConfig();
    }
    private void getNewSpawn(){
        if (needchangeSpawn()) {
            lobby = Bukkit.getWorld(var.lobbyfile.getString("spawntp.world"));
            X = var.lobbyfile.getDouble("spawntp.x");
            Y = var.lobbyfile.getDouble("spawntp.y");
            Z = var.lobbyfile.getDouble("spawntp.z");
            Pitch = var.lobbyfile.getDouble("spawntp.pitch");
            Yaw = var.lobbyfile.getDouble("spawntp.yaw");
            spawn = new Location(lobby, X, Y, Z, (float) Yaw, (float) Pitch);
        }
    }

    private boolean isNotNull(){
        return var.lobbyfile.contains("spawntp.world");
    }

    private boolean needchangeSpawn(){
        boolean changeX = X != var.lobbyfile.getDouble("spawntp.x");
        boolean changeY = Y != var.lobbyfile.getDouble("spawntp.y");
        boolean changeZ = Z != var.lobbyfile.getDouble("spawntp.z");
        boolean changeW = lobby != Bukkit.getWorld(var.lobbyfile.getString("spawntp.world"));
        boolean changeYaw = Yaw != var.lobbyfile.getDouble("spawntp.yaw");
        boolean changePitch = Pitch != var.lobbyfile.getDouble("spawntp.pitch");
        return changeX || changeY || changeZ || changeW || changeYaw || changePitch;
    }

    public boolean TeleportToLobby(Player player){
        CoreConfig var = HyperNiteMC.getAPI().getCoreConfig();
        if (isNotNull()) getNewSpawn();
        if (lobby == null || Y == 0) {
            player.sendMessage(var.getPrefix() + ChatColor.RED+ "伺服器尚未設置重生點，因此無法傳送");
            return false;
        }
        player.teleport(spawn);
        return true;
    }
    public void TeleportToLobby(Player player, CommandSender sender){
        ConfigManager var = HNMCLobby.getConfigManager();
        if (isNotNull()) getNewSpawn();
        if (lobby == null || Y == 0){
            sender.sendMessage(HyperNiteMC.getAPI().getCoreConfig().getPrefix() + ChatColor.RED + "由於重生點尚未設置，因此無法傳送 " + ChatColor.YELLOW + player.getName() + ChatColor.RED + " 。");
            return;
        }
        player.teleport(spawn);
        sender.sendMessage(var.getMessage("Commands.spawn.be-send").replace("<player>",player.getName()));
        player.sendMessage(var.getMessage("Commands.spawn.send"));
    }
}
