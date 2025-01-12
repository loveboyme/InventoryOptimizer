package cn.panda.inventoryoptimizer.algorithm;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.bukkit.inventory.ItemStack;

public class Mutation {

    private static final Random random = new Random();

    public static void mutatePopulation(List<Individual> population, double mutationRate) {
        for (Individual individual : population) {
            if (random.nextDouble() < mutationRate) {
                mutate(individual);
            }
        }
    }

    public static void mutate(Individual individual) {
        List<ItemStack> genes = individual.getGenes();
        if (genes.isEmpty()) return;
        int index1 = random.nextInt(genes.size());
        int index2 = random.nextInt(genes.size());
        // 确保在交换时也处理了 null 值
        ItemStack item1 = genes.get(index1);
        ItemStack item2 = genes.get(index2);
        genes.set(index1, item2 == null ? null : item2.clone());
        genes.set(index2, item1 == null ? null : item1.clone());
    }
}