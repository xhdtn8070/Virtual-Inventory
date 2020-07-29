package org.tikim.minecraft.plugin.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.tikim.minecraft.plugin.VirtualInventoryPlugin;
import org.tikim.minecraft.plugin.domain.VirtualInventory;
import org.tikim.minecraft.plugin.manager.VirtualInventoryManager;
;import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class ViCommand implements CommandExecutor {
    private VirtualInventoryPlugin virtualInventoryPlugin;
    public ViCommand(VirtualInventoryPlugin virtualInventoryPlugin) {
        this.virtualInventoryPlugin = virtualInventoryPlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        /**
         * /vi
         *  - 커맨드 자체의 설명
         */
        if(args.length==0 ){
            getInit(sender);
            return true;
        }

        /**
         * /vi info
         *  - 커맨드 자체의 설명
         */
        if(args.length==1 && args[0].equalsIgnoreCase("info")){
            getInfo(sender);
            return true;
        }

        /**
         * /vi opt
         *  - 현재 적용되어 있는 옵션 보여줌
         */
        if(args.length==1 && args[0].equalsIgnoreCase("opt")){
            getOpt(sender);
            return true;
        }

        /**
         * /vi {help or h}
         *  - 명령어 소개
         */
        if(args.length==1 && (args[0].equalsIgnoreCase("h")
                || args[0].equalsIgnoreCase("help"))){
            getHelp(sender);
            return true;
        }

        /**
         * /vi {list or n}
         *  - 자신의 창고 리스트를 확인할 수 있음
         */
        if(args.length==1 && (args[0].equalsIgnoreCase("l")
                || args[0].equalsIgnoreCase("list"))){
            getMyList(sender);
            return true;
        }

        /**
         * /vi {name or n} {#displayName}
         *  - displayName에 해당하는 실제 유저의 Name을 알 수 있음
         *  - 온라인 유저만 가능
         */
        if(args.length==2 && (args[0].equalsIgnoreCase("n")
                || args[0].equalsIgnoreCase("name"))){
            getName(sender,args[1]);
            return true;
        }

        /**
         * /vi {list or l} {#UserName}
         *  - 다른 사람의 가상 인벤토리 리스트를 확인할 수 있음
         *  - permission
         * 		- vc.admin
         */
        if(args.length==2 && (args[0].equalsIgnoreCase("l")
                || args[0].equalsIgnoreCase("list"))){
            getOtherList(sender,args[1]);
            return true;
        }

        /**
         * /vi {create or c} {#InventoryName}
         *  - 자신의 가상 인벤토리를 생성한다.
         */
        if(args.length==2 && (args[0].equalsIgnoreCase("c")
                || args[0].equalsIgnoreCase("create"))){
            createInventory(sender, args[1]);
            return true;
        }

        /**
         * /vi {open or o} {#InventoryName}
         *  - 자신의 {#InventoryName}이라는 가상 인벤토리를 오픈한다.
         */
        if(args.length==2 && (args[0].equalsIgnoreCase("o")
                || args[0].equalsIgnoreCase("open"))){
            getMyInventory(sender, args[1]);
            return true;
        }

        /**
         * /vi {openother or oo} {#userName} {#InventoryName}
         *  - 다른 사람의 가상 인벤토리를 오픈한다 확인할 수 있음
         *  - permission
         * 		- vc.admin
         */
        if(args.length==3 && (args[0].equalsIgnoreCase("oo")
                || args[0].equalsIgnoreCase("openother"))){
            getOtherInventory(sender, args[1],args[2]);
            return true;
        }

        /**
         * /vi {remove or rm} {#InventoryName}
         *  - 자신의 가상 인벤토리 {#InventoryName}를 삭제한다.
         */
        if(args.length==2 && (args[0].equalsIgnoreCase("rm")
                || args[0].equalsIgnoreCase("remove"))){
            removeMyInventory(sender, args[1]);
            return true;
        }

        /**
         * /vi {removeother or rmo} {#userName} {#InventoryName}
         *  - 다른 사람의 인벤토리를 삭제한다.
         *  - permission
         * 		- vc.admin
         */
        if(args.length==3 && (args[0].equalsIgnoreCase("rmo")
                || args[0].equalsIgnoreCase("removeother"))){
            removeOtherInventory(sender, args[1],args[2]);
            return true;
        }

        /**
         * /vi {save}
         *  - 자신의 창고 리스트를 확인할 수 있음
         *  - 서버 렉을 발생시키는 등의 비효율적인 현상이 예상되서 개발 단계에서 제거.
         */

        /**
         * /vi {saveAll}
         *  - 자신의 창고 리스트를 확인할 수 있음
         *  - permission
         *  	- vc.admin
         */
        if(args.length==1 && (args[0].equalsIgnoreCase("saveAll"))){
            try {
                saveAll(sender);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    private void getInit(CommandSender sender) {
        String init = this.virtualInventoryPlugin.virtualInventoryManager.getVirtualInventoryPlugin().messageConfig.getConfig().getString("init");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',init.replace("{#version}",this.virtualInventoryPlugin.virtualInventoryManager.getVirtualInventoryPlugin().getDescription().getVersion())));
    }

    private void saveAll(CommandSender sender) throws IOException {
        if(!checkPermission(sender,"vi.admin")){
            return ;
        }
        this.virtualInventoryPlugin.virtualInventoryManager.saveAll(false);
    }

    private void removeOtherInventory(CommandSender sender, String name, String inventoryName) {
        if(!checkPermission(sender,"vi.admin")){
            return ;
        }

        Player targetPlayer = Bukkit.getPlayer(name);
        if(!checkPlayer(sender,targetPlayer)){
            return;
        }
        this.virtualInventoryPlugin.virtualInventoryManager.removeInventory(targetPlayer,inventoryName);
    }

    private void removeMyInventory(CommandSender sender, String inventoryName) {
        if(isPlayer(sender)) {
            Player player = (Player) sender;
            this.virtualInventoryPlugin.virtualInventoryManager.removeInventory(player,inventoryName);
        }
    }

    public boolean checkPermission(CommandSender sender,String permission){
        if(sender.hasPermission(permission)){
            return true;
        }
        sender.sendMessage(ChatColor.GREEN+"You do not have \""+ChatColor.YELLOW+ permission + ChatColor.GREEN + "\" permission to run that command");
        return false;
    }
    public boolean checkPlayer(CommandSender sender, Player player){
        if(player==null){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',this.virtualInventoryPlugin.virtualInventoryManager.getVirtualInventoryPlugin().messageConfig.getConfig().getString("message.player.no-exists")));
            return false;
        }
        return true;
    }
    public boolean isPlayer(CommandSender sender){
        if(sender instanceof Player) {
            return true;
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',this.virtualInventoryPlugin.virtualInventoryManager.getVirtualInventoryPlugin().messageConfig.getConfig().getString("message.player.non-player")));
        return false;
    }
    private void getOtherInventory(CommandSender sender, String name, String inventoryName) {
        if(!checkPermission(sender,"vi.admin")){
            return ;
        }

        Player targetPlayer = Bukkit.getPlayer(name);
        if(!checkPlayer(sender,targetPlayer)){
            return;
        }

        if(isPlayer(sender)) {
            Player player = (Player) sender;
            VirtualInventory virtualInventory = this.virtualInventoryPlugin.virtualInventoryManager.getInventroy(targetPlayer, inventoryName);
            openInventory(player,virtualInventory,inventoryName);
        }
    }
    private void openInventory(Player player,VirtualInventory virtualInventory,String inventoryName){
        if(virtualInventory==null){
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',this.virtualInventoryPlugin.virtualInventoryManager.getVirtualInventoryPlugin().messageConfig.getConfig().getString("message.inventory.no-exists").replace("#{inventoryName}",inventoryName)));
            return;
        }
        player.openInventory(virtualInventory.getInventory());
    }
    private void getMyInventory(CommandSender sender, String inventoryName) {
        if(isPlayer(sender)) {
            Player player = (Player) sender;
            VirtualInventory virtualInventory = this.virtualInventoryPlugin.virtualInventoryManager.getInventroy(player, inventoryName);
            openInventory(player,virtualInventory,inventoryName);
        }
    }

    private void createInventory(CommandSender sender, String inventoryName) {
        if(isPlayer(sender)) {
            Player player = (Player) sender;
            VirtualInventory virtualInventory = this.virtualInventoryPlugin.virtualInventoryManager.createInventory(player, inventoryName);
            if(virtualInventory!=null){
                player.openInventory(virtualInventory.getInventory());
            }
            return;
        }
    }


    private void getMyList(CommandSender sender) {
        if(isPlayer(sender)) {
            printList(sender,this.virtualInventoryPlugin.virtualInventoryManager.getList((Player) sender));
            return;
        }
    }

    private void getOtherList(CommandSender sender, String name) {
        if(!checkPermission(sender,"vi.admin")){
            return;
        }
        Player targetPlayer = Bukkit.getPlayer(name);
        if(!checkPlayer(sender,targetPlayer)){
            return;
        }
        printList(sender,this.virtualInventoryPlugin.virtualInventoryManager.getList(targetPlayer));
    }
    private void printList(CommandSender sender,List<String> list){
        sender.sendMessage(ChatColor.YELLOW +"lists :");
        if (list==null){
            return;
        }
        for(String inventoryName : list){
            sender.sendMessage(ChatColor.GREEN+inventoryName);
        }
    }
    private void getName(CommandSender sender,String displayName) {
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        for(Player player : onlinePlayers){
            if(player.getDisplayName().equalsIgnoreCase(displayName)){
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',("&e    displayName : "+player.getDisplayName() + "\n    Name : " + player.getName())));
                return;
            }
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',this.virtualInventoryPlugin.virtualInventoryManager.getVirtualInventoryPlugin().messageConfig.getConfig().getString("message.player.no-exists-online")));

    }

    private void getInfo(CommandSender sender) {
        List<String> InfoList = this.virtualInventoryPlugin.virtualInventoryManager.getVirtualInventoryPlugin().messageConfig.getConfig().getStringList("info");
        if(InfoList==null){
            sender.sendMessage("Info is null");
        }
        for(String info: InfoList){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',info));
        }

    }

    private void getHelp(CommandSender sender) {
        List<String> helpList = this.virtualInventoryPlugin.virtualInventoryManager.getVirtualInventoryPlugin().messageConfig.getConfig().getStringList("help");
        if(helpList==null){
            sender.sendMessage("Help is null");
        }
        for(String help: helpList){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',help));
        }

    }

    private void getOpt(CommandSender sender) {
        if(!checkPermission(sender,"vi.admin")){
            return;
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',ChatColor.GREEN + "auto-save : " + ChatColor.AQUA + this.virtualInventoryPlugin.virtualInventoryManager.getVirtualInventoryPlugin().getConfig().getInt("auto-save")));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',ChatColor.GREEN + "save-messaging : " + ChatColor.AQUA + this.virtualInventoryPlugin.virtualInventoryManager.getVirtualInventoryPlugin().getConfig().getBoolean("save-messaging")));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',ChatColor.GREEN + "save-message : "+ ChatColor.AQUA + this.virtualInventoryPlugin.virtualInventoryManager.getVirtualInventoryPlugin().getConfig().getString("save-message")));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',ChatColor.GREEN + "save-messaging : " + ChatColor.AQUA + this.virtualInventoryPlugin.virtualInventoryManager.getVirtualInventoryPlugin().getConfig().getBoolean("load-messaging")));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',ChatColor.GREEN + "save-message : "+ ChatColor.AQUA + this.virtualInventoryPlugin.virtualInventoryManager.getVirtualInventoryPlugin().getConfig().getString("load-message")));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',ChatColor.GREEN + "max-inventory : " + ChatColor.AQUA + this.virtualInventoryPlugin.virtualInventoryManager.getVirtualInventoryPlugin().getConfig().getInt("max-inventory")));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',ChatColor.GREEN + "save-once-per-several-times : " + ChatColor.AQUA + this.virtualInventoryPlugin.virtualInventoryManager.getVirtualInventoryPlugin().getConfig().getInt("save-once-per-several-times")));

    }

}
