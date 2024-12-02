package com.johnymuffin.beta.tntcontrol.config;

import org.bukkit.util.config.Configuration;

import java.io.File;

public class ExplosionControlSettings extends Configuration {

   public ExplosionControlSettings(File settingsFile) {
      super(settingsFile);
      this.reload();
   }

   private void write() {
      // Main
      generateConfigOption("config-version", 1);

      // TNT Chaining
      generateConfigOption("settings.tnt-chaining.enabled", true);
      generateConfigOption("settings.tnt-chaining.maximum", 20);
      generateConfigOption("settings.tnt-chaining.duration-milliseconds", 1000);

      // TNT Behaviors
      generateConfigOption("settings.tnt-behavior.cancel-entire-explosion-if-non-whitelisted.enabled", true);
      generateConfigOption("settings.tnt-behavior.use-tnt-whitelist.enabled", true);
      generateConfigOption("settings.tnt-behavior.ignore-single-if-block-pop-off.enabled", true);
   }


   public void generateConfigOption(String key, Object defaultValue) {
      if (this.getProperty(key) == null) {
         this.setProperty(key, defaultValue);
      }
      final Object value = this.getProperty(key);
      this.removeProperty(key);
      this.setProperty(key, value);
   }


   //Getters Start
   public Object getConfigOption(String key) {
      return this.getProperty(key);
   }

   public String getConfigString(String key) {
      return String.valueOf(getConfigOption(key));
   }

   public Integer getConfigInteger(String key) {
      return Integer.valueOf(getConfigString(key));
   }

   public Long getConfigLong(String key) {
      return Long.valueOf(getConfigString(key));
   }

   public Double getConfigDouble(String key) {
      return Double.valueOf(getConfigString(key));
   }

   public Boolean getConfigBoolean(String key) {
      return Boolean.valueOf(getConfigString(key));
   }
   //Getters End


   public Long getConfigLongOption(String key) {
      if (this.getConfigOption(key) == null) {
         return null;
      }
      return Long.valueOf(String.valueOf(this.getProperty(key)));
   }


   private boolean convertToNewAddress(String newKey, String oldKey) {
      if (this.getString(newKey) != null) {
         return false;
      }
      if (this.getString(oldKey) == null) {
         return false;
      }
      System.out.println("Converting Config: " + oldKey + " to " + newKey);
      Object value = this.getProperty(oldKey);
      this.setProperty(newKey, value);
      this.removeProperty(oldKey);
      return true;
   }


   private void reload() {
      this.load();
      this.write();
      this.save();
   }
}
