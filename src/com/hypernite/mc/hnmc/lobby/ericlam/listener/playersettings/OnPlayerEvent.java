package com.hypernite.mc.hnmc.lobby.ericlam.listener.playersettings;

import com.hypernite.mc.hnmc.core.managers.ConfigManager;
import com.hypernite.mc.hnmc.lobby.caxerx.PlayerSettingManager;
import com.hypernite.mc.hnmc.lobby.ericlam.addon.GUIBuilder;
import com.hypernite.mc.hnmc.lobby.ericlam.addon.LobbyConfig;
import com.hypernite.mc.hnmc.lobby.main.HNMCLobby;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


public class OnPlayerEvent implements Listener {

    private ConfigManager cf;
    private LobbyConfig lobbycf;
    private PlayerSettingManager psm;

    public OnPlayerEvent(){
        cf = HNMCLobby.getConfigManager();
        lobbycf = HNMCLobby.getLobbyConfig();
        psm = PlayerSettingManager.getInstance();
    }

    @EventHandler
    public void onHideChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        UUID puuid = player.getUniqueId();
        PlayerSettingManager psm = PlayerSettingManager.getInstance();
        if (psm.getPlayerSetting(puuid).isHideChat()) {
            event.setCancelled(true);
            event.getRecipients().remove(player);
            player.sendMessage(cf.getMessage("Commands.HideChat.hidden"));
        }
        event.getRecipients().removeIf(pl -> psm.getPlayerSetting(pl.getUniqueId()).isHideChat());
    }

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
                toPush.setVelocity(thrower.getLocation().getDirection().multiply(lobbycf.config.getInt("Stacker.Throw-Power")));
                toPush.setVelocity(new Vector(toPush.getVelocity().getX(), lobbycf.config.getDouble("Stacker.Throw-Y"), toPush.getVelocity().getZ()));
                if (toThrow.size() == 1) thrower.sendMessage(cf.getMessage("Commands.Stacker.pushed").replace("<player>", toPush.getName()));
            }
            if (toThrow.size() > 1)thrower.sendMessage(cf.getMessage("Commands.Stacker.pushed-all"));
        }
    }

    @EventHandler
    public void PlayerLeaveMySQL(PlayerQuitEvent event){
        Player player = event.getPlayer();
        UUID puuid = player.getUniqueId();
        if (lobbycf.isMySQL())
            Bukkit.getScheduler().runTaskAsynchronously(HNMCLobby.plugin, () -> {
                PlayerSettingManager.getInstance().savePlayerSetting(puuid);
            });
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID puuid = player.getUniqueId();
        int amplifier = lobbycf.config.getInt("Speed.Level") - 1;

        Bukkit.getScheduler().runTaskAsynchronously(HNMCLobby.plugin, () -> {
            psm.getPlayerSetting(puuid);
            Bukkit.getScheduler().runTask(HNMCLobby.plugin, () -> {
                boolean speed = psm.getPlayerSetting(puuid).isSpeed() && player.hasPermission("settings.speed");
                boolean fly = psm.getPlayerSetting(puuid).isFly() && player.hasPermission("settings.fly");
                player.setAllowFlight(fly);
                player.setFlying(fly);
                if (speed) player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999*20, amplifier));
                else player.removePotionEffect(PotionEffectType.SPEED);


                if (lobbycf.config.getBoolean("Join-Show-UUID.Enable")) {
                    player.sendMessage(cf.getMessage("Functions.ShowUUID.JoinMessage").replace("<UUID>", puuid.toString()));
                }
                GUIBuilder gui = GUIBuilder.getInstance();
                gui.getInventoryGUI(player);
                trackPlayerHide(player);
            });
        });
    }

    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent e) {
        trackPlayerHide(e.getPlayer());
    }

    private void trackPlayerHide(Player player) {

        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
            if (online == player) continue;

            if (psm.getPlayerSetting(online.getUniqueId()).isHidePlayer()) {
                online.hidePlayer(HNMCLobby.plugin, player);
                HNMCLobby.plugin.getLogger().info("DEBUG: Someone Enabled HidePlayer, Hided onJoin player.");
            } else {
                online.showPlayer(HNMCLobby.plugin, player);
                HNMCLobby.plugin.getLogger().info("DEBUG: Someone Disabled HidePlayer, Showed onJoin player.");
            }

            if (psm.getPlayerSetting(player.getUniqueId()).isHidePlayer()) {
                player.hidePlayer(HNMCLobby.plugin, online);
                HNMCLobby.plugin.getLogger().info("DEBUG: onJoinPlayer Enabled HidePlayer, Hided online players.");
            } else {
                player.showPlayer(HNMCLobby.plugin, online);
                HNMCLobby.plugin.getLogger().info("DEBUG: onJoinPlayer Disabled HidePlayer, Showed online players.");
            }
        }

    }

    @EventHandler
    public void onPlayerStacker(PlayerInteractEntityEvent event) throws SQLException {
        if (event.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        if (!lobbycf.config.getBoolean("Stacker.Enable")) {
            return;
        }

        Player player = event.getPlayer();
        UUID puuid = player.getUniqueId();
        Entity entity = event.getRightClicked();

        if (!(entity instanceof Player) || !player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
            return;
        }

        Player target = (Player) entity;

        if (!psm.getPlayerSetting(puuid).isStacker()) {
            player.sendMessage(cf.getMessage("Commands.Stacker.disactive"));
            return;
        }
        if (!psm.getPlayerSetting(target.getUniqueId()).isStacker()) {
            player.sendMessage(cf.getMessage("Commands.Stacker.be-disactive"));
            return;
        }


        int maxStack = lobbycf.config.getInt("Stacker.Max-Stack");
        int stack = 0;
        Player top = player;
        /*Bukkit.broadcastMessage(ChatColor.RED + "======DEBUG======");
        Bukkit.broadcastMessage("Click Player: " + event.getPlayer().getName());
        Bukkit.broadcastMessage("Clicked Player: " + event.getRightClicked().getName());
        Bukkit.broadcastMessage("Layer" + stack + ": " + top.getName());*/
        Set<Player> stacks = new HashSet<>();
        stacks.add(top);
        while (top.getPassengers().size() > 0) {
            if (top.getPassengers().size() != 1) {
                Bukkit.broadcastMessage("returned");
                return;
            }
            Entity topEntity = top.getPassengers().get(0);
            if (!(topEntity instanceof Player)) {
                Bukkit.broadcastMessage("returned");
                return;
            }
            top = (Player) topEntity;
            stacks.add(top);
            stack++;
            //Bukkit.broadcastMessage("Layer" + stack + ": " + top.getName());
        }
        //Bukkit.broadcastMessage(ChatColor.RED + "=================");

        if (!stacks.contains(target)) {
            if (stack >= maxStack) {
                player.sendMessage(cf.getMessage("Commands.Stacker.Max"));
                return;
            }
            top.addPassenger(target);
            player.sendMessage(cf.getMessage("Commands.Stacker.stacked").replace("<player>", ((Player) entity).getDisplayName()));
        }

    }

}
