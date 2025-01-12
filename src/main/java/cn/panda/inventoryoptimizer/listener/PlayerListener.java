package cn.panda.inventoryoptimizer.listener;

import cn.panda.inventoryoptimizer.InventoryOptimizer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final InventoryOptimizer plugin;

    public PlayerListener(InventoryOptimizer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.getDataManager().loadPlayerData(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getDataManager().unloadPlayerData(event.getPlayer().getUniqueId());
    }
}