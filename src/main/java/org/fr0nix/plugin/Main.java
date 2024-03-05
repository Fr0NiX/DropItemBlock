package org.fr0nix.plugin;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Main extends JavaPlugin implements Listener {

    private FileConfiguration config;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);

        // Создаем конфигурационный файл при первом запуске
        setupDefaultConfig(); // Используем метод setupDefaultConfig() вместо saveDefaultConfig()
        config = getConfig();

        getLogger().info("Плагин инициализирован!"); // Сообщение о инициализации плагина
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.OAK_LOG) {
            Player player = event.getPlayer();
            if (player != null) {
                Random rand = new Random();
                int chance = rand.nextInt(100);
                int dropChance = this.config.getInt("dropChance", 5);
                String itemName = this.config.getString("itemName", "&aКиви");
                Material itemMaterial = Material.matchMaterial(this.config.getString("itemMaterial", "MELON"));
                if (chance < dropChance) {
                    ItemStack itemStack = new ItemStack(itemMaterial);
                    ItemMeta itemMeta = itemStack.getItemMeta(); // Create a new ItemMeta
                    itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemName));
                    itemStack.setItemMeta(itemMeta); // Set the ItemMeta back to the ItemStack
                    event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), itemStack);
                }
            }
        }
    }

    private void setupDefaultConfig() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveResource("config.yml", false);
        }
    }
}