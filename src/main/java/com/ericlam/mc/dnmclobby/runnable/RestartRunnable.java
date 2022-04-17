package com.ericlam.mc.dnmclobby.runnable;


import com.ericlam.mc.dnmclobby.config.MainConfig;
import com.ericlam.mc.dnmclobby.main.DNMCLobby;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Timestamp;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class RestartRunnable extends BukkitRunnable {
    private final Plugin plugin;
    private final boolean spigotRestart;
    private final int time;
    private final int delayTime;
    private final int delayPlayers;
    private final ZoneId zone;
    private LocalDateTime firstCheck;
    private final LocalDate firstDate;
    private final int dayPeriod;
    private final LocalTime restartTime;

    public RestartRunnable(Plugin plugin) {
        this.plugin = plugin;
        firstDate = LocalDate.now();
        MainConfig.Restart config = DNMCLobby.getConfigManager().getConfigAs(MainConfig.class).restart;
        boolean customZone = config.customTimeZone;
        dayPeriod = config.dayPeriod;
        spigotRestart = config.spigotRestart;
        String timeZone = config.timeZone;
        String[] time = Optional.ofNullable(config.time).map(t -> t.split(":")).orElse(new String[]{"03", "30"});
        int beforeMin = config.notifyBefore;
        this.time = beforeMin * 60;
        delayTime = config.delaySec;
        delayPlayers = config.delayOnline;
        int hours = Integer.parseInt(time[0]);
        int mins = Integer.parseInt(time[1]);
        restartTime = LocalTime.of(hours, mins).plusMinutes(-beforeMin);
        plugin.getLogger().info("DEBUG: Server scheduled restart at " + restartTime.plusMinutes(beforeMin).truncatedTo(ChronoUnit.MINUTES));
        plugin.getLogger().info("DEBUG: Server launch the countdown at " + restartTime.truncatedTo(ChronoUnit.MINUTES));
        if (!customZone || timeZone == null) zone = ZoneId.systemDefault();
        else zone = ZoneId.of(timeZone);
    }

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
                if (Period.between(firstDate, nowDate).getDays() >= dayPeriod) {
                    new CountdownRunnable(time, plugin, delayTime, delayPlayers, spigotRestart).runTaskTimer(plugin, 0L, 20L);
                    cancel();
                }
            }
        }
    }
}
