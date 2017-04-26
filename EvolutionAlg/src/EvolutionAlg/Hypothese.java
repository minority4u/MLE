package EvolutionAlg;

import java.util.Random;

import static EvolutionAlg.Constants.*;

/**
 * Created by minority on 12.10.16.
 */
public class Hypothese implements Comparable<Hypothese> {

    Byte[] hypothesis;
    int fitness;
    double probability;
    int hypotheseSize;
    Random random;

    public Hypothese() {
        this.hypotheseSize = sizeOfOneHyp;
        this.random = new Random();
        fitness = 0;
        probability = 0;
        hypothesis = new Byte[hypotheseSize];
        initializeHypothesis();
    }

    // initialize this hypothese with random values between 0 and 1
    private void initializeHypothesis() {
        for (int i = 0; i < hypotheseSize; i++) {
            if (random.nextBoolean()) {
                hypothesis[i] = 1;
            } else {
                hypothesis[i] = 0;
            }
        }
    }

    // compares this hypothese with a goal hypothese and returns the hemming distance as fitness value
    // different lengths are ignored
    // actual fitness is stored as class var
    public int calcFitnessWithHemmingDistance(Byte[] goal) {
        int tempFitness = 0;
        int shorter = Math.min(goal.length, hypotheseSize);

        for (int i = 0; i < shorter; i++) {
            if (goal[i] != hypothesis[i]) {
                tempFitness += 1;
            }
        }
        this.fitness = shorter - tempFitness;
        return this.fitness;
    }

    // switch a random bit in this hypothese
    public Hypothese mutate() {
        int low = 0;

        int res = random.nextInt(hypotheseSize - low) + low;

        if (hypothesis[res] == 1) {
            hypothesis[res] = 0;
        } else {
            hypothesis[res] = 1;
        }
        return this;
    }


    // for reverse sorting by probability
    @Override
    public int compareTo(Hypothese o) {
        int returnVal = 0;

        if (this.probability < o.probability) {
            returnVal = 1;
        } else if (this.probability > o.probability) {
            returnVal = -1;
        } else if (this.probability == o.probability) {
            returnVal = 0;
        }
        return returnVal;
    }

    // console output
    @Override
    public String toString() {
        String returnStr = "[";

        for (Byte b : hypothesis) {
            returnStr += Byte.toString(b) + ",";
        }
        returnStr += "]" + "\n" + "Fitness = " + this.fitness + "; " + "probability: " + this.probability;
        return returnStr;
    }
}