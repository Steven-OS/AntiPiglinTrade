package com.steven_os.antipiglintrade;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiPiglinTrade extends JavaPlugin implements Listener {

    private FileConfiguration config;
    private String prefix;
    private String errorMessage;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        com.steven_os.antipiglintrade.AntiPiglinTradeCommandExecutor executor = new com.steven_os.antipiglintrade.AntiPiglinTradeCommandExecutor(this);
        getCommand("antipiglintrade").setExecutor(executor);
        getCommand("antipiglintrade").setTabCompleter(executor);
        loadConfig();
        printConsoleMessage();
        boolean isEnableBStats = getConfig().getBoolean("enable-bstats", true); // The second parameter is default value

        // Initialize Metrics only if 'enable-bstats' is true
        if (isEnableBStats) {
            int pluginId = 21750; // Replace this with your bStats plugin ID!
            com.steven_os.antipiglintrade.Metrics metrics = new com.steven_os.antipiglintrade.Metrics(this, pluginId);
            // Add your custom charts or other bStats-related logic here
            // only if 'enable-bstats' is true.
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("AntiPiglinTrade plugin has been disabled.");
    }

    public void loadConfig() {
        saveDefaultConfig();
        config = getConfig();
        prefix = ChatColor.translateAlternateColorCodes('&', config.getString("prefix", "&eAntiPiglinTrade &8»&f "));
        errorMessage = ChatColor.translateAlternateColorCodes('&', config.getString("error-message", "&cPiglin bartering is disabled."));
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if(event.getEntityType() == EntityType.PIGLIN) {
            ItemStack pickedItem = event.getItem().getItemStack();
            if(pickedItem.getType() == Material.GOLD_INGOT && !isAllowedWorld(event.getEntity().getWorld().getName())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onClickEvent(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Piglin)) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.GOLD_INGOT && !isAllowedWorld(player.getWorld().getName())) {
            player.sendMessage(getPrefix() + errorMessage);
            event.setCancelled(true);
        }
    }

    private boolean isAllowedWorld(String worldName) {
        return !config.getStringList("world-list").contains(worldName);
    }

    private void printConsoleMessage() {
        getLogger().info("--------------------------------------------------------------");
        getLogger().info("");
        getLogger().info("» AntiPiglinTrade has loaded successfully!");
        getLogger().info("» Made by: Steven-OS");
        getLogger().info("» Plugin version: " + getDescription().getVersion());
        getLogger().info("» Server version: " + Bukkit.getServer().getVersion());
        getLogger().info("");
        getLogger().info("--------------------------------------------------------------");
    }

    public String getPrefix() {
        return prefix;
    }
}