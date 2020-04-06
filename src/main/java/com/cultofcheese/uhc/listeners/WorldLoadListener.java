package com.cultofcheese.uhc.listeners;

import com.cultofcheese.uhc.UHC;
import com.cultofcheese.uhc.managers.CacheManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class WorldLoadListener implements Listener {

    @EventHandler
    public void onWorldLoad(WorldLoadEvent e) {
        if (e.getWorld().getName().equals("uhc")) {
            //World has been loaded, now start generating chunks. Uses the scheduler to run on the next server tick.
            new BukkitRunnable(){
                @Override
                public void run() {
                    CacheManager.getGame().generateChunks(0);
                }
            }.runTask(UHC.get());
        }
    }

}
