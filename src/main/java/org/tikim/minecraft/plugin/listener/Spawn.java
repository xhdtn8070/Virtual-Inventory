package org.tikim.minecraft.plugin.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.tikim.minecraft.plugin.VirtualInventoryPlugin;

import java.util.HashMap;
import java.util.logging.Logger;

public class Spawn implements Listener {
    VirtualInventoryPlugin plugin;
    Logger logger;
    HashMap<String,Boolean> mark;

    public Spawn(VirtualInventoryPlugin plugin,Logger logger) {
        this.plugin = plugin;
        this.logger = logger;
        this.mark = new HashMap<>();
    }

    @EventHandler
    public void playerRespawnEvent(PlayerRespawnEvent event) {

        logger.info("플레이어 리스폰 시작");
        logger.info("체력 : " + event.getPlayer().getHealth());
        mark.put(event.getPlayer().getUniqueId().toString(), true);
        logger.info("플레이어 위치 : "  + event.getPlayer().getLocation());
//		logger.info("플레이어 위치 : "  + event.
        logger.info("플렝이어 리스폰 위치 : "  + event.getPlayer().getBedSpawnLocation());
        logger.info("이벤트 플렝이어 리스폰 위치 : "  + event.getRespawnLocation());
        logger.info("월드 스파운 로케이션"+event.getPlayer().getWorld().getSpawnLocation());
		event.setRespawnLocation(event.getPlayer().getLocation());
//		event.getPlayer().setBedSpawnLocation(event.getPlayer().getLocation());
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                logger.info("하이요");
                event.getPlayer().teleport(event.getPlayer().getWorld().getSpawnLocation());
                // TODO Auto-generated method stub
                logger.info("바이요");
            }
        }.runTaskLater(plugin, 100);
        logger.info("플레이어 리스폰 끝");
    }
}
