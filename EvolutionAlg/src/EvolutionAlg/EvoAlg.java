package EvolutionAlg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static EvolutionAlg.Constants.*;

/**
 * Created by minority on 12.10.16.
 */
public class EvoAlg {

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

    ArrayList<Hypothese> hypothesises;
    Hypothese goalHypothese;
    ArrayList<Hypothese> nextGeneration;

    public EvoAlg(Hypothese goalHypothese) {
        this.goalHypothese = goalHypothese;
        initialize();
    }

    private void initialize() {
        // initialize parameters
        this.sumOfAllFitnesses = 0;
        this.maxFitness = 0;
        this.generation = 0;
        this.fitnessThreshold = minFitness;
        this.populationSize = numberOfHyp;
        this.percentagePartForCrossover = percentPartToReplace;
        this.percentagePartForMutation = mutationPercentageRate;
        this.random = new Random();
        this.hypothesises = new ArrayList();

        // initialize the starting population with new random hypotheses
        for (int i = 0; i < populationSize; i++) {
            hypothesises.add(new Hypothese());
        }
        // calculate this population facts
        evaluateAndSortPopulation();
    }

    // sum the fitness of all hyp and save the highest fitness in the maxFitness variable
    private void calculateFitnessForAllHyp() {
        this.sumOfAllFitnesses = 0;
        this.maxFitness = 0;

        for (Hypothese hyp : hypothesises) {
            this.sumOfAllFitnesses += hyp.calcFitnessWithHemmingDistance(goalHypothese.hypothesis);
            if (hyp.fitness >= this.maxFitness) {
                this.maxFitness = hyp.fitness;
            }
        }
    }

    // calculate the individual probability according to the individual fitness percentage of fitness sum
    private void calculateProbabilityForAllHyp() {
        for (Hypothese hyp : hypothesises) {
            hyp.probability = (hyp.fitness) / sumOfAllFitnesses;
        }
    }

    public void printPopulationOverview(){
        System.out.println("Generation: " + this.generation);
        System.out.println("Max fitness: " + this.maxFitness);
        System.out.println("Fitness sum: " + this.sumOfAllFitnesses);
        System.out.println("Fitness threshold: " + this.fitnessThreshold * goalHypothese.hypothesis.length);
        System.out.println("EvoAlg size: " + this.hypothesises.size());
        //printHypotheses();
        System.out.println("......................................");
    }

    // prints all hypotheses of this population
    public void printHypotheses() {
        for (Hypothese hyp : hypothesises) {
            System.out.println(hyp.toString());
        }
    }

    // sorts all hypotheses desc by probability and prints the best hypothese
    public void printTheBestHyp(){
        Collections.sort(hypothesises);
        System.out.println("Best hypothese in generation " + this.generation );
        System.out.println(hypothesises.get(0).toString());
    }

    // Start the evolution algorithm for this population
    public void run() {

        while (this.maxFitness < this.fitnessThreshold * goalHypothese.hypothesis.length
                && generation < maxNumberOfGenerations
                && hypothesises.size() > minimalSizeOfTheGeneration) {

            // print a short overview about this population
            printPopulationOverview();

            // selection
            nextGeneration = selection();

            // crossover
            nextGeneration.addAll(crossover(hypothesises));

            // mutate and update the next generation
            update(mutation(nextGeneration));

            // rise the population counter
            generation++;

            // calculate the facts for the new population and sort the hypotheses
            evaluateAndSortPopulation();

        }
        System.out.println("\nEvolution done... \n");
        printTheBestHyp();
    }


    // second idea, the probability is a selector for the new generation, rank selection
    private ArrayList<Hypothese> selection() {

        int numberOfSelectedHyp = (int) ((1 - this.percentagePartForCrossover) * this.populationSize);
        ArrayList nextGeneration = new ArrayList<Hypothese>();
        // as long as we dont have enough hyp in our selected list we add the best from actual population
        for (Hypothese hyp : hypothesises) {
            if (nextGeneration.size() < numberOfSelectedHyp) {
                // our list is sorted by probability, therefore we take the first elements, rank selection
                nextGeneration.add(hyp);
            } else {
                break;
            }
        }
        return nextGeneration;
    }


