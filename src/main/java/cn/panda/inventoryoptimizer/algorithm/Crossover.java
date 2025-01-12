package cn.panda.inventoryoptimizer.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.bukkit.inventory.ItemStack;

public class Crossover {

    private static final Random random = new Random();

    public static List<Individual> crossoverPopulation(List<Individual> matingPool, int populationSize) {
        List<Individual> children = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            Individual parent1 = matingPool.get(random.nextInt(matingPool.size()));
            Individual parent2 = matingPool.get(random.nextInt(matingPool.size()));
            children.add(crossover(parent1, parent2));
        }
        return children;
    }

    public static Individual crossover(Individual parent1, Individual parent2) {
        List<ItemStack> parent1Genes = parent1.getGenes();
        List<ItemStack> parent2Genes = parent2.getGenes();
        List<ItemStack> childGenes = new ArrayList<>(Arrays.asList(new ItemStack[parent1Genes.size()]));

        int crossoverPoint = random.nextInt(parent1Genes.size());

        for (int i = 0; i < parent1Genes.size(); i++) {
            if (i < crossoverPoint) {
                childGenes.set(i, parent1Genes.get(i) == null ? null : parent1Genes.get(i).clone());
            } else {
                childGenes.set(i, parent2Genes.get(i) == null ? null : parent2Genes.get(i).clone());
            }
        }

        return new Individual(childGenes);
    }
}