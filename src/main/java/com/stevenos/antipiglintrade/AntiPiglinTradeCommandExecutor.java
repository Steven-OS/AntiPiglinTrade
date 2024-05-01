package com.steven_os.antipiglintrade;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import java.util.Collections;
import java.util.List;

public class AntiPiglinTradeCommandExecutor implements CommandExecutor, TabCompleter {

    private final com.steven_os.antipiglintrade.AntiPiglinTrade plugin;

    public AntiPiglinTradeCommandExecutor(com.steven_os.antipiglintrade.AntiPiglinTrade plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        PluginDescriptionFile pdfFile = plugin.getDescription();

        if(args.length == 0) {
            sender.sendMessage(plugin.getPrefix() + pdfFile.getVersion() + " made by Steven-OS");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("antipiglintrade.reload")) {
                sender.sendMessage(plugin.getPrefix() + "You don't have permission to do this.");
                return true;
            }

            plugin.reloadConfig();
            plugin.loadConfig();
            sender.sendMessage(plugin.getPrefix() + ChatColor.translateAlternateColorCodes('&', "&aConfig reloaded"));
            return true;
        }

        sender.sendMessage(plugin.getPrefix() + "Invalid command");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Collections.singletonList("reload");
        }
        return null;
    }
}