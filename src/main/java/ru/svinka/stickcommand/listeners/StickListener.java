package ru.svinka.stickcommand.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import ru.svinka.stickcommand.StickCommand;
import ru.svinka.stickcommand.StickCommand.StickData;

import java.util.Map;

public class StickListener implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) {
            return;
        }

        Player damager = (Player) event.getDamager();
        Player target = (Player) event.getEntity();

        ItemStack item = damager.getInventory().getItemInMainHand();
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
            return;
        }

        String itemName = ChatColor.stripColor(item.getItemMeta().getDisplayName());
        Map<String, StickData> sticks = StickCommand.getInstance().getSticks();

        if (!sticks.containsKey(itemName)) {
            return;
        }

        if (!damager.hasPermission("stickcommand.use")) {
            damager.sendMessage(ChatColor.RED + StickCommand.getInstance().getMessage("no_permission"));
            return;
        }

        StickData stickData = sticks.get(itemName);
        String command = stickData.getCommand()
                .replace("{player}", target.getName())
                .replace("{x}", String.valueOf(Math.round(target.getLocation().getX())))
                .replace("{y}", String.valueOf(Math.round(target.getLocation().getY())))
                .replace("{z}", String.valueOf(Math.round(target.getLocation().getZ())));

        try {
            StickCommand.getInstance().getServer().dispatchCommand(
                    StickCommand.getInstance().getServer().getConsoleSender(), command);
            damager.sendMessage(ChatColor.GREEN + StickCommand.getInstance().getMessage("command_executed")
                    .replace("{player}", target.getName()).replace("{command}", command));
        } catch (Exception e) {
            String errorMessage = StickCommand.getInstance().getMessage("command_failed").replace("{command}", command);
            damager.sendMessage(ChatColor.RED + errorMessage);
            StickCommand.getInstance().getLogger().severe(errorMessage);
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Player)) {
            return;
        }

        Player damager = event.getPlayer();
        Player target = (Player) event.getRightClicked();

        ItemStack item = damager.getInventory().getItemInMainHand();
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
            return;
        }

        String itemName = ChatColor.stripColor(item.getItemMeta().getDisplayName());
        Map<String, StickData> sticks = StickCommand.getInstance().getSticks();

        if (!sticks.containsKey(itemName)) {
            return;
        }

        if (!damager.hasPermission("stickcommand.use")) {
            damager.sendMessage(ChatColor.RED + StickCommand.getInstance().getMessage("no_permission"));
            return;
        }

        StickData stickData = sticks.get(itemName);
        String command = stickData.getCommand()
                .replace("{player}", target.getName())
                .replace("{x}", String.valueOf(Math.round(target.getLocation().getX())))
                .replace("{y}", String.valueOf(Math.round(target.getLocation().getY())))
                .replace("{z}", String.valueOf(Math.round(target.getLocation().getZ())));

        try {
            StickCommand.getInstance().getServer().dispatchCommand(
                    StickCommand.getInstance().getServer().getConsoleSender(), command);
            damager.sendMessage(ChatColor.GREEN + StickCommand.getInstance().getMessage("command_executed")
                    .replace("{player}", target.getName()).replace("{command}", command));
        } catch (Exception e) {
            String errorMessage = StickCommand.getInstance().getMessage("command_failed").replace("{command}", command);
            damager.sendMessage(ChatColor.RED + errorMessage);
            StickCommand.getInstance().getLogger().severe(errorMessage);
        }
    }
}
