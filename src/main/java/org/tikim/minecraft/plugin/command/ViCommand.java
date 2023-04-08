package org.tikim.minecraft.plugin.command;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;
import org.tikim.minecraft.plugin.VirtualInventoryPlugin;
import org.tikim.minecraft.plugin.domain.VirtualInventory;
import org.tikim.minecraft.plugin.manager.VirtualInventoryManager;
;import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class ViCommand extends SimpleCommand {

    private VirtualInventoryPlugin virtualInventoryPlugin;
    public ViCommand() {
        super("vi");
        virtualInventoryPlugin = (VirtualInventoryPlugin)VirtualInventoryPlugin.getInstance();
    }

    @Override
    protected void onCommand() {
        CommandSender sender = getSender();
        /**
         * /vi
         *  - 커맨드 자체의 설명
         */
        if(args.length==0 ){
            getInit(sender);
        }

        /**
         * /vi info
         *  - 커맨드 자체의 설명
         */
        if(args.length==1 && args[0].equalsIgnoreCase("info")){
            getInfo(sender);
        }

        /**
         * /vi opt
         *  - 현재 적용되어 있는 옵션 보여줌
         */
        if(args.length==1 && args[0].equalsIgnoreCase("opt")){
            getOpt(sender);
        }

        /**
         * /vi {help or h}
         *  - 명령어 소개
         */
        if(args.length==1 && (args[0].equalsIgnoreCase("h")
                || args[0].equalsIgnoreCase("help"))){
            getHelp(sender);
        }

        /**
         * /vi {list or n}
         *  - 자신의 창고 리스트를 확인할 수 있음
         */
        if(args.length==1 && (args[0].equalsIgnoreCase("l")
                || args[0].equalsIgnoreCase("list"))){
            getMyList(sender);
        }

        /**
         * /vi {name or n} {#displayName}
         *  - displayName에 해당하는 실제 유저의 Name을 알 수 있음
         *  - 온라인 유저만 가능
         */
        if(args.length==2 && (args[0].equalsIgnoreCase("n")
                || args[0].equalsIgnoreCase("name"))){
            getName(sender,args[1]);
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
        }

        /**
         * /vi {create or c} {#InventoryName}
         *  - 자신의 가상 인벤토리를 생성한다.
         */
        if(args.length==2 && (args[0].equalsIgnoreCase("c")
                || args[0].equalsIgnoreCase("create"))){
            createInventory(sender, args[1]);
        }

        /**
         * /vi {open or o} {#InventoryName}
         *  - 자신의 {#InventoryName}이라는 가상 인벤토리를 오픈한다.
         */
        if(args.length==2 && (args[0].equalsIgnoreCase("o")
                || args[0].equalsIgnoreCase("open"))){
            getMyInventory(sender, args[1]);
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
        }

        /**
         * /vi {remove or rm} {#InventoryName}
         *  - 자신의 가상 인벤토리 {#InventoryName}를 삭제한다.
         */
        if(args.length==2 && (args[0].equalsIgnoreCase("rm")
                || args[0].equalsIgnoreCase("remove"))){
            removeMyInventory(sender, args[1]);
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
        }
    }

    @Override
    protected List<String> tabComplete() {
        /**
         * /vi
         *  - 커맨드 자체의 설명
         */
        List<String> result=new ArrayList<>();

        if(args.length==1 ){
            for(String string :this.virtualInventoryPlugin.tabConfig.getTabs()){
                if(string.toLowerCase().startsWith(args[0])){
                    result.add(string);
                }
            }
            return result;
        }


        /**
         * /vi {name or n} {#displayName}
         *  - displayName에 해당하는 실제 유저의 Name을 알 수 있음
         *  - 온라인 유저만 가능
         */
        if(args.length==2 && (args[0].equalsIgnoreCase("n")
                || args[0].equalsIgnoreCase("name"))){
            for(Player player :Bukkit.getOnlinePlayers()){
                if(player.getDisplayName().startsWith(args[1])){
                    result.add(player.getDisplayName());
                }
            }
            return result;
        }

        /**
         * /vi {list or l} {#UserName}
         *  - 다른 사람의 가상 인벤토리 리스트를 확인할 수 있음
         *  - permission
         * 		- vc.admin
         */
        if(args.length==2 && (args[0].equalsIgnoreCase("l")
                || args[0].equalsIgnoreCase("list"))){
            if(!this.virtualInventoryPlugin.viCommand.checkPermission(sender,"vi.admin")){
                return result;
            }
            for(Player player :Bukkit.getOnlinePlayers()){
                if(player.getName().startsWith(args[1])){
                    result.add(player.getName());
                }
            }
            return result;
        }

        /**
         * /vi {create or c} {#InventoryName}
         *  - 자신의 가상 인벤토리를 생성한다.
         */
        if(args.length==2 && (args[0].equalsIgnoreCase("c")
                || args[0].equalsIgnoreCase("create"))){
            return result;
        }

        /**
         * /vi {open or o} {#InventoryName}
         *  - 자신의 {#InventoryName}이라는 가상 인벤토리를 오픈한다.
         */
        if(args.length==2 && (args[0].equalsIgnoreCase("o")
                || args[0].equalsIgnoreCase("open"))){
            if(this.virtualInventoryPlugin.viCommand.isPlayer(sender)) {
                Player player = (Player) sender;
                for(String string :this.virtualInventoryPlugin.virtualInventoryManager.getList(player)){
                    if(string.startsWith(args[1])){
                        result.add(string);
                    }
                }
                return result;
            }
            return result;
        }

        /**
         * /vi {openother or oo} {#userName} {#InventoryName}
         *  - 다른 사람의 가상 인벤토리를 오픈한다 확인할 수 있음
         *  - permission
         * 		- vc.admin
         */
        if(args.length==2 && (args[0].equalsIgnoreCase("oo")
                || args[0].equalsIgnoreCase("openother"))){
            if(!this.virtualInventoryPlugin.viCommand.checkPermission(sender,"vi.admin")){
                return result;
            }

            for(Player player :Bukkit.getOnlinePlayers()){
                if(player.getName().startsWith(args[1])){
                    result.add(player.getName());
                }
            }
            return result;
        }
        if(args.length==3 && (args[0].equalsIgnoreCase("oo")
                || args[0].equalsIgnoreCase("openother"))){
            if(!this.virtualInventoryPlugin.viCommand.checkPermission(sender,"vi.admin")) {
                return result;
            }

            if(this.virtualInventoryPlugin.viCommand.isPlayer(sender)) {

                Player targetPlayer = Bukkit.getPlayer(args[1]);
                if(!this.virtualInventoryPlugin.viCommand.checkPlayer(sender,targetPlayer)){
                    return result;
                }
                for(String string :this.virtualInventoryPlugin.virtualInventoryManager.getList(targetPlayer)){
                    if(string.startsWith(args[2])){
                        result.add(string);
                    }
                }
                return result;
            }
            return result;
        }
        /**
         * /vi {remove or rm} {#InventoryName}
         *  - 자신의 가상 인벤토리 {#InventoryName}를 삭제한다.
         */
        if(args.length==2 && (args[0].equalsIgnoreCase("rm")
                || args[0].equalsIgnoreCase("remove"))){
            if(this.virtualInventoryPlugin.viCommand.isPlayer(sender)) {
                Player player = (Player) sender;
                for(String string :this.virtualInventoryPlugin.virtualInventoryManager.getList(player)){
                    if(string.startsWith(args[1])){
                        result.add(string);
                    }
                }
                return result;
            }
            return result;
        }

        /**
         * /vi {removeother or rmo} {#userName} {#InventoryName}
         *  - 다른 사람의 인벤토리를 삭제한다.
         *  - permission
         * 		- vc.admin
         */
        if(args.length==2 && (args[0].equalsIgnoreCase("rmo")
                || args[0].equalsIgnoreCase("removeother"))){
            if(!this.virtualInventoryPlugin.viCommand.checkPermission(sender,"vi.admin")){
                return result;
            }

            for(Player player :Bukkit.getOnlinePlayers()){
                if(player.getName().startsWith(args[1])){
                    result.add(player.getName());
                }
            }
            return result;
        }

        if(args.length==3 && (args[0].equalsIgnoreCase("rmo")
                || args[0].equalsIgnoreCase("removeother"))){
            if(!this.virtualInventoryPlugin.viCommand.checkPermission(sender,"vi.admin")) {
                return result;
            }

            if(this.virtualInventoryPlugin.viCommand.isPlayer(sender)) {

                Player targetPlayer = Bukkit.getPlayer(args[2]);
                if(!this.virtualInventoryPlugin.viCommand.checkPlayer(sender,targetPlayer)){
                    return result;
                }
                for(String string :this.virtualInventoryPlugin.virtualInventoryManager.getList(targetPlayer)){
                    if(string.startsWith(args[2])){
                        result.add(string);
                    }
                }
                return result;
            }
            return result;
        }
        return result;
    }

    private void getInit(CommandSender sender) {
        String init = virtualInventoryPlugin.messageConfig.getInit();
        Common.tell(sender, ChatColor.translateAlternateColorCodes('&',init.replace("{#version}",this.virtualInventoryPlugin.virtualInventoryManager.getVirtualInventoryPlugin().getDescription().getVersion())));
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
        String message = this.virtualInventoryPlugin.virtualInventoryManager.getVirtualInventoryPlugin().messageConfig.getPlayer().getNoAllowedPermission();
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',message.replace("#{permission}",permission)));
        return false;
    }
    public boolean checkPlayer(CommandSender sender, Player player){
        if(player==null){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',this.virtualInventoryPlugin.virtualInventoryManager.getVirtualInventoryPlugin().messageConfig.getPlayer().getNoExists()));
            return false;
        }
        return true;
    }
    public boolean isPlayer(CommandSender sender){
        if(sender instanceof Player) {
            return true;
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',this.virtualInventoryPlugin.virtualInventoryManager.getVirtualInventoryPlugin().messageConfig.getPlayer().getNonPlayer()));
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
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',this.virtualInventoryPlugin.virtualInventoryManager.getVirtualInventoryPlugin().messageConfig.getInventory().getNoExists().replace("#{inventoryName}",inventoryName)));
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
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',this.virtualInventoryPlugin.virtualInventoryManager.getVirtualInventoryPlugin().messageConfig.getPlayer().getNoExistsOnline()));

    }

    private void getInfo(CommandSender sender) {
        List<String> InfoList = this.virtualInventoryPlugin.virtualInventoryManager.getVirtualInventoryPlugin().messageConfig.getInfo();
        if(InfoList==null){
            sender.sendMessage("Info is null");
        }
        for(String info: InfoList){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',info));
        }

    }

    private void getHelp(CommandSender sender) {
        List<String> helpList = this.virtualInventoryPlugin.virtualInventoryManager.getVirtualInventoryPlugin().messageConfig.getHelp();
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
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',ChatColor.GREEN + "auto-save : " + ChatColor.AQUA + this.virtualInventoryPlugin.pluginConfig.isAutoSave()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',ChatColor.GREEN + "auto-save-time : " + ChatColor.AQUA + this.virtualInventoryPlugin.pluginConfig.getAutoSaveTime()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',ChatColor.GREEN + "save-messaging : " + ChatColor.AQUA + this.virtualInventoryPlugin.pluginConfig.isSaveMessaging()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',ChatColor.GREEN + "save-message : "+ ChatColor.AQUA + this.virtualInventoryPlugin.pluginConfig.getSaveMessage()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',ChatColor.GREEN + "save-messaging : " + ChatColor.AQUA + this.virtualInventoryPlugin.pluginConfig.isLoadMessaging()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',ChatColor.GREEN + "save-message : "+ ChatColor.AQUA + this.virtualInventoryPlugin.pluginConfig.getLoadMessage()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',ChatColor.GREEN + "max-inventory : " + ChatColor.AQUA + this.virtualInventoryPlugin.pluginConfig.getMaxInventory()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',ChatColor.GREEN + "save-once-per-several-times : " + ChatColor.AQUA + this.virtualInventoryPlugin.pluginConfig.getSaveOncePerSeveralTimes()));

    }

}
