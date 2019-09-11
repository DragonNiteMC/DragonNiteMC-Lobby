package com.hypernite.mc.hnmc.lobby.ericlam.addon.config.component;

import com.hypernite.mc.hnmc.core.config.Component;
import com.hypernite.mc.hnmc.core.config.Prop;

import java.util.List;

@Component
public class JoinItem {

    @Prop
    private String name;

    @Prop
    private int slot;

    @Prop
    private List<String> lores;

    @Prop
    private String command;

    public String getName() {
        return name;
    }

    public int getSlot() {
        return slot;
    }

    public List<String> getLores() {
        return lores;
    }

    public String getCommand() {
        return command;
    }
}
