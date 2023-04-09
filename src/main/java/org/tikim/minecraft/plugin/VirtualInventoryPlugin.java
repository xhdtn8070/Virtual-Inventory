package org.tikim.minecraft.plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.CharSequenceReader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.tikim.minecraft.plugin.command.ViCommand;
import org.tikim.minecraft.plugin.config.MessageConfig;
import org.tikim.minecraft.plugin.config.PluginConfig;
import org.tikim.minecraft.plugin.config.TabConfig;
import org.tikim.minecraft.plugin.listener.Spawn;
import org.tikim.minecraft.plugin.manager.VirtualInventoryManager;

public final class VirtualInventoryPlugin extends SimplePlugin {

    public PluginConfig pluginConfig;

    public MessageConfig messageConfig;

    public TabConfig tabConfig;

    public VirtualInventoryManager virtualInventoryManager;
    public ViCommand viCommand;

    @Override
    protected void onPluginStart() {
        Common.log(Common.consoleLine());
        Common.log(ChatColor.GREEN + "VirtualInventory Enabling....");
        Common.log(ChatColor.GREEN + "This is a private library developed by Tony for projects");
        Common.log(ChatColor.RED + "You are not allowed to redistribute or claim this resource as your own product.");
        Common.log(Common.consoleLine());

        pluginConfig = PluginConfig.getInstance();
        messageConfig = MessageConfig.getInstance();
        tabConfig = TabConfig.getInstance();

        Common.log(this.messageConfig.getInventory().getStart());

        this.virtualInventoryManager = new VirtualInventoryManager(this,new File(getDataFolder(), "inventory"));
        try{
            virtualInventoryManager.load();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        registerCommand(new ViCommand());
        repeatSaveVirtualInventory();
//        this.getServer().getPluginManager().registerEvents(new Spawn(this,this.getLogger()),this);


    }
    @Override
    protected void onPluginStop() {
        Common.log(this.messageConfig.getInventory().getStop());
        try {
            this.virtualInventoryManager.saveAll(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPluginLoad() {

    }

    @Override
    protected void onPluginReload() {

    }

    private void repeatSaveVirtualInventory(){
        boolean autoSave = this.pluginConfig.isAutoSave();
        int second = this.pluginConfig.getAutoSaveTime();
        if(autoSave && second!=0){
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

    /**
     * Return the instance of this plugin, which simply refers to a static field already created for you in SimplePlugin but casts it to your specific plugin instance for your convenience.
     *
     * @return
     */
    public static SimplePlugin getInstance() {
        return (VirtualInventoryPlugin) SimplePlugin.getInstance();
    }
}
