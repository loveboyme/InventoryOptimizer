package cn.panda.inventoryoptimizer.data;

import cn.panda.inventoryoptimizer.InventoryOptimizer;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigurationManager {

    private final InventoryOptimizer plugin;

    public ConfigurationManager(InventoryOptimizer plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig(); // 确保默认配置存在
    }

    public FileConfiguration getConfig() {
        return plugin.getConfig();
    }

    public int getPopulationSize() {
        return getConfig().getInt("genetic_algorithm.population_size", 50);
    }

    public double getMutationRate() {
        return getConfig().getDouble("genetic_algorithm.mutation_rate", 0.05);
    }

    public int getEliteSize() {
        return getConfig().getInt("genetic_algorithm.elite_size", 5);
    }

    public int getLearningThreshold() {
        return getConfig().getInt("learning_threshold", 200); // 达到多少次交互认为可以开始优化
    }
}