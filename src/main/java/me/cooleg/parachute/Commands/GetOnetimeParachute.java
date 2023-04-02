package me.cooleg.parachute.Commands;

import me.cooleg.parachute.Parachute;
import me.cooleg.parachute.Util.ConfigWrapper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collections;
import java.util.UUID;

public class GetOnetimeParachute implements CommandExecutor {

    private final ItemStack parachute;
    private final boolean stack;
    private final NamespacedKey random;

    public GetOnetimeParachute(Parachute main, ConfigWrapper config) {
        parachute = new ItemStack(config.getOnetimeMat());
        stack = config.isOnetimeStack();
        random = new NamespacedKey(main, "unstack");
        ItemMeta meta = parachute.getItemMeta();
        meta.setDisplayName(config.getOnetimeName());
        meta.setLore(Collections.singletonList(config.getOnetimeDescription()));
        meta.setCustomModelData(config.getOnetimeData());
        meta.getPersistentDataContainer().set(new NamespacedKey(main, "item"), PersistentDataType.STRING, "onetime");
        parachute.setItemMeta(meta);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {return false;}
        final ItemStack clone = parachute.clone();
        if (!stack) {
            final ItemMeta cloneMeta = clone.getItemMeta();
            cloneMeta.getPersistentDataContainer().set(random, PersistentDataType.STRING, UUID.randomUUID().toString());
            clone.setItemMeta(cloneMeta);
        }
        if (args.length == 0) {
            ((Player) commandSender).getInventory().addItem(clone);
            commandSender.sendMessage(ChatColor.GREEN + "You were given a one time parachute!");
            return true;
        } else if (args.length == 1) {
            Player p = Bukkit.getPlayer(args[0]);
            if (p == null) {commandSender.sendMessage(ChatColor.RED + "This player doesn't exist!"); return true;}
            p.getInventory().addItem(clone);
            commandSender.sendMessage(ChatColor.GREEN + args[0] + " was given a one time parachute!");
            return true;
        } else {
            Player p = Bukkit.getPlayer(args[0]);
            if (p == null) {commandSender.sendMessage(ChatColor.RED + "This player doesn't exist!"); return true;}
            int count;
            try {
                count = Integer.parseInt(args[1]);
            } catch (NumberFormatException ex) {
                commandSender.sendMessage(ChatColor.RED + "Not a valid number!");
                return true;
            }
            if (count > 64) {count = 64;}
            if (count < 1) {count = 1;}
            ItemStack newStack = clone.clone();
            newStack.setAmount(count);
            p.getInventory().addItem(newStack);
            commandSender.sendMessage(ChatColor.GREEN + args[0] + " was given one time parachutes!");
            return true;
        }
    }

}
