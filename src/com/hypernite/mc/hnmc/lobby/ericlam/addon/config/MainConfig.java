package com.hypernite.mc.hnmc.lobby.ericlam.addon.config;

import com.hypernite.mc.hnmc.core.config.Prop;
import com.hypernite.mc.hnmc.core.config.yaml.Configuration;
import com.hypernite.mc.hnmc.core.config.yaml.Resource;
import com.hypernite.mc.hnmc.lobby.ericlam.addon.config.component.RestartOption;
import com.hypernite.mc.hnmc.lobby.ericlam.addon.config.component.Stacker;

@Resource(locate = "config.yml")
public class MainConfig extends Configuration {

    @Prop(path = "General.Use-MySQL")
    private boolean useMySQL;

    @Prop(path = "Join-Show-UUID.Enable")
    private boolean joinShowUUID;

    @Prop(path = "Stacker")
    private Stacker stacker;

    @Prop(path = "Speed.Level")
    private int speedLevel;

    @Prop(path = "GUI.Enable-Notify-On-OpenGUI")
    private boolean notifyGUIOpen;

    @Prop(path = "Restart")
    private RestartOption restartOption;

    public boolean isUseMySQL() {
        return useMySQL;
    }

    public boolean isJoinShowUUID() {
        return joinShowUUID;
    }

    public Stacker getStacker() {
        return stacker;
    }

    public int getSpeedLevel() {
        return speedLevel;
    }

    public boolean isNotifyGUIOpen() {
        return notifyGUIOpen;
    }

    public RestartOption getRestartOption() {
        return restartOption;
    }
}
