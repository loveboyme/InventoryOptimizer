package cn.panda.inventoryoptimizer.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Selection {

    private static final Random random = new Random();

    public static List<Individual> tournamentSelection(List<Individual> population, int tournamentSize) {
        List<Individual> matingPool = new ArrayList<>();
        for (int i = 0; i < population.size(); i++) {
            List<Individual> tournament = new ArrayList<>();
            for (int j = 0; j < tournamentSize; j++) {
                tournament.add(population.get(random.nextInt(population.size())));
            }
            tournament.sort((a, b) -> Double.compare(b.getFitness(), a.getFitness()));
            matingPool.add(tournament.get(0));
        }
        return matingPool;
    }
}