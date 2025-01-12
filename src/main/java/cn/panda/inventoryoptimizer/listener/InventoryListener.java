package cn.panda.inventoryoptimizer.listener;

import cn.panda.inventoryoptimizer.InventoryOptimizer;
import cn.panda.inventoryoptimizer.data.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

public class InventoryListener implements Listener {

    private final InventoryOptimizer plugin;

    public InventoryListener(InventoryOptimizer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();

        // 确保点击事件发生在玩家自己的背包中
        if (clickedInventory == null || !clickedInventory.equals(player.getInventory())) {
            return;
        }

        PlayerData playerData = plugin.getDataManager().getOrCreatePlayerData(player);

        // 记录物品使用和槽位偏好
        ItemStack currentItem = event.getCurrentItem();
        if (currentItem != null && currentItem.getType() != Material.AIR) {
            playerData.incrementItemUsage(currentItem.getType());
            if (event.getSlotType() != org.bukkit.event.inventory.InventoryType.SlotType.OUTSIDE) {
                playerData.incrementSlotPreference(currentItem.getType(), event.getSlot());
            }
            if (!playerData.isLearningComplete() && playerData.getItemUsageCount().values().stream().mapToInt(Integer::intValue).sum() >= plugin.getConfig().getInt("learning_threshold", 200)) {
                playerData.setLearningComplete(true);
                plugin.getDataManager().savePlayerData(playerData);
                player.sendMessage("§a[背包优化] 学习完成！现在可以使用 /optimize 命令来优化你的背包了。");
            }
            plugin.getDataManager().savePlayerData(playerData); // 及时保存数据
        }
    }
}