package com.johnymuffin.beta.tntcontrol;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import com.johnymuffin.beta.tntcontrol.config.ExplosionControlSettings;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class ExplosionControl extends JavaPlugin {
   private static Logger log;

   private static PluginDescriptionFile pdf;

   private ExplosionControl plugin;

   private ExplosionControlSettings settings;

   public static final Set<Material> blockWhitelist = new HashSet<>(Arrays.asList(Material.DIRT,
           Material.STONE, Material.GRAVEL, Material.COBBLESTONE, Material.COAL_ORE, Material.REDSTONE_ORE,
           Material.GLOWING_REDSTONE_ORE, Material.IRON_ORE, Material.DIAMOND_ORE, Material.GOLD_ORE,
           Material.LAPIS_ORE, Material.GRASS, Material.LONG_GRASS, Material.SAND, Material.SANDSTONE,
           Material.SAPLING, Material.LOG, Material.LEAVES, Material.DEAD_BUSH, Material.YELLOW_FLOWER,
           Material.RED_ROSE, Material.RED_MUSHROOM, Material.BROWN_MUSHROOM, Material.TORCH, Material.SNOW,
           Material.CACTUS, Material.CLAY, Material.SUGAR_CANE_BLOCK, Material.ICE, Material.TNT, Material.FIRE,
           Material.LAVA, Material.WATER, Material.STATIONARY_LAVA, Material.STATIONARY_WATER, Material.LEVER,
           Material.REDSTONE_TORCH_OFF, Material.REDSTONE_TORCH_ON));

   public static final Set<Material> blockBlacklist = new HashSet<>(Arrays.asList(Material.DIODE_BLOCK_OFF, Material.DIODE_BLOCK_ON,
           Material.STONE_BUTTON, Material.RAILS, Material.DETECTOR_RAIL, Material.POWERED_RAIL, Material.REDSTONE_WIRE, Material.SIGN_POST,
           Material.WOOD_PLATE, Material.STONE_PLATE, Material.WOODEN_DOOR, Material.IRON_DOOR_BLOCK, Material.CAKE_BLOCK,
           Material.WALL_SIGN, Material.TRAP_DOOR, Material.LADDER, Material.SUGAR_CANE_BLOCK, Material.WATER, Material.STATIONARY_WATER,
           Material.LAVA, Material.STATIONARY_LAVA, Material.SAPLING));

   public void onEnable() {
      log = getServer().getLogger();
      this.plugin = this;
      pdf = this.plugin.getDescription();
      log.info("[" + pdf.getName() + "] Is loading");

      File dataFolder = plugin.getDataFolder();
      if (!dataFolder.exists()) {
         dataFolder.mkdir();
      }

      File configFile = new File(dataFolder, "settings.yml");
      settings = new ExplosionControlSettings(configFile);

      ExplosionControlListener explosionControlListener = new ExplosionControlListener(this.plugin);
      Bukkit.getServer().getPluginManager().registerEvents(explosionControlListener, (Plugin)this.plugin);
   }

   public void onDisable() {
      log.info("[" + pdf.getName() + "] Is Disabled");
   }

   public static void logInfo(String s) {
      log.info("[" + pdf.getName() + "] " + s);
   }

   public ExplosionControlSettings getSettings() { return settings; }
}
