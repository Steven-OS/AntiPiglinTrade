package com.steven_os.antipiglintrade;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class AntiPiglinTrade extends JavaPlugin implements Listener {

    private String prefix;
    private String errorMessage;
    private List<String> disallowedWorlds;

    @Override
    public void onEnable() {
        registerEventsAndCommands();
        loadConfig();
        initMetrics();
        printConsoleMessage();
    }

    private void registerEventsAndCommands() {
        getServer().getPluginManager().registerEvents(this, this);
        com.steven_os.antipiglintrade.AntiPiglinTradeCommandExecutor executor = new com.steven_os.antipiglintrade.AntiPiglinTradeCommandExecutor(this);
        getCommand("antipiglintrade").setExecutor(executor);
        getCommand("antipiglintrade").setTabCompleter(executor);
    }

    @Override
    public void onDisable() {
        getLogger().info("AntiPiglinTrade plugin has been disabled.");
    }

    public void loadConfig() {
        saveDefaultConfig();
        reloadConfig();
        prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("prefix", "&6AntiPiglinTrade &8»&f "));
        errorMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString("error-message", "&cPiglin bartering is disabled."));
        disallowedWorlds = getConfig().getStringList("world-list");
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (event.getEntityType() == EntityType.PIGLIN && isGoldIngot(event.getItem().getItemStack()) && isDisallowedWorld(event.getEntity().getWorld().getName())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClickEvent(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Piglin) {
            Player player = event.getPlayer();
            if (isGoldIngot(player.getInventory().getItemInMainHand()) && isDisallowedWorld(player.getWorld().getName())) {
                player.sendMessage(prefix + errorMessage);
                event.setCancelled(true);
            }
        }
    }

    private boolean isGoldIngot(ItemStack item) {
        return item.getType() == Material.GOLD_INGOT;
    }

    private boolean isDisallowedWorld(String worldName) {
        return disallowedWorlds.contains(worldName);
    }

    private void initMetrics() {
        boolean isEnableBStats = getConfig().getBoolean("enable-bstats", true);
        if (isEnableBStats) {
            int pluginId = 21750;
            new Metrics(this, pluginId);
        }
    }

    private void printConsoleMessage() {
        getLogger().info("--------------------------------------------------------------");
        getLogger().info("» AntiPiglinTrade has loaded successfully!");
        getLogger().info("» Made by: Steven-OS");
        getLogger().info("» Plugin version: " + getDescription().getVersion());
        getLogger().info("» Server version: " + Bukkit.getServer().getVersion());
        getLogger().info("--------------------------------------------------------------");
    }

    public String getPrefix() {
        return prefix;
    }
}
