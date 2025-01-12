package cn.panda.inventoryoptimizer;

import cn.panda.inventoryoptimizer.data.DataManager;
import cn.panda.inventoryoptimizer.listener.InventoryListener;
import cn.panda.inventoryoptimizer.listener.PlayerListener;
import cn.panda.inventoryoptimizer.task.LearningNotificationTask;
import cn.panda.inventoryoptimizer.command.OptimizeCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class InventoryOptimizer extends JavaPlugin {

    private DataManager dataManager;
    private static InventoryOptimizer instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig(); // 保存默认配置

        dataManager = new DataManager(this);

        // 注册事件监听器
        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        // 注册命令
        getCommand("optimize").setExecutor(new OptimizeCommand(this));

        // 启动定时任务，检查是否可以通知玩家
        startLearningNotificationTask();

        getLogger().info("InventoryOptimizer 插件已启用!");
    }

    @Override
    public void onDisable() {
        getLogger().info("InventoryOptimizer 插件已禁用!");
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public static InventoryOptimizer getInstance() {
        return instance;
    }

    private void startLearningNotificationTask() {
        long delay = getConfig().getLong("learning_check_interval", 300L) * 20L; // 默认 5 分钟检查一次
        new LearningNotificationTask(this).runTaskTimerAsynchronously(this, delay, delay);
    }
}