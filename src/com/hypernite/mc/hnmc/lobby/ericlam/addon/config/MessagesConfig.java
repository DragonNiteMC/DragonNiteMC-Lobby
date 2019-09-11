package com.hypernite.mc.hnmc.lobby.ericlam.addon.config;

import com.hypernite.mc.hnmc.core.config.Prop;
import com.hypernite.mc.hnmc.core.config.yaml.MessageConfiguration;
import com.hypernite.mc.hnmc.core.config.yaml.Prefixed;
import com.hypernite.mc.hnmc.core.config.yaml.Resource;
import com.hypernite.mc.hnmc.lobby.ericlam.addon.config.component.CommandMSG;

@Resource(locate = "Messages.yml")
public class MessagesConfig extends MessageConfiguration {

    @Prefixed
    @Prop(path = "Commands")
    private CommandMSG commandMSG;

    @Prefixed
    @Prop(path = "Functions.ShowUUID.JoinMessage")
    private String joinUUIDMessage;

    public CommandMSG getCommandMSG() {
        return commandMSG;
    }

    public String getJoinUUIDMessage() {
        return joinUUIDMessage;
    }
}
