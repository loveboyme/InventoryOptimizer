package cn.panda.inventoryoptimizer.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemStackUtil {

    public static boolean areEqual(ItemStack item1, ItemStack item2) {
        if (item1 == null || item1.getType() == Material.AIR) {
            return item2 == null || item2.getType() == Material.AIR;
        }
        if (item2 == null || item2.getType() == Material.AIR) {
            return false;
        }
        return item1.isSimilar(item2) && item1.getAmount() == item2.getAmount();
    }
}