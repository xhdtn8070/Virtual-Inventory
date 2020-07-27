package org.tikim.minecraft.plugin.manager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.tikim.minecraft.plugin.VirtualInventoryPlugin;

import java.io.*;
import java.util.logging.Level;

/**
 * https://bukkit.gamepedia.com/Configuration_API_Reference#Arbitrary_Configurations
 * modify by tikim
 */
public class YmlDataManager {
    private VirtualInventoryPlugin plugin;
    private FileConfiguration dataConfig;
    private File configFile;
    private String fileName;
    public YmlDataManager(VirtualInventoryPlugin virtualInventoryPlugin,String fileName) {
        this.plugin = virtualInventoryPlugin;
        this.fileName = fileName+".yml";
        this.dataConfig = null;
        this.configFile = null;
        saveDefaultConfig();
    }
    public void reloadConfig() throws UnsupportedEncodingException {
        if(this.configFile == null){
            this.configFile = new File(this.plugin.getDataFolder(),fileName);
        }
        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);

        Reader defConfigStream = new InputStreamReader(this.plugin.getResource(fileName), "UTF8");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            dataConfig.setDefaults(defConfig);
        }
    }
    public FileConfiguration getConfig(){
        try{
            if(this.dataConfig == null){
                reloadConfig();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  this.dataConfig;
    }
    public void saveConfig(){
        if(this.dataConfig==null || this.configFile == null){
            return;
        }
        try {
            this.getConfig().save(configFile);
        } catch (IOException ex) {
            this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + fileName, ex);
        }
    }
    public void saveDefaultConfig() {
        if (configFile == null) {
            this.configFile = new File(this.plugin.getDataFolder(), fileName);
        }
        if (!configFile.exists()) {
            this.plugin.saveResource(fileName, false);
        }
    }
}
