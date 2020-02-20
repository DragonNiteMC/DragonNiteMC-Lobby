package com.hypernite.mc.hnmc.lobby.ericlam.restart;


import com.hypernite.mc.hnmc.lobby.ericlam.addon.config.MainConfig;
import com.hypernite.mc.hnmc.lobby.main.HNMCLobby;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Timestamp;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class ScheduleRestart {
    private Plugin plugin;
    private boolean spigotRestart;
    private int time;
    private int delayTime;
    private int delayPlayers;
    private ZoneId zone;
    private LocalDateTime firstCheck;
    private LocalDate firstDate;
    private int dayPeriod;

    public ScheduleRestart() {
        firstDate = LocalDate.now();
        MainConfig.Restart config = HNMCLobby.getConfigManager().getConfigAs(MainConfig.class).restart;
        plugin = HNMCLobby.plugin;
        boolean customZone = config.customTimeZone;
        dayPeriod = config.dayPeriod;
        spigotRestart = config.spigotRestart;
        String timeZone = config.timeZone;
        String[] time = Optional.ofNullable(config.time).map(t -> t.split(":")).orElse(new String[]{"03", "30"});
        int beforeMin = config.notifyBefore;
        this.time = beforeMin * 60;
        int interval = config.checkInterval * 60;
        delayTime = config.delaySec;
        delayPlayers = config.delayOnline;
        int hours = Integer.parseInt(time[0]);
        int mins = Integer.parseInt(time[1]);
        LocalTime restartTime = LocalTime.of(hours, mins).plusMinutes(-beforeMin);
        plugin.getLogger().info("DEBUG: Server scheduled restart at " + restartTime.plusMinutes(beforeMin).truncatedTo(ChronoUnit.MINUTES));
        plugin.getLogger().info("DEBUG: Server launch the countdown at " + restartTime.truncatedTo(ChronoUnit.MINUTES));
        if (!customZone || timeZone == null) zone = ZoneId.systemDefault();
        else zone = ZoneId.of(timeZone);
        new BukkitRunnable() {
            @Override
            public void run() {

                LocalDateTime now = LocalDateTime.now(zone);

                if (firstCheck == null) firstCheck = now.plusMinutes(1);

                else {
                    long first = Timestamp.valueOf(firstCheck).getTime();
                    long second = Timestamp.valueOf(now).getTime();
                    long restart = Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), restartTime)).getTime();

                    if (restart >= first && restart <= second) {
                        LocalDate nowDate = LocalDate.now();
                        if (Period.between(firstDate, nowDate).getDays() >= dayPeriod){
                            countDownRestart();
                            cancel();
                        }
                    }
                }

                //plugin.getLogger().info("DEBUG: first check: "+firstCheck.toLocalTime().truncatedTo(ChronoUnit.MINUTES));
                //plugin.getLogger().info("DEBUG: second check: "+now.toLocalTime().truncatedTo(ChronoUnit.MINUTES));
            }
        }.runTaskTimerAsynchronously(plugin, 0L, interval * 20L);
    }
    private int Task;
    private void countDownRestart(){
        Task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin,()->{
            if (time%60 == 0 && time > 0){
                plugin.getLogger().info("Server is restarting in "+time/60+" mins");
                Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle("","§a伺服器將在§e "+time/60+" §a分鐘後重啟。",20,60,20));
            }else if (time%30 == 0 || time <= 10) {
                plugin.getLogger().info("Server is restarting in "+time+ " sec.");
                Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle("","§a伺服器將在§e "+time+" §a秒後重啟。",0,30,0));
            }

            if (time == 0){
                Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle("§a伺服器正在重啟...","",0,100,20));
                if (Bukkit.getOnlinePlayers().size() >= delayPlayers) {
                    Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle("","§e人數過多，加時§b "+delayTime+" §e秒。",20,60,20));
                    Bukkit.broadcastMessage("§4系統// §c由於伺服器內的玩家人數超過了"+delayPlayers+"個，本系統將延時"+delayTime+"秒後再重啟。");
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
