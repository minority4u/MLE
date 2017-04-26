package GeneticAlg.Algorithm;

import GeneticAlg.Individum.Hypothese;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import static GeneticAlg.Constants.*;

/**
 * Created by minority on 03.11.16.
 */
public class GeneticAlgorithm {

    // size of this population (p)
    int populationSize;
    // percentagePartForCrossover (r)
    double percentagePartForCrossover;
    // mutation percentage rate (m), the index for mutation is random.
    double percentagePartForMutation;

    double maxFitness;
    double fitnessThreshold;
    double sumOfAllFitnesses;
    int generation;
    Random random;

    ArrayList<Hypothese> population;
    ArrayList<Hypothese> nextPopulation;


    private void initialize() {
        // initialize parameters
        this.sumOfAllFitnesses = 0;
        this.maxFitness = 0;
        this.generation = 0;
        this.fitnessThreshold = minFitness;
        this.populationSize = populationSize;
        this.percentagePartForCrossover = percentPartToReplace;
        this.percentagePartForMutation = mutationPercentageRate;
        this.random = new Random();
        this.population = new ArrayList<Hypothese>();
        this.nextPopulation = new ArrayList<Hypothese>();

        // initialize the starting population with new random hypotheses
        for (int i = 0; i < populationSize; i++) {
            population.add(new Hypothese());
        }
        // calculate this population facts
        evalualteAndSortPopulation();

    }

    private void calculateFitnessForTheCurrentPopulation() {
        this.sumOfAllFitnesses = 0;
        this.maxFitness = 0;

        population.forEach(Hypothese::calcFitness);


    }

    public void printPopulationOverview() {

    }

    public void printBestCode() {

    }

    public void run() {

        while (maxFitness < this.fitnessThreshold) {
            this.nextPopulation = selection();
            this.nextPopulation.addAll(crossover(population));
            mutate(nextPopulation);
            update();
            evalualteAndSortPopulation();
            generation++;
        }
        System.out.println("\nEvolution done... \n");
        printBestCode();

    }

    private void evalualteAndSortPopulation() {
        calculateFitnessForTheCurrentPopulation();
        calculateProbabilityForAll();
        Collections.sort(population);
    }

    // calculate the individual probability according to the individual fitness percentage of fitness sum
    private void calculateProbabilityForAll() {
        for (Hypothese hypothese : population) {
            hypothese.probability = (hypothese.fitness) / sumOfAllFitnesses;
        }
    }


    private void update() {
    }

    private ArrayList<Hypothese> mutate(ArrayList<Hypothese> nextGeneration) {

        int Low = 0;
        int High = nextGeneration.size();
        int mutations = (int) (percentagePartForMutation * nextGeneration.size());
        int index;
        Hypothese hypToMutate;

        for (int i = 0; i < mutations; i++) {
            index = random.nextInt(High - Low) + Low;
            // replace mutated
            hypToMutate = nextGeneration.remove(index);
            nextGeneration.add(hypToMutate.mutate());
        }
        return nextGeneration;


    }

    private Collection<Hypothese> crossover(ArrayList<Hypothese> oldPopulation) {

        int numberOfCrossoverHypPairs = (int) ((this.percentagePartForCrossover * this.populationSize));
        ArrayList pairsOfParents = new ArrayList<Hypothese>();
        ArrayList nextGeneration = new ArrayList<Hypothese>();

        // select the best parents for crossover
        for (Hypothese hyp : oldPopulation) {
            // Our population is sorted by probability, we only take the best
            if (pairsOfParents.size() < numberOfCrossoverHypPairs) {
                pairsOfParents.add(hyp);
            } else {
                break;
            }
        }
        // to avoid Nullpointer --> i +1 < pairsOfParents.size()
        // cross each parent-pair and add the new pair of children to the next generation
        for (int i = 0; i + 1 < pairsOfParents.size(); i = i + 2) {
            Hypothese fatherHyp = (Hypothese) pairsOfParents.get(i);
            Hypothese motherhyp = (Hypothese) pairsOfParents.get(i + 1);
            nextGeneration.addAll(cross(fatherHyp, motherhyp));
        }
        return nextGeneration;
    }

    // returns an Arraylist with two hyp children single point crossed out of each parent pair hyp
    // single point crossover
    private ArrayList<Hypothese> cross(Hypothese father, Hypothese mother) {

        double randomCross = random.nextDouble();
        int crossoverIndex = (int) (randomCross * (Math.min(father.codeTree.getSize(father.codeTree.root), mother.codeTree.getSize(mother.codeTree.root))));
        ArrayList<Hypothese> children = new ArrayList();
        Hypothese child1 = new Hypothese();
        Hypothese child2 = new Hypothese();

        //TODO
//        for (int i = 0; i < father.codeTree.size(); i++) {
//            if (i < crossoverIndex) {
//                child1.hypothesis[i] = father.hypothesis[i];
//                child2.hypothesis[i] = mother.hypothesis[i];
//            } else {
//                child1.hypothesis[i] = mother.hypothesis[i];
//                child2.hypothesis[i] = father.hypothesis[i];
//            }
//        }

        children.add(child1);
        children.add(child2);
        return children;
    }


    private ArrayList<Hypothese> selection() {
        return new ArrayList<Hypothese>();
    }


}
