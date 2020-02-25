package com.hypernite.mc.hnmc.lobby.ericlam.addon;

import org.bukkit.Material;

public class SettingItem {
    private int slot;
    private Material material;
    private String command;
    private String name;

    public SettingItem(int slot, Material material, String command, String name) {
        this.slot = slot;
        this.material = material;
        this.command = command;
        this.name = name;
    }

    public int getSlot() {
        return slot;
    }

    public Material getMaterial() {
        return material;
    }

    public String getCommand() {
        return command;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()) return false;
        SettingItem item = (SettingItem) obj;
        return item.getMaterial().equals(this.getMaterial());
    }

    @Override
    public int hashCode() {
        return this.getMaterial().hashCode();
    }
}
