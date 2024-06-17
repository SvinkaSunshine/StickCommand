package ru.svinka.stickcommand.commands;

import ru.svinka.stickcommand.StickCommand;
import ru.svinka.stickcommand.StickCommand.StickData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class SCCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /sc <reload|give> [stickName]");
            return false;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("stickcommand.reload")) {
                sender.sendMessage(ChatColor.RED + StickCommand.getInstance().getMessage("no_permission"));
                return false;
            }

            StickCommand.getInstance().reload();
            sender.sendMessage(ChatColor.GREEN + StickCommand.getInstance().getMessage("reload_success"));
            return true;

        } else if (args[0].equalsIgnoreCase("give")) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Usage: /sc give <stickName>");
                return false;
            }

            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + StickCommand.getInstance().getMessage("player_only"));
                return false;
            }

            Player player = (Player) sender;
            StringJoiner joiner = new StringJoiner(" ");
            for (int i = 1; i < args.length; i++) {
                joiner.add(args[i]);
            }
            String stickName = joiner.toString();
            Map<String, StickData> sticks = StickCommand.getInstance().getSticks();

            if (!sticks.containsKey(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', stickName)))) {
                player.sendMessage(ChatColor.RED + StickCommand.getInstance().getMessage("no_such_stick"));
                return false;
            }

            StickData stickData = sticks.get(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', stickName)));

            ItemStack stick = new ItemStack(Material.STICK);
            ItemMeta meta = stick.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', stickData.getName()));
            if (stickData.getLore() != null) {
                List<String> lore = new ArrayList<>();
                for (String line : stickData.getLore()) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', line));
                }
                meta.setLore(lore);
            }
            if (stickData.isEnchanting()) {
                meta.addEnchant(org.bukkit.enchantments.Enchantment.ARROW_INFINITE, 1, true);
                meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            }
            stick.setItemMeta(meta);

            player.getInventory().addItem(stick);
            player.sendMessage(ChatColor.GREEN + StickCommand.getInstance().getMessage("stick_given").replace("{stickName}", stickName));
            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            if ("reload".startsWith(args[0].toLowerCase())) {
                completions.add("reload");
            }
            if ("give".startsWith(args[0].toLowerCase())) {
                completions.add("give");
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            completions.addAll(StickCommand.getInstance().getSticks().keySet());
        }

        return completions;
    }
}
