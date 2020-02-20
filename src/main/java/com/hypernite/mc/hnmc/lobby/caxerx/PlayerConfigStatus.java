package com.hypernite.mc.hnmc.lobby.caxerx;

public class PlayerConfigStatus {
    private boolean fly;
    private boolean hideChat;
    private boolean hidePlayer;
    private boolean stacker;
    private boolean speed;
    private boolean changed;


    public PlayerConfigStatus() {
        this.fly = false;
        this.hideChat = false;
        this.hidePlayer = false;
        this.stacker = false;
        this.speed = false;
        this.changed = false;
    }

    public PlayerConfigStatus(boolean fly, boolean hideChat, boolean hidePlayer, boolean stacker, boolean speed) {
        this.fly = fly;
        this.hideChat = hideChat;
        this.hidePlayer = hidePlayer;
        this.stacker = stacker;
        this.speed = speed;
        this.changed = false;
    }

    public boolean isFly() {
        return fly;
    }

    public boolean isHideChat() {
        return hideChat;
    }

    public boolean isHidePlayer() {
        return hidePlayer;
    }

    public boolean isStacker() {
        return stacker;
    }

    public boolean isSpeed() {
        return speed;
    }

    public void setFly(boolean fly) {
        if (fly != this.fly) {
            this.changed = true;
        }
        this.fly = fly;
    }

    public void setHideChat(boolean hideChat) {
        if (hideChat != this.hideChat) {
            this.changed = true;
        }
        this.hideChat = hideChat;
    }

    public void setHidePlayer(boolean hidePlayer) {
        if (hidePlayer != this.hidePlayer) {
            this.changed = true;
        }
        this.hidePlayer = hidePlayer;
    }

    public void setStacker(boolean stacker) {
        if (stacker != this.stacker) {
            this.changed = true;
        }
        this.stacker = stacker;
    }

    public void setSpeed(boolean speed) {
        if (speed != this.speed) {
            this.changed = true;
        }
        this.speed = speed;
    }

    public boolean isChanged() {
        return changed;
    }
}