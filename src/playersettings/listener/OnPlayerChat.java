package playersettings.listener;

import addon.ericlam.Variable;
import com.caxerx.mc.PlayerSettingManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.sql.SQLException;
import java.util.UUID;

import static addon.ericlam.Variable.messagefile;


public class OnPlayerChat implements Listener {
    private Variable var = new Variable();
    @EventHandler
    public void onHideChat(AsyncPlayerChatEvent event) throws SQLException {
        Player player = event.getPlayer();
        UUID puuid = player.getUniqueId();
        PlayerSettingManager psm = PlayerSettingManager.getInstance();
        if (psm.getPlayerSetting(puuid).isHideChat()) {
            event.setCancelled(true);
            event.getRecipients().remove(player);
            player.sendMessage(var.prefix() + var.getFs().returnColoredMessage(messagefile,"Commands.HideChat.hidden"));
        }
        event.getRecipients().removeIf(pl -> psm.getPlayerSetting(pl.getUniqueId()).isHideChat());
    }



}
