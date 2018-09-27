package CmdExecute.ericlam;

import addon.ericlam.Variable;
import jdk.nashorn.internal.ir.ReturnNode;
import main.ericlam.PlayerSettings;
import net.minecraft.server.v1_13_R2.DataWatcher;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;
import java.sql.Array;
import java.sql.SQLException;
import java.util.*;

import static addon.ericlam.Variable.*;

public class SettingsExe implements CommandExecutor {
    private static SettingsExe setting;
    private final PlayerSettings plugin;
    public SettingsExe(PlayerSettings plugin){ this.plugin = plugin;}
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player target;
        boolean terminal = commandSender instanceof ConsoleCommandSender;
        boolean perm = commandSender.hasPermission("ericlam.settings");
        boolean permother = commandSender.hasPermission("ericlam.settings.other");
        if (strings.length <= 0 && perm) {
            if (!terminal) {
                Player player = (Player) commandSender;
                    OpenGUI(player, player);
            }else{commandSender.sendMessage(ChatColor.RED + "Console can only use /settings <player>");}
        } else if(permother || terminal){
            target = (Bukkit.getServer().getPlayer(strings[0]));
            if (target == null){
                commandSender.sendMessage(prefix + Variable.returnColoredMessage("General.Player-Not-Found"));
            }else {
                    OpenGUI(target, commandSender);
            }
        }else{
            commandSender.sendMessage(prefix + noperm);
        }
        return true;
    }
    public static Inventory getInventoryGUI(){
        Inventory PlayerSettingGUI = Bukkit.createInventory(null, 54, title);
        ItemStack[] representItem = {new ItemStack(Material.IRON_BOOTS), new ItemStack(Material.ELYTRA), new ItemStack(Material.PAPER), new ItemStack(Material.PLAYER_HEAD), new ItemStack(Material.STICKY_PISTON)};
        String[] ItemName = {"Speed", "Fly", "HideChat", "HidePlayer", "Stacker"};
        Integer[] ItemSlot = {10, 13, 16, 29, 33};
        for (int i = 0; i < representItem.length;i++){
            ItemStack IS = representItem[i];
            String IN = ItemName[i];
            int slot = ItemSlot[i];
            PlayerSettingGUI.setItem(slot, AddedMetaItem(IS, IN));
        }
        return PlayerSettingGUI;
    }
    public static Inventory OpenGUI(Player name, CommandSender sender) {
        Player p = name.getPlayer();
        Inventory gui = getInventoryGUI();
        p.openInventory(gui);
        if (sender != name) sender.sendMessage(returnColoredMessage("Commands.GUI.be-show"));
        if (messagefile.getBoolean("Commands.GUI.show.Enable-MSG-On-OpenGUI")) name.sendMessage("Commands.GUI.show");
        changeStatus(p, gui);
        return gui;
    }
    private static ItemStack AddedMetaItem(ItemStack Material, String ItemName){
        List<String> list = returnColoredStringList("Commands.GUI.Lore");
        ItemMeta MetaName = Material.getItemMeta();
        MetaName.setDisplayName(returnColoredMessage("Commands.GUI."+ItemName));
        MetaName.setLore(list);
        Material.setItemMeta(MetaName);
        return Material;
    }
    public static void changeStatus(Player player,Inventory inventory) {
        UUID puuid = player.getUniqueId();
        boolean Fly = player.getAllowFlight();
        boolean Speed = player.hasPotionEffect(PotionEffectType.SPEED);
        boolean HidePlayer = HidePlayerExe.vanished.contains(player);
        boolean HideChat = HideChatExe.chatdisabled.contains(puuid);
        boolean Stacker = StackerExe.stackerenabled.contains(puuid);
        Integer[] StatusSlot = {19, 22, 25, 38, 42};
        Boolean[] Status = {Speed, Fly, HideChat, HidePlayer, Stacker};
            for (int i = 0; i < Status.length;i++) {
                int slot = StatusSlot[i];
                boolean stats = Status[i];
                if (stats) inventory.setItem(slot, new ItemStack(Material.LIME_STAINED_GLASS_PANE));
                else inventory.setItem(slot, new ItemStack(Material.RED_STAINED_GLASS_PANE));
            }
    }
}
