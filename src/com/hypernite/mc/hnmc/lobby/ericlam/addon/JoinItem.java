package com.hypernite.mc.hnmc.lobby.ericlam.addon;

import org.bukkit.Material;

import java.util.List;

public class JoinItem {
    private String name;
    private List<String> lores;
    private String command;
    private Material material;
    private int slot;

    JoinItem(String name, List<String> lores, String command, Material material, int slot) {
        this.name = name;
        this.lores = lores;
        this.command = command;
        this.material = material;
        this.slot = slot;
    }

    public int getSlot() {
        return slot;
    }

    public String getName() {
        return name;
    }

    public List<String> getLores() {
        return lores;
    }

    public String getCommand() {
        return command;
    }

    public Material getMaterial() {
        return material;
    }
}
