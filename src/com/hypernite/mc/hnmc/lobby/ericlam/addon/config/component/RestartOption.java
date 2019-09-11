package com.hypernite.mc.hnmc.lobby.ericlam.addon.config.component;

import com.hypernite.mc.hnmc.core.config.Component;
import com.hypernite.mc.hnmc.core.config.Prop;

@Component
public class RestartOption {

    @Prop(path = "Time")
    private String time;

    @Prop(path = "Day-Period")
    private int dayPeriod;

    @Prop(path = "Custom-Time-Zone")
    private boolean customTimeZone;

    @Prop(path = "Time-Zone")
    private String timeZone;

    @Prop(path = "Spigot-Restart")
    private boolean spigotRestart;

    @Prop(path = "Delay-Online")
    private int delayOnline;

    @Prop(path = "Delay-Sec")
    private int delaySec;

    @Prop(path = "Check-Interval")
    private int checkInterval;

    @Prop(path = "Notify-Before")
    private int notifyBefore;

    public String getTime() {
        return time;
    }

    public int getDayPeriod() {
        return dayPeriod;
    }

    public boolean isCustomTimeZone() {
        return customTimeZone;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public boolean isSpigotRestart() {
        return spigotRestart;
    }

    public int getDelayOnline() {
        return delayOnline;
    }

    public int getDelaySec() {
        return delaySec;
    }

    public int getCheckInterval() {
        return checkInterval;
    }

    public int getNotifyBefore() {
        return notifyBefore;
    }
}
