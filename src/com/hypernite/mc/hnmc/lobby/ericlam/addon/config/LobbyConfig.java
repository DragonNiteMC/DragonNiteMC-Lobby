package com.hypernite.mc.hnmc.lobby.ericlam.addon.config;

import com.hypernite.mc.hnmc.core.config.Prop;
import com.hypernite.mc.hnmc.core.config.yaml.Configuration;
import com.hypernite.mc.hnmc.core.config.yaml.Resource;
import com.hypernite.mc.hnmc.lobby.ericlam.addon.config.component.JoinItem;

import java.util.Map;

@Resource(locate = "Lobby.yml")
public class LobbyConfig extends Configuration {

    @Prop(path = "join.donor-msg")
    private String joinDonorMessage;

    @Prop(path = "tablist-header")
    private String tablistHeader;

    @Prop(path = "join-items")
    private Map<String, JoinItem> joinItemMap;

    public String getJoinDonorMessage() {
        return joinDonorMessage;
    }

    public String getTablistHeader() {
        return tablistHeader;
    }

    public Map<String, JoinItem> getJoinItemMap() {
        return joinItemMap;
    }
}
