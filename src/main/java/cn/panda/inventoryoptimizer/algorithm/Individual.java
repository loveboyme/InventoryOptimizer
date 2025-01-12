package cn.panda.inventoryoptimizer.algorithm;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Individual {

    private List<ItemStack> genes;
    private double fitness = 0;

    public Individual(List<ItemStack> genes) {
        this.genes = new ArrayList<>(genes);
    }

    public List<ItemStack> getGenes() {
        // 返回基因列表的副本，防止外部修改原始基因
        return new ArrayList<>(genes);
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public Inventory toInventory() {
        Inventory inventory = Bukkit.createInventory(null, 36, "优化后的背包"); // 可以添加一个标题方便调试
        for (int i = 0; i < genes.size(); i++) {
            if (genes.get(i) != null) {
                inventory.setItem(i, genes.get(i).clone()); // 关键：使用 clone()
            }
        }
        return inventory;
    }

    public static Individual createRandom(ItemStack[] currentInventory) {
        List<ItemStack> items = new ArrayList<>();
        for (ItemStack item : currentInventory) {
            if (item != null) {
                items.add(item.clone()); // 确保克隆
            }
        }
        Collections.shuffle(items);
        List<ItemStack> randomGenes = new ArrayList<>(Arrays.asList(new ItemStack[36]));
        for (int i = 0; i < items.size(); i++) {
            randomGenes.set(i, items.get(i));
        }
        return new Individual(randomGenes);
    }
}