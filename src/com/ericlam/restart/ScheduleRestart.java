package com.ericlam.restart;


import com.ericlam.addon.Variable;
import main.HyperNiteMC;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class ScheduleRestart {
    private Plugin plugin;
    private boolean spigotRestart;
    private int time;
    private int delayTime;
    private int delayPlayers;
    private ZoneId zone;
    private LocalDateTime firstCheck;

    public ScheduleRestart() {
        FileConfiguration config = Variable.config;
        plugin = HyperNiteMC.plugin;
        boolean customZone = config.getBoolean("Restart.Custom-Time-Zone");
        spigotRestart = config.getBoolean("Restart.Spigot-Restart");
        String timeZone = config.getString("Restart.Time-Zone");
        String[] time = config.getString("Restart.Time").split(":");
        int beforeMin = config.getInt("Restart.Notify-Before");
        this.time =  beforeMin * 60;
        int interval = config.getInt("Restart.Check-Interval") * 60;
        delayTime = config.getInt("Restart.Delay-Sec");
        delayPlayers = config.getInt("Restart.Delay-Online");
        int hours = Integer.parseInt(time[0]);
        int mins = Integer.parseInt(time[1]);
        LocalTime restartTime = LocalTime.of(hours,mins).plusMinutes(-beforeMin);
        plugin.getLogger().info("DEBUG: Server scheduled restart at "+restartTime.plusMinutes(beforeMin).truncatedTo(ChronoUnit.MINUTES));
        plugin.getLogger().info("DEBUG: Server launch the countdown at "+restartTime.truncatedTo(ChronoUnit.MINUTES));
        if (!customZone) zone = ZoneId.systemDefault();
        else zone = ZoneId.of(timeZone);
        synchronized (ScheduleRestart.class) {
            new BukkitRunnable() {
                @Override
                public void run() {

                    LocalDateTime now = LocalDateTime.now(zone);

                    if (firstCheck == null) firstCheck = now;

                    else {
                        long first = Timestamp.valueOf(firstCheck).getTime();
                        long second = Timestamp.valueOf(now).getTime();
                        long restart = Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), restartTime)).getTime();

                        if (restart >= first && restart <= second) {
                            countDownRestart();
                            cancel();
                        }
                    }

                    //plugin.getLogger().info("DEBUG: first check: "+firstCheck.toLocalTime().truncatedTo(ChronoUnit.MINUTES));
                    //plugin.getLogger().info("DEBUG: second check: "+now.toLocalTime().truncatedTo(ChronoUnit.MINUTES));
                }
            }.runTaskTimerAsynchronously(plugin, 0L, interval * 20L);
        }
    }
    private int Task;
    private synchronized void countDownRestart(){
        Task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin,()->{
            if (time%60 == 0 && time > 0){
                plugin.getLogger().info("Server is restarting in "+time/60+" mins");
                Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle("","§a伺服器將在§e "+time/60+" §a分鐘後重啟。",20,60,20));
            }

            if (time <= 10) {
                plugin.getLogger().info("Server is restarting in "+time+ " sec.");
                Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle("","§a伺服器將在§e "+time+" §a秒後重啟。",0,30,0));
            }

            if (time == 0){
                Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle("§a伺服器正在重啟...","",0,100,20));
                if (Bukkit.getOnlinePlayers().size() >= delayPlayers) {
                    Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle("","§e人數過多，加時§b "+delayTime+" §e秒。",20,60,20));
                    Bukkit.broadcastMessage("§4系統// §c由於伺服器內的玩家人數超過了"+delayPlayers+"個，本系統將延時至"+delayTime+"秒後再重啟。");
                    time += delayTime+1;
                }else{
                    if (spigotRestart) Bukkit.getServer().spigot().restart();
                    else Bukkit.getServer().shutdown();
                    Bukkit.getScheduler().cancelTask(Task);
                }
            }

            time -= 1;

        },0L,20L);
    }
}
