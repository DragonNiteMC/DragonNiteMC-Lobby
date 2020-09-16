package com.ericlam.mc.hnmclobby.listener;

import com.ericlam.mc.hnmclobby.config.MainConfig;
import com.ericlam.mc.hnmclobby.config.MessagesConfig;
import com.ericlam.mc.hnmclobby.manager.GUIManager;
import com.ericlam.mc.hnmclobby.manager.PlayerSettingManager;
import com.hypernite.mc.hnmc.core.managers.YamlManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


public class OnPlayerListener implements Listener {

    private final MessagesConfig msg;
    private final MainConfig config;
    private final PlayerSettingManager psm;
    private final GUIManager gui;
    private final Plugin plugin;

    public OnPlayerListener(YamlManager cf, GUIManager gui, Plugin plugin, PlayerSettingManager psm) {
        config = cf.getConfigAs(MainConfig.class);
        msg = cf.getConfigAs(MessagesConfig.class);
        this.psm = psm;
        this.gui = gui;
        this.plugin = plugin;
    }

    @EventHandler
    public void onHideChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID puuid = player.getUniqueId();
        if (psm.getPlayerSetting(puuid).isHideChat()) {
            event.setCancelled(true);
            event.getRecipients().remove(player);
            player.sendMessage(msg.get("HideChat.hidden"));
        }
        event.getRecipients().removeIf(pl -> psm.getPlayerSetting(pl.getUniqueId()).isHideChat());
    }

    @EventHandler
    public void onPlayerPush(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.LEFT_CLICK_AIR) && !event.getPlayer().getPassengers().isEmpty()) {
            Player thrower = event.getPlayer();
            Set<Player> toThrow = new HashSet<>();
            Player top = thrower;
            while (top.getPassengers().size() > 0) {
                top = getPlayer(toThrow, top);
                if (top == null) return;
            }
            thrower.eject();
            for (Player toPush : toThrow) {
                toPush.eject();
                toPush.setVelocity(thrower.getLocation().getDirection().multiply(config.stacker.throwPower));
                toPush.setVelocity(new Vector(toPush.getVelocity().getX(), config.stacker.throwY, toPush.getVelocity().getZ()));
                if (toThrow.size() == 1)
                    thrower.sendMessage(msg.getStacker("pushed").replace("<player>", toPush.getName()));
            }
            if (toThrow.size() > 1) thrower.sendMessage(msg.getStacker("pushed-all"));
        }
    }

    private Player getPlayer(Set<Player> toThrow, Player top) {
        if (top.getPassengers().size() != 1) {
            return null;
        }
        Entity topentity = top.getPassengers().get(0);
        if (!(topentity instanceof Player)) {
            return null;
        }
        top = (Player) topentity;
        toThrow.add(top);
        return top;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID puuid = player.getUniqueId();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            psm.savePlayerSetting(puuid);
        });
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID puuid = player.getUniqueId();
        int amplifier = config.speedLevel - 1;

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            psm.getPlayerSetting(puuid);
            Bukkit.getScheduler().runTask(plugin, () -> {
                boolean speed = psm.getPlayerSetting(puuid).isSpeed() && player.hasPermission("settings.speed");
                boolean fly = psm.getPlayerSetting(puuid).isFly() && player.hasPermission("settings.fly");
                player.setAllowFlight(fly);
                player.setFlying(fly);
                if (speed) player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999 * 20, amplifier));
                else player.removePotionEffect(PotionEffectType.SPEED);
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
                online.hidePlayer(plugin, player);
                plugin.getLogger().info("DEBUG: Someone Enabled HidePlayer, Hided onJoin player.");
            } else {
                online.showPlayer(plugin, player);
                plugin.getLogger().info("DEBUG: Someone Disabled HidePlayer, Showed onJoin player.");
            }

            if (psm.getPlayerSetting(player.getUniqueId()).isHidePlayer()) {
                player.hidePlayer(plugin, online);
                plugin.getLogger().info("DEBUG: onJoinPlayer Enabled HidePlayer, Hided online players.");
            } else {
                player.showPlayer(plugin, online);
                plugin.getLogger().info("DEBUG: onJoinPlayer Disabled HidePlayer, Showed online players.");
            }
        }

    }

    @EventHandler
    public void onPlayerStacker(PlayerInteractEntityEvent event) {
        if (event.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        if (!config.stacker.enable) {
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
            player.sendMessage(msg.getStacker("disactive"));
            return;
        }
        if (!psm.getPlayerSetting(target.getUniqueId()).isStacker()) {
            player.sendMessage(msg.getStacker("be-disactive"));
            return;
        }


        int maxStack = config.stacker.maxStack;
        int stack = 0;
        Player top = player;
        /*Bukkit.broadcastMessage(ChatColor.RED + "======DEBUG======");
        Bukkit.broadcastMessage("Click Player: " + event.getPlayer().getName());
        Bukkit.broadcastMessage("Clicked Player: " + event.getRightClicked().getName());
        Bukkit.broadcastMessage("Layer" + stack + ": " + top.getName());*/
        Set<Player> stacks = new HashSet<>();
        stacks.add(top);
        while (top.getPassengers().size() > 0) {
            top = getPlayer(stacks, top);
            stack++;
            //Bukkit.broadcastMessage("Layer" + stack + ": " + top.getName());
        }
        //Bukkit.broadcastMessage(ChatColor.RED + "=================");

        if (!stacks.contains(target)) {
            if (stack >= maxStack) {
                player.sendMessage(msg.getStacker("Max"));
                return;
            }
            top.addPassenger(target);
            player.sendMessage(msg.getStacker("stacked").replace("<player>", ((Player) entity).getDisplayName()));
        }

    }

}
