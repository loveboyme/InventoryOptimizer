package cn.panda.inventoryoptimizer.util;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtil {

    public static boolean canAddItem(Inventory inventory, ItemStack item) {
        return inventory.addItem(item.clone()).isEmpty();
    }
}