package org.tikim.minecraft.plugin;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.CharSequenceReader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.tikim.minecraft.plugin.command.ViCommand;
import org.tikim.minecraft.plugin.listener.Spawn;
import org.tikim.minecraft.plugin.manager.VirtualInventoryManager;
import org.tikim.minecraft.plugin.manager.YmlDataManager;
import org.tikim.minecraft.plugin.tab.ViTab;

import java.io.*;

public final class VirtualInventoryPlugin extends JavaPlugin {
    public YmlDataManager messageConfig;
    public VirtualInventoryManager virtualInventoryManager;
    public FileConfiguration tabConfiguration;
    public ViCommand viCommand;
    public ViTab viTab;
    @Override
    public void onEnable() {


        getConfig().options().configuration();
        saveDefaultConfig();

        messageConfig = new YmlDataManager(this,"message");
        try {
            tabConfiguration = YamlConfiguration.loadConfiguration(getReaderFromStream(this.getResource("tab.yml")));
        } catch (IOException e) {
            e.printStackTrace();
        }


        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',this.messageConfig.getConfig().getString("message.inventory.start")));
        this.virtualInventoryManager = new VirtualInventoryManager(this,new File(getDataFolder(), "inventory"));
        try{
            virtualInventoryManager.load();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        viCommand = new ViCommand(this);
        viTab = new ViTab(this);
        this.getCommand("vi").setExecutor(viCommand);
        this.getCommand("vi").setTabCompleter(viTab);
        repeatSaveVirtualInventory();
        this.getServer().getPluginManager().registerEvents(new Spawn(this,this.getLogger()),this);


    }
    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',this.messageConfig.getConfig().getString("message.inventory.stop")));
        try {
            this.virtualInventoryManager.saveAll(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void repeatSaveVirtualInventory(){
        int second = this.getConfig().getInt("auto-save");
        if(second!=0){
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                @Override
                public void run() {
                    try {
                        virtualInventoryManager.saveAll(false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, 0L, second*20L); //0 Tick initial delay, 20 Tick (1 Second) between repeats

        }
    }

    public Reader getReaderFromStream(InputStream initialStream)
            throws IOException {

        byte[] buffer = IOUtils.toByteArray(initialStream);

        Reader targetReader = new CharSequenceReader(new String(buffer));
        return targetReader;
    }
}
