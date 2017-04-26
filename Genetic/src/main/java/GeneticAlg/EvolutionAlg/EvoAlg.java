package GeneticAlg.EvolutionAlg;

import GeneticAlg.OpcodeLoader;
import GeneticAlg.VM;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


import static GeneticAlg.Constants.*;

/**
 * Created by minority on 12.10.16.
 */
public class EvoAlg extends Thread {

    // for multithreaded evolutions
    int ID;
    // current generation of this population
    int generation;

    // size of this population (p)
    int populationSize;
    // percentagePartForCrossover (r) - single point crossover
    double percentagePartForCrossover;
    // mutation percentage rate (m), the index for mutation is random.
    double percentagePartForMutation;

    double maxFitness;
    double fitnessThreshold;
    double sumOfAllFitnesses;

    int lenOfBestHyp;
    int averageOpcodeLength;
    int vmOperationsOfBestHyp;

    ArrayList<Hypothesis> currentPopulation;
    ArrayList<Hypothesis> nextGeneration;

    // helper objects
    // necessary to load an existing opcode into a new evolution
    OpcodeLoader opcodeLoader;
    // necessary to simulate each opcode and evaluate
    VM vm;


    // new population with random hypothesis
    public EvoAlg() {
        this.opcodeLoader = null;
        initializeParams();
        initializePopulation();
        evaluateAndSortPopulation();
    }

    // new population with hypothesis from loaded opcode
    public EvoAlg(String path) {
        this.opcodeLoader = new OpcodeLoader(path);
        initializeParams();
        initializePopulationWithOpcode();
        evaluateAndSortPopulation();
    }

    // initialize half of the population with the loaded opcode
    // the others will be initialized to include new gens
    private void initializePopulationWithOpcode() {

        // TODO: define the percent part for the loaded opcode in Constants
        // actually its 1 %
        for (int i = 0; i < populationSize; i++) {
            if (currentPopulation.size() < populationSize * PERCENTPARTOFLOADEDOPCODE) {
                currentPopulation.add(new Hypothesis(opcodeLoader.getOpCode()));
            } else {
                currentPopulation.add(new Hypothesis());
            }
        }
    }

    // initialize the starting population with new random hypotheses
    private void initializePopulation() {
        for (int i = 0; i < populationSize; i++) {
            currentPopulation.add(new Hypothesis());
        }
    }

    private void initializeParams() {
        // initialize objects
        this.vm = new VM();
        this.currentPopulation = new ArrayList<Hypothesis>();
        this.nextGeneration = new ArrayList<Hypothesis>();
        this.ID = random.nextInt(1000);
        this.generation = 0;
        this.sumOfAllFitnesses = 0;
        this.fitnessThreshold = FITNESSGOAL;
        this.maxFitness = 0;
        this.lenOfBestHyp = 0;
        this.percentagePartForCrossover = PERCENTPARTFORCROSSOVER;
        this.percentagePartForMutation = PERCENTPARTFORMUTATION;
        this.populationSize = POPULATIONSIZE;
        // flexible populationsize for multithreaded testing
        //this.populationSize = POPULATIONSIZE - random.nextInt(POPULATIONSIZE) + 20;
    }

    // Start the evolution algorithm for this population
    public void run() {
        // print a short overview vor each population (multithreaded) at the beginning
        printPopulationOverview();
        //printHypotheses();

        while (this.maxFitness < this.fitnessThreshold
                && generation < MAXGENERATION) {

            // print a short overview about this population for each generation
            printPopulationOverview();

            // selection, selected hyp will be added to nextGeneration
            selection();

            // crossover, children will be added to nextGeneration
            crossover();

            // mutate nextGeneration
            mutate();

            // update the next generation as current population
            update();

            // rise the population counter
            generation++;

            // calculate and sort the new population
            evaluateAndSortPopulation();
        }

        System.out.println("\nEvolution no." + this.ID + " done... \n");
        //printTheBestHyp();
        saveTheBestOpcodeToFile();
        //printHypotheses();
    }


