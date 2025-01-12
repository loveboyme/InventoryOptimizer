package cn.panda.inventoryoptimizer.task;

import cn.panda.inventoryoptimizer.InventoryOptimizer;
import cn.panda.inventoryoptimizer.data.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LearningNotificationTask extends BukkitRunnable {

    private final InventoryOptimizer plugin;

    public LearningNotificationTask(InventoryOptimizer plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (!plugin.isEnabled()) {
            cancel();
            return;
        }

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            PlayerData playerData = plugin.getDataManager().getPlayerData(player.getUniqueId());
            if (playerData != null && !playerData.isLearningComplete() &&
                    playerData.getItemUsageCount().values().stream().mapToInt(Integer::intValue).sum() >= plugin.getConfig().getInt("learning_threshold", 200)) {
                playerData.setLearningComplete(true);
                plugin.getDataManager().savePlayerData(playerData);
                player.sendMessage("§a[背包优化] 学习完成！现在可以使用 /optimize 命令来优化你的背包了。");
            }
        }
    }
}