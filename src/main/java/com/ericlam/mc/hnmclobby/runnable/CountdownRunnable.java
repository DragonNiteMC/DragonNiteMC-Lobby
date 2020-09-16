package com.ericlam.mc.hnmclobby.runnable;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class CountdownRunnable extends BukkitRunnable {

    private final Plugin plugin;
    private final long delayTime;
    private final int delayPlayers;
    private final boolean spigotRestart;
    private long time;

    public CountdownRunnable(long time, Plugin plugin, long delayTime, int delayPlayers, boolean spigotRestart) {
        this.time = time;
        this.plugin = plugin;
        this.delayTime = delayTime;
        this.delayPlayers = delayPlayers;
        this.spigotRestart = spigotRestart;
    }

    @Override
    public void run() {
        if (time % 60 == 0 && time > 0) {
            plugin.getLogger().info("Server is restarting in " + time / 60 + " mins");
            Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle("", "§a伺服器將在§e " + time / 60 + " §a分鐘後重啟。", 20, 60, 20));
        } else if (time % 30 == 0 || time <= 10) {
            plugin.getLogger().info("Server is restarting in " + time + " sec.");
            Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle("", "§a伺服器將在§e " + time + " §a秒後重啟。", 0, 30, 0));
        }

        if (time == 0) {
            Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle("§a伺服器正在重啟...", "", 0, 100, 20));
            if (Bukkit.getOnlinePlayers().size() >= delayPlayers) {
                Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle("", "§e人數過多，加時§b " + delayTime + " §e秒。", 20, 60, 20));
                Bukkit.broadcastMessage("§4系統// §c由於伺服器內的玩家人數超過了" + delayPlayers + "個，本系統將延時" + delayTime + "秒後再重啟。");
                time += delayTime + 1;
            } else {
                if (spigotRestart) Bukkit.getServer().spigot().restart();
                else Bukkit.getServer().shutdown();
                cancel();
            }
        }
        time -= 1;
    }
}
