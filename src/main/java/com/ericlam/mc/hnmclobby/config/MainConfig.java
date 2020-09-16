package com.ericlam.mc.hnmclobby.config;

import com.hypernite.mc.hnmc.core.config.yaml.Configuration;
import com.hypernite.mc.hnmc.core.config.yaml.Resource;

@Resource(locate = "config.yml")
public class MainConfig extends Configuration {

    public Stacker stacker;
    public int speedLevel;
    public Restart restart;

    public static class Stacker {
        public boolean enable;
        public int throwPower;
        public double throwY;
        public int maxStack;
    }

    public static class Restart {
        public String time;
        public int dayPeriod;
        public boolean customTimeZone;
        public String timeZone;
        public boolean spigotRestart;
        public int delayOnline;
        public int delaySec;
        public int checkInterval;
        public int notifyBefore;
    }
}