    public void printPopulationOverview() {
        System.out.println("Evolution-ID: " + this.ID);
        System.out.println("Generation: " + this.generation);
        System.out.println("Max fitness: " + this.maxFitness);
        System.out.println("Length of the best Hyp: " + this.lenOfBestHyp);
        System.out.println("VM-Operations of the best Hyp: : " + this.vmOperationsOfBestHyp);
        System.out.println("Fitness sum: " + this.sumOfAllFitnesses);
        System.out.println("Average Hyp length: " + this.averageOpcodeLength);
        System.out.println("Fitness threshold: " + this.fitnessThreshold);
        System.out.println("Population size: " + this.currentPopulation.size());
        //printHypotheses();
        System.out.println("......................................");
    }


    // prints all hypotheses of this population
    public void printHypotheses() {
        for (Hypothesis hyp : currentPopulation) {
            System.out.println(hyp.toString());
        }
    }

    // sorts all hypotheses desc by probability and prints the best hypothese
    public void printTheBestHyp() {
        System.out.println(currentPopulation.get(0));
        System.out.println();
    }

    public void printStack(Hypothesis hypothesis) {
        System.out.println("The Stack of this hypothesis: ");
        vm.setMemAndResizeMAX(hypothesis.opCode);
        vm.simulate();
        //vm.printStack();
    }

    // sum the fitness of all hyp and save the highest fitness in the maxFitness variable
    private void calculateFitnessAndProbabilityForAllHyp() {
        this.sumOfAllFitnesses = 0;
        this.maxFitness = 0;
        int lengthSum = 0;

        for (Hypothesis hyp : currentPopulation) {
            this.sumOfAllFitnesses += hyp.calcFitness(vm);
            lengthSum += hyp.hypotheseSize;
            if (hyp.fitness >= this.maxFitness) {
                this.lenOfBestHyp = hyp.hypotheseSize;
                this.maxFitness = hyp.fitness;
                this.vmOperationsOfBestHyp = hyp.vmOperations;
            }
        }
        // calculate the probability, fitness must be calculated before
        for (Hypothesis hyp : currentPopulation) {
            hyp.probability = (hyp.fitness) / sumOfAllFitnesses;
        }
        this.averageOpcodeLength = lengthSum / populationSize;
    }


    // stores the best hyp at project directory dir = PATHTOSAVETHEBESTOPCODE for later reuse
    private void saveTheBestOpcodeToFile() {

        Hypothesis bestHyp;
        Collections.sort(currentPopulation);
        bestHyp = currentPopulation.get(0);

        try {
            String filename = PATHTOSAVETHEBESTOPCODE +
                    (int) bestHyp.fitness +
                    "_Len" + bestHyp.opCode.length +
                    "_Gen" + generation +
                    "_Id" + this.ID +
                    ".txt";

            PrintWriter writer = new PrintWriter(filename, "UTF-8");
            writer.println(bestHyp);
            writer.close();
        } catch (Exception e) {
            // do something
        }
    }


    // second idea, rank selection
    private void selection() {

        int numberOfSelectedHyp = (int) ((1 - this.percentagePartForCrossover) * this.populationSize);
        // as long as we dont have enough hyp in our selected list we add the best from actual population
        for (Hypothesis hyp : currentPopulation) {
            if (this.nextGeneration.size() < numberOfSelectedHyp) {
                // our list is sorted by probability, therefore we take the first elements, rank selection
                this.nextGeneration.add(hyp);
            } else {
                break;
            }
        }
    }

    // TODO: after n generations population is dominated by the best?
    // second idea, the probability is a direct selector
    private void crossover() {

        int numberOfCrossoverHypPairs = (int) ((this.percentagePartForCrossover * this.populationSize));
        ArrayList pairsOfParents = new ArrayList<>();

        // select the best parents for crossover
        for (Hypothesis hyp : currentPopulation) {
            // Our population is sorted by probability, we only take the best
            if (pairsOfParents.size() < numberOfCrossoverHypPairs) {
                pairsOfParents.add(hyp);
            } else {
                break;
            }
        }


        // cross each parent-pair and add the new pair of children to the next generation
        // cut and splice crossover
        int randomCrossoverPartnerIndex;
        Hypothesis fatherHyp, motherHyp;

        // create two children for each couple and add them to the new population
        while (pairsOfParents.size() > 1) {
            randomCrossoverPartnerIndex = random.nextInt(pairsOfParents.size());
            fatherHyp = (Hypothesis) pairsOfParents.remove(randomCrossoverPartnerIndex);
            motherHyp = (Hypothesis) pairsOfParents.remove(0);
            this.nextGeneration.addAll(cross(motherHyp, fatherHyp));
        }
    }


