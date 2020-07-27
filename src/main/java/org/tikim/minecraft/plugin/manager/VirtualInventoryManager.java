package org.tikim.minecraft.plugin.manager;

import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.tikim.minecraft.plugin.VirtualInventoryPlugin;
import org.tikim.minecraft.plugin.domain.VirtualInventory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class VirtualInventoryManager {
    private final VirtualInventoryPlugin virtualInventoryPlugin;
    private final HashMap<String, VirtualInventory> inventories;
    private final HashMap<String, List<String>> list;
    private final File folder;
    public VirtualInventoryManager(VirtualInventoryPlugin virtualInventoryPlugin,File folder) {
        this.virtualInventoryPlugin = virtualInventoryPlugin;
        this.folder = folder;
        this.inventories = new HashMap<>();
        this.list = new HashMap<>();
        if(!folder.exists()){
            folder.mkdir();
            Bukkit.broadcastMessage("mkdir " + folder.getAbsolutePath());
        }
    }

    public VirtualInventoryPlugin getVirtualInventoryPlugin() {
        return virtualInventoryPlugin;
    }

    public List<String> getList(Player player){
        return this.list.get(player.getName().toLowerCase());
    }

    public VirtualInventory getInventroy(Player player, String inventoryName){
        //player : player
        //name : 1 or chestName
        String playerName = player.getName().toLowerCase();
        String json = playerName+"."+inventoryName;
        VirtualInventory inventory = this.inventories.get(json);
        if(inventory==null||inventory.isDelete()){
            return null;
        }
        inventory.setModify(true);
        return inventory;

    }
    public VirtualInventory createInventory(Player player, String inventoryName){

        VirtualInventory inventory = getInventroy(player, inventoryName);
        if(inventory==null||inventory.isDelete()){
            if(getMaxInventory()<getAmountInventory(player)){

                player.sendMessage(ChatColor.translateAlternateColorCodes('&',this.virtualInventoryPlugin.messageConfig.getConfig().getString("message.inventory.create-error")));
                return null;
            }
            String playerName = player.getName().toLowerCase();
            String json = playerName+"."+inventoryName;
            List<String> stringList = this.list.get(playerName);
            if(stringList==null){
                stringList = new ArrayList<>();
            }
            stringList.add(inventoryName);
            this.list.put(playerName,stringList);
            String title = this.virtualInventoryPlugin.messageConfig.getConfig().getString("message.inventory.title");
            title = title.replace("#{playerName}",playerName).replace("#{inventoryName}",inventoryName);
            inventory = new VirtualInventory(Bukkit.createInventory(player, 54,ChatColor.translateAlternateColorCodes('&',title)),true);
            inventories.put(json,inventory);
            return inventory;
        }
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',this.virtualInventoryPlugin.messageConfig.getConfig().getString("message.inventory.already-exists")));
        return inventory;
    }
    public int getAmountInventory(Player player){
        List<String> list = this.list.get(player.getName().toLowerCase());
        if(list==null){
            return 0;
        }
        return list.size();
    }
    public int getMaxInventory(){
        return this.virtualInventoryPlugin.getConfig().getInt("max-inventory");
    }

    public void load() throws IOException, ClassNotFoundException {
        boolean loadMessaging = this.virtualInventoryPlugin.getConfig().getBoolean("load-messaging");
        String loadMessage = this.virtualInventoryPlugin.getConfig().getString("load-message");

        for(File file :this.folder.listFiles()){
            if(file.getName().endsWith("txt")){
                /*
                file.getName() ex) KOO_MA.1.json
                 */
                if(loadMessaging){
                    String message = loadMessage.replace("#{fileName}",file.getName());
                    Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',message));
                }

                String[] property = file.getName().split("\\.");
                List<String> stringList = this.list.get(property[0]);
                if(stringList==null){
                    stringList = new ArrayList<>();
                }
                stringList.add(property[1]);
                this.list.put(property[0], stringList);

                FileInputStream fileInputStream = new FileInputStream(file);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                ItemStack[] itemStacks = new ItemStack[54];
                String reader="";
                for(int i = 0; i < 54; i++){
                    reader = bufferedReader.readLine();

                    if((reader) ==null){
                        break;
                    }
                    itemStacks[i] = deserialize(reader);
                }
                bufferedReader.close();
                fileInputStream.close();
                String title = this.virtualInventoryPlugin.messageConfig.getConfig().getString("message.inventory.title");
                title = title.replace("#{playerName}",property[0]).replace("#{inventoryName}",property[1]);
                Inventory inventory = Bukkit.createInventory(Bukkit.getPlayer(property[0]), 54, ChatColor.translateAlternateColorCodes('&',title));
                inventory.setContents(itemStacks);
                this.inventories.put(property[0]+"."+property[1],new VirtualInventory(inventory,true));
            }
        }
    }

    public byte[] serialize(ItemStack itemStack) throws IOException {
        if(itemStack==null) return null;
        Map<String, Object> serialize = itemStack.serialize();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BukkitObjectOutputStream oos = new BukkitObjectOutputStream(baos);
        oos.writeObject(serialize);
        oos.close();
        baos.close();
        return Base64.encodeBase64(baos.toByteArray());
    }
    public ItemStack deserialize(String data) throws IOException, ClassNotFoundException {
        if(data.equalsIgnoreCase("")){
            return null;
        }
        try{
            ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decodeBase64(data));
            BukkitObjectInputStream ois = new BukkitObjectInputStream(bais);

            Map<String, Object> serialize = (Map<String, Object>) ois.readObject();
            ois.close();
            bais.close();
            return ItemStack.deserialize(serialize);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }

    public void saveFile(File file, VirtualInventory virtualInventory, boolean force) throws IOException {
        if(file.exists()&&virtualInventory.isDelete()){
            file.delete();
            return;
        }
        /**
         * 강제로 창고의 정보를 전부 저장하게 함.
         * 서버가 종료될 때, force가 true로 입력된다.
         */
        if((!force)&&!virtualInventory.isModify()){
            /**
             * 창고의 정보가 일부 누락될 수 있기 때문에
             * 변경점이 발견되지 않아도 저장 n번당 한번씩 저장한다.
             */
            if(virtualInventory.getCount()>=this.virtualInventoryPlugin.getConfig().getInt("save-once-per-several-times")){
                virtualInventory.setModify(true);
            }else{
                virtualInventory.setCount(virtualInventory.getCount()+1);
            }
            return;
        }
        virtualInventory.setCount(1);
        virtualInventory.setModify(false);
        FileOutputStream fos = new FileOutputStream(file);

        for(ItemStack itemStack :virtualInventory.getInventory().getContents()){
            byte[] serializeByte = serialize(itemStack);
            if(serializeByte!=null){
                fos.write(serializeByte);
            }
            fos.write("\n".getBytes());
        }
        fos.close();
    }
    public void saveAll(boolean force) throws IOException {

        for (Map.Entry<String, VirtualInventory> entry : this.inventories.entrySet()) {
            VirtualInventory virtualInventory = entry.getValue();
            File file = new File(folder.getPath()+"/"+entry.getKey()+".txt");

            saveFile(file,virtualInventory,force);
        }
        if(this.virtualInventoryPlugin.getConfig().getBoolean("save-messaging")){
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',this.virtualInventoryPlugin.getConfig().getString("save-message")));
        }else{
            Bukkit.getConsoleSender().sendMessage((ChatColor.translateAlternateColorCodes('&',this.virtualInventoryPlugin.getConfig().getString("save-message"))));
        }

    }

    public void removeInventory(Player player, String inventoryName) {

        List<String> list = getList(player);
        if(list==null){
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',this.virtualInventoryPlugin.messageConfig.getConfig().getString("message.inventory.no-exists")));
            return;
        }
        int index = -1;
        for(int i = 0; i <list.size(); i++){
            if(list.get(i).equals(inventoryName)){
                index = i;
                list.remove(i);
                break;
            }
        }
        if(index==-1){
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',this.virtualInventoryPlugin.messageConfig.getConfig().getString("message.inventory.no-exists")));
            return;
        }
        VirtualInventory virtualInventory = getInventroy(player, inventoryName);
        virtualInventory.setDelete(true);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',this.virtualInventoryPlugin.messageConfig.getConfig().getString("message.inventory.remove")));
    }
}
