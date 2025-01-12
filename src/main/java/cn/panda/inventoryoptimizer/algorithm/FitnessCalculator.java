package cn.panda.inventoryoptimizer.algorithm;

import cn.panda.inventoryoptimizer.data.DataManager;
import cn.panda.inventoryoptimizer.data.PlayerData;
import cn.panda.inventoryoptimizer.InventoryOptimizer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

public class FitnessCalculator {

    public static double calculateFitness(Player player, Individual individual) {
        PlayerData playerData = InventoryOptimizer.getInstance().getDataManager().getPlayerData(player.getUniqueId());
        if (playerData == null) {
            return 0; // 如果没有数据，则适应度为0
        }

        double fitness = 0;
        ItemStack[] inventory = individual.toInventory().getContents();

        for (int i = 0; i < inventory.length; i++) {
            ItemStack item = inventory[i];
            if (item != null && item.getType() != Material.AIR) {
                // 偏好得分：如果物品在玩家经常使用的位置，则得分高
                fitness += playerData.getSlotPreference(item.getType(), i);

                // 使用频率得分：常用物品得分高
                fitness += playerData.getItemUsageCount(item.getType()) * 0.1; // 可以调整权重
            }
        }
        return fitness;
    }
}