    // returns an arraylist with two hyp children single point crossed out of each parent pair hyp
    // single point crossover
    // cut and slice
    private ArrayList<Hypothesis> cross(Hypothesis father, Hypothesis mother) {

        int minSizeOfParents;
        int crossoverIndex;
        int[] fatherFront, fatherBack, motherFront, motherBack;
        ArrayList<Hypothesis> childrens = new ArrayList();

        // get a random index for single point crossover between 0 and min(parents.opCode.length)
        // childrens length =  crossoverIndex + (parent.length - crossoverIndex)
        minSizeOfParents = Math.min(father.opCode.length, mother.opCode.length);
        crossoverIndex = random.nextInt(minSizeOfParents);

        // cuts the parents in two new code sequences each
        motherFront = Arrays.copyOfRange(mother.opCode, 0, crossoverIndex);
        fatherFront = Arrays.copyOfRange(father.opCode, 0, crossoverIndex);
        motherBack = Arrays.copyOfRange(mother.opCode, crossoverIndex, mother.hypotheseSize);
        fatherBack = Arrays.copyOfRange(father.opCode, crossoverIndex, father.hypotheseSize);

        // creates two new Hypotheses
        childrens.add(new Hypothesis(motherFront, fatherBack));
        childrens.add(new Hypothesis(fatherFront, motherBack));
        return childrens;
    }


    // returns an arraylist with two hyp children single point crossed out of each parent pair hyp
    // single point crossover, but children length could cut the parents gene
    private ArrayList<Hypothesis> crossOld(Hypothesis father, Hypothesis mother) {

        int minSizeOfParents;
        int minSizeOfChildren;
        int minSizeOfChildrenAndParents;
        int crossoverIndex;
        ArrayList<Hypothesis> children = new ArrayList();
        Hypothesis child1 = new Hypothesis();
        Hypothesis child2 = new Hypothesis();

        // get a random index for single point crossover between 0 and min(opCode.length)
        minSizeOfParents = Math.min(father.opCode.length, mother.opCode.length);
        minSizeOfChildren = Math.min(child1.opCode.length, child2.opCode.length);
        minSizeOfChildrenAndParents = Math.min(minSizeOfParents, minSizeOfChildren);
        crossoverIndex = random.nextInt(minSizeOfChildrenAndParents);

        for (int i = 0; i < minSizeOfChildrenAndParents; i++) {
            if (i < crossoverIndex) {
                child1.opCode[i] = father.opCode[i];
                child2.opCode[i] = mother.opCode[i];
            } else {
                child1.opCode[i] = mother.opCode[i];
                child2.opCode[i] = father.opCode[i];
            }
        }

        children.add(child1);
        children.add(child2);
        return children;
    }


    private void mutate() {

        // TODO: survivor?
        int Low = (int) (PERCENTPARTMUTATIONSURVIVORS * populationSize);
        //int Low = (int) MUTATIONSURVIVOR;
        int High = nextGeneration.size();
        int mutations = (int) (percentagePartForMutation * nextGeneration.size());
        int index;

        Hypothesis hypToMutate;

        for (int i = 0; i < mutations; i++) {
            // dont mutate the best
            // we dont create the smallest indexes (the best are at the front part of the list)
            index = random.nextInt(High - Low) + Low;
            // replace mutated
            hypToMutate = nextGeneration.remove(index);
            // add Hyp at last position
            nextGeneration.add(nextGeneration.size() - 1, hypToMutate.mutate());
        }
    }

    // set the next generation as current generation
    private void update() {
        this.currentPopulation = nextGeneration;
        this.populationSize = nextGeneration.size();
        this.nextGeneration = new ArrayList<>();
    }

    private void evaluateAndSortPopulation() {
        calculateFitnessAndProbabilityForAllHyp();
        Collections.sort(currentPopulation);
    }

}
