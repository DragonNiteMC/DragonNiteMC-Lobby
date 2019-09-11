package com.hypernite.mc.hnmc.lobby.ericlam.addon.config.component;

import com.hypernite.mc.hnmc.core.config.Component;
import com.hypernite.mc.hnmc.core.config.Prop;

import java.util.Map;

@Component
public class CommandMSG {

    @Prop(path = "Heal")
    private Map<String, String> heal;

    @Prop(path = "Fly")
    private Map<String, String> fly;

    @Prop(path = "HideChat")
    private Map<String, String> hideChat;

    @Prop(path = "HidePlayer")
    private Map<String, String> hidePlayer;

    @Prop(path = "Stacker")
    private Map<String, String> stacker;

    @Prop
    private Map<String, String> spawn;

    @Prop(path = "GUI")
    private Map<String, String> gui;

    @Prop(path = "Speed")
    private Map<String, String> speed;

    public Map<String, String> getSpeed() {
        return speed;
    }

    public Map<String, String> getHeal() {
        return heal;
    }

    public Map<String, String> getFly() {
        return fly;
    }

    public Map<String, String> getHideChat() {
        return hideChat;
    }

    public Map<String, String> getHidePlayer() {
        return hidePlayer;
    }

    public Map<String, String> getStacker() {
        return stacker;
    }

    public Map<String, String> getSpawn() {
        return spawn;
    }

    public Map<String, String> getGui() {
        return gui;
    }
}
