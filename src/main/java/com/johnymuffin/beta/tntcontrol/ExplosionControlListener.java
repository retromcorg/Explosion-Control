package com.johnymuffin.beta.tntcontrol;

import java.util.ArrayList;

import com.johnymuffin.beta.tntcontrol.config.ExplosionControlSettings;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.entity.Creeper;

import static com.johnymuffin.beta.tntcontrol.ExplosionControl.blockBlacklist;
import static com.johnymuffin.beta.tntcontrol.ExplosionControl.blockWhitelist;

public class ExplosionControlListener implements Listener {
   private ExplosionControl plugin;
   private long lastResetTime = System.currentTimeMillis();
   private int explosionCount = 0;

   private ExplosionControlSettings settings;

   private final boolean tntChainingEnabled;
   private final int tntChainingMaximum;
   private final long tntChainingDurationMilliseconds;

   private final boolean tntBehaviorCancelNonWhitelist;
   private final boolean tntBehaviorUseTNTWhitelist;
   private final boolean tntBehaviorIgnoreSingleBlockPopOff;

   public ExplosionControlListener(ExplosionControl plugin) {
      this.plugin = plugin;
      settings = plugin.getSettings();

      // Load Config Values
      tntChainingEnabled = settings.getConfigBoolean("settings.tnt-chaining.enabled");
      tntChainingMaximum = settings.getConfigInteger("settings.tnt-chaining.maximum");
      tntChainingDurationMilliseconds = settings.getConfigLong("settings.tnt-chaining.duration-milliseconds");

      tntBehaviorCancelNonWhitelist = settings.getConfigBoolean("settings.tnt-behavior.cancel-entire-explosion-if-non-whitelisted.enabled");
      tntBehaviorUseTNTWhitelist = settings.getConfigBoolean("settings.tnt-behavior.use-tnt-whitelist.enabled");
      tntBehaviorIgnoreSingleBlockPopOff = settings.getConfigBoolean("settings.tnt-behavior.ignore-single-if-block-pop-off.enabled");
   }

   @EventHandler
   public void onExplode(EntityExplodeEvent event) {
      if (event.isCancelled())
         return;
      Location location = event.getLocation();


      // Creper explosions sh
      World.Environment environment = location.getWorld().getEnvironment();
      if (event.getEntity() instanceof Creeper
              && (environment.equals(World.Environment.NORMAL) || environment.equals(World.Environment.SKYLANDS))) {
         event.blockList().clear();
         return;
      }

      if (environment.equals(World.Environment.NETHER))
         return;
      if (!environment.equals(World.Environment.NORMAL)) {
         event.setCancelled(true);
         return;
      }

      if(tntChainingEnabled){
         long currentTime = System.currentTimeMillis();

         if (currentTime - lastResetTime >= tntChainingDurationMilliseconds) {
            lastResetTime = currentTime;
            explosionCount = 0;
         }
      }

      ArrayList<Block> blocksToRemove = new ArrayList<>();
      for (Block b : event.blockList()) {
         Material type = b.getType();

         if(tntBehaviorCancelNonWhitelist){
            if (!blockWhitelist.contains(type)){
               event.setCancelled(true);
               return;
            }
         }
         else if(tntBehaviorUseTNTWhitelist){
            if (!blockWhitelist.contains(type)){
               blocksToRemove.add(b);
               continue;
            }
         }

         if(tntBehaviorIgnoreSingleBlockPopOff) {
            if(blockBlacklist.contains(b.getRelative(0, 1, 0).getType())){
               blocksToRemove.add(b);
               continue;
            }
            if(blockBlacklist.contains(b.getRelative(1, 0, 0).getType())){
               blocksToRemove.add(b);
               continue;
            }
            if(blockBlacklist.contains(b.getRelative(-1, 0, 0).getType())){
               blocksToRemove.add(b);
               continue;
            }
            if(blockBlacklist.contains(b.getRelative(0, 0, 1).getType())){
               blocksToRemove.add(b);
               continue;
            }
            if(blockBlacklist.contains(b.getRelative(0, 0, -1).getType())){
               blocksToRemove.add(b);
            }
         }

         if(tntChainingEnabled && type == Material.TNT){
            if (explosionCount >= tntChainingMaximum) {
               blocksToRemove.add(b);
            }
            else{
               explosionCount++;
            }
         }
         else if(type == Material.TNT){
            blocksToRemove.add(b);
         }
      }

      event.blockList().removeAll(blocksToRemove);
   }

   @EventHandler
   public void onEntityDamageByBlock(EntityDamageByBlockEvent event) {
      if (event.isCancelled())
         return;
      if (!(event.getEntity() instanceof org.bukkit.entity.Player))
         return;
      if (!event.getEntity().getWorld().getEnvironment().equals(World.Environment.NORMAL) || !event.getEntity().getWorld().getEnvironment().equals(World.Environment.SKYLANDS))
         return;
      if (event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)
         event.setCancelled(true);
   }
}
