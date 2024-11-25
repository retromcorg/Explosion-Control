package com.johnymuffin.beta.tntcontrol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class ExplosionControl extends JavaPlugin {
   private static Logger log;

   private static PluginDescriptionFile pdf;

   private ExplosionControl plugin;

   private ArrayList<Material> blockWhitelist = new ArrayList<>(Arrays.asList(Material.DIRT,
           Material.STONE, Material.GRAVEL, Material.COBBLESTONE, Material.COAL_ORE, Material.REDSTONE_ORE,
           Material.GLOWING_REDSTONE_ORE, Material.IRON_ORE, Material.DIAMOND_ORE, Material.GOLD_ORE,
           Material.LAPIS_ORE, Material.GRASS, Material.LONG_GRASS, Material.SAND, Material.SANDSTONE,
           Material.SAPLING, Material.LOG, Material.LEAVES, Material.DEAD_BUSH, Material.YELLOW_FLOWER,
           Material.RED_ROSE, Material.RED_MUSHROOM, Material.BROWN_MUSHROOM, Material.TORCH, Material.SNOW,
           Material.CACTUS, Material.CLAY, Material.SUGAR_CANE_BLOCK, Material.ICE));

   private ArrayList<Material> blockCheckBlacklist = new ArrayList<>(Arrays.asList(Material.DIODE_BLOCK_OFF, Material.DIODE_BLOCK_ON,
           Material.STONE_BUTTON, Material.LEVER, Material.RAILS, Material.DETECTOR_RAIL, Material.POWERED_RAIL,
           Material.REDSTONE_WIRE, Material.REDSTONE_TORCH_OFF, Material.REDSTONE_TORCH_ON, Material.SIGN_POST,
           Material.WOOD_PLATE, Material.STONE_PLATE, Material.WOODEN_DOOR, Material.IRON_DOOR_BLOCK, Material.CAKE_BLOCK,
           Material.WALL_SIGN, Material.TRAP_DOOR, Material.LADDER));

   public void onEnable() {
      log = getServer().getLogger();
      this.plugin = this;
      pdf = this.plugin.getDescription();
      log.info("[" + pdf.getName() + "] Is loading");
      ExplosionControlListener explosionControlListener = new ExplosionControlListener(this.plugin);
      Bukkit.getServer().getPluginManager().registerEvents(explosionControlListener, (Plugin)this.plugin);
   }

   public void onDisable() {
      log.info("[" + pdf.getName() + "] Is Disabled");
   }

   public static void logInfo(String s) {
      log.info("[" + pdf.getName() + "] " + s);
   }

   public ArrayList<Material> getBlockWhitelist() {
      return this.blockWhitelist;
   }

   public ArrayList<Material> getBlockCheckBlacklist() {
      return this.blockCheckBlacklist;
   }
}