    // second idea, the probability is a direct selector
    private ArrayList<Hypothese> crossover(ArrayList<Hypothese> oldPopulation) {

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
        for (int i = 0; i+1 < pairsOfParents.size(); i = i + 2) {
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
        int crossoverIndex = (int) (randomCross * (Math.min(father.hypothesis.length, mother.hypothesis.length)));
        ArrayList<Hypothese> children = new ArrayList();
        Hypothese child1 = new Hypothese();
        Hypothese child2 = new Hypothese();

        for (int i = 0; i < father.hypothesis.length; i++) {
            if (i < crossoverIndex) {
                child1.hypothesis[i] = father.hypothesis[i];
                child2.hypothesis[i] = mother.hypothesis[i];
            } else {
                child1.hypothesis[i] = mother.hypothesis[i];
                child2.hypothesis[i] = father.hypothesis[i];
            }
        }

        children.add(child1);
        children.add(child2);
        return children;
    }

    private ArrayList<Hypothese> mutation(ArrayList<Hypothese> nextGeneration) {

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

    private void update(ArrayList<Hypothese> nextGeneration) {
        this.hypothesises = nextGeneration;
        this.populationSize = nextGeneration.size();
    }

    private void evaluateAndSortPopulation(){
        calculateFitnessForAllHyp();
        calculateProbabilityForAllHyp();
        Collections.sort(hypothesises);
    }





/*    // first idea, the probability is a true %-probability
    private ArrayList<Hypothese> selectionOld() {

        int numberOfSelectedHyp = (int) ((1 - this.percentagePartForCrossover) * this.populationSize);
        ArrayList nextGeneration = new ArrayList<Hypothese>();
        // as long as we dont have enough hyp in our selected list search for the best in actual population
        // buggy!!! for loop needs longer than while loop
        while (nextGeneration.size() < numberOfSelectedHyp) {
            for (Hypothese hyp : hypothesises) {
                // use the individual probability for better selection of each
                if (this.random.nextDouble() < hyp.probability) {
                    nextGeneration.add(hyp);
                }
            }
        }
        return nextGeneration;
    }*/

/*    // first idea, the probability is a true %-probability
    private ArrayList<Hypothese> crossoverOld(ArrayList<Hypothese> oldPopulation) {
        //int numberOfCrossoverHypPairs = (int) ((this.percentagePartForCrossover * this.populationSize) / 2);
        int numberOfCrossoverHypPairs = (int) ((this.percentagePartForCrossover * this.populationSize / 2));
        ArrayList pairsOfParents = new ArrayList<Hypothese>();
        ArrayList nextGeneration = new ArrayList<Hypothese>();

        // select the hyp pairs
        // as long as we dont have enough pairs of hyp in our crossover list search for the best in actual population
        while (pairsOfParents.size() < numberOfCrossoverHypPairs) {
            for (Hypothese hyp : oldPopulation) {
                // calculate the individual probability
                hyp.probability = ((hyp.fitness) / sumOfAllFitnesses);
                // use the individual probability for better selection of each
                if (this.random.nextDouble() < hyp.probability) {
                    pairsOfParents.add(hyp);
                }
            }
        }
        // generate children and add them to the next generation
        for (int i = 0; i < pairsOfParents.size() - 1; i = i + 2) {
            Hypothese fatherHyp = (Hypothese) pairsOfParents.get(i);
            Hypothese motherhyp = (Hypothese) pairsOfParents.get(i + 1);
            nextGeneration.addAll(cross(fatherHyp, motherhyp));
        }
        return nextGeneration;
    }*/

}
