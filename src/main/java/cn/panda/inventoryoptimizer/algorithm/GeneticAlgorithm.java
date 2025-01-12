package cn.panda.inventoryoptimizer.algorithm;

import cn.panda.inventoryoptimizer.data.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class GeneticAlgorithm {

    private int populationSize;
    private double mutationRate;
    private int eliteSize;

    public GeneticAlgorithm(int populationSize, double mutationRate, int eliteSize) {
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.eliteSize = eliteSize;
    }

    public List<Individual> evolvePopulation(Player player, List<Individual> population) {
        // 1. 选择 (Selection)
        List<Individual> matingPool = Selection.tournamentSelection(population, populationSize);

        // 2. 交叉 (Crossover)
        List<Individual> nextGeneration = Crossover.crossoverPopulation(matingPool, populationSize - eliteSize);

        // 3. 变异 (Mutation)
        Mutation.mutatePopulation(nextGeneration, mutationRate);

        // 4. 精英保留 (Elitism)
        population.sort((i1, i2) -> Double.compare(FitnessCalculator.calculateFitness(player, i2), FitnessCalculator.calculateFitness(player, i1)));
        for (int i = 0; i < eliteSize; i++) {
            nextGeneration.add(new Individual(population.get(i).getGenes())); // 确保精英个体也被正确复制
        }

        // 5. 计算新一代的适应度
        nextGeneration.forEach(individual -> individual.setFitness(FitnessCalculator.calculateFitness(player, individual)));

        return nextGeneration;
    }

    public Inventory getBestInventory(List<Individual> population) {
        population.sort((i1, i2) -> Double.compare(i2.getFitness(), i1.getFitness()));
        return population.get(0).toInventory();
    }

    public List<Individual> initPopulation(Player player) {
        List<Individual> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(Individual.createRandom(player.getInventory().getContents()));
        }
        return population;
    }
}