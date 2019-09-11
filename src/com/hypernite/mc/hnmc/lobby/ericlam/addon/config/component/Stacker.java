package com.hypernite.mc.hnmc.lobby.ericlam.addon.config.component;

import com.hypernite.mc.hnmc.core.config.Component;
import com.hypernite.mc.hnmc.core.config.Prop;

@Component
public class Stacker {

    @Prop(path = "Enable")
    private boolean enable;

    @Prop(path = "Throw-Power")
    private double flowPower;

    @Prop(path = "Throw-Y")
    private double flowY;

    @Prop(path = "Max-Stack")
    private int maxStack;

    public boolean isEnable() {
        return enable;
    }

    public double getFlowPower() {
        return flowPower;
    }

    public double getFlowY() {
        return flowY;
    }

    public int getMaxStack() {
        return maxStack;
    }
}
