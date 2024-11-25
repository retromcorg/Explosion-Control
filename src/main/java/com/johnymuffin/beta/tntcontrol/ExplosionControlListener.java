package com.johnymuffin.beta.tntcontrol;

import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class ExplosionControlListener implements Listener {
   private ExplosionControl plugin;

   public ExplosionControlListener(ExplosionControl plugin) {
      this.plugin = plugin;
   }

   @EventHandler
   public void onExplode(EntityExplodeEvent event) {
      if (event.isCancelled())
         return;
      Location location = event.getLocation();
      if (location.getWorld().getEnvironment().equals(World.Environment.NETHER))
         return;
      if (!location.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
         event.setCancelled(true);
         return;
      }
      ArrayList<Block> blocksToRemove = new ArrayList<>();
      for (Block b : event.blockList()) {
//         if (b.getLocation().getBlockY() >= 128) {
//            blocksToRemove.add(b);
//            continue;
//         }
         if (!this.plugin.getBlockWhitelist().contains(b.getType())){
            blocksToRemove.add(b);
            continue;
         }

         ArrayList<Material> blockCheckBlacklist = this.plugin.getBlockCheckBlacklist();
         if(blockCheckBlacklist.contains(b.getRelative(0, 1, 0).getType())){
            blocksToRemove.add(b);
            continue;
         }
         if(blockCheckBlacklist.contains(b.getRelative(1, 0, 0).getType())){
            blocksToRemove.add(b);
            continue;
         }
         if(blockCheckBlacklist.contains(b.getRelative(-1, 0, 0).getType())){
            blocksToRemove.add(b);
            continue;
         }
         if(blockCheckBlacklist.contains(b.getRelative(0, 0, 1).getType())){
            blocksToRemove.add(b);
            continue;
         }
         if(blockCheckBlacklist.contains(b.getRelative(0, 0, 1).getType())){
            blocksToRemove.add(b);
         }
      }
      for (Block b : blocksToRemove)
         event.blockList().remove(b);
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