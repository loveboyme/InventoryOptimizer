package cn.panda.inventoryoptimizer.command;

import cn.panda.inventoryoptimizer.InventoryOptimizer;
import cn.panda.inventoryoptimizer.algorithm.GeneticAlgorithm;
import cn.panda.inventoryoptimizer.algorithm.Individual;
import cn.panda.inventoryoptimizer.data.PlayerData;
import cn.panda.inventoryoptimizer.util.ItemStackUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class OptimizeCommand implements CommandExecutor {

    private final InventoryOptimizer plugin;
    private final int generations = 50; // 遗传算法迭代次数

    public OptimizeCommand(InventoryOptimizer plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("此命令只能由玩家执行。");
            return true;
        }

        Player player = (Player) sender;
        PlayerData playerData = plugin.getDataManager().getPlayerData(player.getUniqueId());

        if (playerData == null || !playerData.isLearningComplete()) {
            player.sendMessage("§c[背包优化] 插件仍在学习你的使用习惯，请稍后再试。");
            return true;
        }

        player.sendMessage("§a[背包优化] 正在努力优化你的背包...");

        // 获取遗传算法参数
        int populationSize = plugin.getConfig().getInt("genetic_algorithm.population_size", 50);
        double mutationRate = plugin.getConfig().getDouble("genetic_algorithm.mutation_rate", 0.05);
        int eliteSize = plugin.getConfig().getInt("genetic_algorithm.elite_size", 5);

        GeneticAlgorithm ga = new GeneticAlgorithm(populationSize, mutationRate, eliteSize);
        List<Individual> population = ga.initPopulation(player);

        // 运行遗传算法
        for (int i = 0; i < generations; i++) {
            population = ga.evolvePopulation(player, population);
        }

        // 应用最佳的背包排列
        Inventory optimizedInventory = ga.getBestInventory(population);

        // 获取玩家当前背包的物品快照
        ItemStack[] currentContentBefore = player.getInventory().getContents().clone();

        // 更安全地应用优化后的背包
        ItemStack[] optimizedContents = optimizedInventory.getContents();
        ItemStack[] playerContents = player.getInventory().getContents();

        // 逐个设置物品，只在必要时修改
        for (int i = 0; i < playerContents.length; i++) {
            ItemStack optimizedItem = (i < optimizedContents.length) ? optimizedContents[i] : null;
            ItemStack playerItem = playerContents[i];

            boolean optimizedIsNull = optimizedItem == null || optimizedItem.getType() == Material.AIR;
            boolean playerIsNull = playerItem == null || playerItem.getType() == Material.AIR;

            if (!ItemStackUtil.areEqual(playerItem, optimizedItem)) {
                player.getInventory().setItem(i, optimizedItem == null ? null : optimizedItem.clone());
            }
        }

        // 获取玩家当前背包的物品快照
        ItemStack[] currentContentAfter = player.getInventory().getContents().clone();

        player.sendMessage("§a[背包优化] 背包已优化！");
        plugin.getLogger().info("[背包优化调试] 优化前: " + Arrays.toString(currentContentBefore));
        plugin.getLogger().info("[背包优化调试] 优化后: " + Arrays.toString(currentContentAfter));

        return true;
    }
}