package org.tikim.minecraft.plugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.tikim.minecraft.plugin.command.ViCommand;
import org.tikim.minecraft.plugin.manager.VirtualInventoryManager;
import org.tikim.minecraft.plugin.manager.YmlDataManager;

import java.io.File;
import java.io.IOException;

public final class VirtualInventoryPlugin extends JavaPlugin {
    public YmlDataManager messageConfig;
    public VirtualInventoryManager virtualInventoryManager;
    @Override
    public void onEnable() {


        getConfig().options().configuration();
        saveDefaultConfig();
        messageConfig = new YmlDataManager(this,"message");

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',this.messageConfig.getConfig().getString("message.inventory.start")));
        this.virtualInventoryManager = new VirtualInventoryManager(this,new File(getDataFolder(), "inventory"));
        try{
            virtualInventoryManager.load();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        getCommand("vi").setExecutor(new ViCommand(this.virtualInventoryManager));
        repeatSaveVirtualInventory();



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
}
