package GeneticAlg.EvolutionAlg;

import GeneticAlg.VM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static GeneticAlg.Constants.*;

/**
 * Created by minority on 12.10.16.
 */
public class Hypothesis implements Comparable<Hypothesis> {

    int[] opCode;
    double fitness;
    double probability;
    int hypotheseSize;
    int vmOperations;
    ArrayList primeNumbers;

    // Constructor for a new random hypothesis
    public Hypothesis() {
        // variable opcode-lengths for opcodes between 4 and SIZEOFONEHYP
        // opcodes smaller than 4 are not useful
        this.hypotheseSize = (SIZEOFONEHYP - random.nextInt(SIZEOFONEHYP) + 4);
        this.fitness = -1;
        this.probability = 0;
        this.opCode = new int[hypotheseSize];
        this.primeNumbers = new ArrayList<>();
        initializeHypothesisRandom();
    }


    // Constructor for creating a hypothesis from loaded opcode
    public Hypothesis(int[] startingopCode){
        this.opCode = startingopCode;
        this.hypotheseSize = opCode.length;
        this.fitness = -1;
        this.probability = 0;
        this.primeNumbers = new ArrayList<>();
    }

    // Constructor for a crossover child hypothesis
    public Hypothesis(int[] firstParent, int[] secondParent){
        this.hypotheseSize = firstParent.length+secondParent.length;
        this.fitness = -1;
        this.probability = 0;
        this.opCode = new int[hypotheseSize];
        this.primeNumbers = new ArrayList<>();
        initializeHypothesisWithParentOpcode(firstParent, secondParent);
    }

    // concat parents opCode to a new child opCode
    // https://community.oracle.com/thread/2103771?start=0&tstart=0
    private void initializeHypothesisWithParentOpcode(int[] first, int[] second) {
        this.opCode = Arrays.copyOf(first,hypotheseSize);
        System.arraycopy(second,0,opCode,first.length,second.length);
    }


    // initialize this hypothese with random opCode
    private void initializeHypothesisRandom() {

        // every opcode should start with a "load-command"
        opCode[0] = generateRandomLoadComand();
        for (int i = 1; i < hypotheseSize; i++) {
            opCode[i] = generateRandomCommand();
        }
    }

    // values between 0 - MAXVALUEFORLOAD
    private int generateRandomLoadComand() {
        return random.nextInt(MAXVALUEFORLOAD) << 3;
    }


    // creates cmd in between 0 and 7
    private int generateRandomCommand() {
        int cmd = random.nextInt(8);
        if (cmd == 0) {
            return generateRandomLoadComand();
        } else {
            return cmd;
        }
    }

    // calculate the individual fitness according to the opcode length
    // we only calculate the fitness if opcode is new or changed
    // this is for performance reason, we avoid calculating the fitness double
    public double calcFitness(VM vm) {
        // we set the fitness to -1 if new (initialized or from crossover) or mutated
        if (this.fitness==-1){
            vm.setMemAndResizeMAX(opCode);
            vm.simulate();
            this.primeNumbers = vm.getPrimeNumbers();
            this.vmOperations = vm.getCounter();
            this.fitness = primeNumbers.size();
            vm.reset();
        }
        return fitness;
    }


    // switch a random command in this hypothese
    // dont change the first command (load)
    // reset fitness to -1 for a new calculation
    public Hypothesis mutate() {
        this.fitness = -1;
        int low = 0;
        int res = random.nextInt(hypotheseSize) + low;
        opCode[res] = generateRandomCommand();
        return this;
    }


    // for reverse sorting by fitness
    public int compareTo(Hypothesis o) {
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
        Collections.sort(primeNumbers);
        String returnStr = "[";

        for (int b : opCode) {
            switch (b) {
                case 1: {
                    returnStr += "PUSH,";
                    break;
                }
                case 2: {
                    returnStr += "POP,";
                    break;
                }
                case 3: {
                    returnStr += "MUL,";
                    break;
                }
                case 4: {
                    returnStr += "DIV,";
                    break;
                }
                case 5: {
                    returnStr += "ADD,";
                    break;
                }
                case 6: {
                    returnStr += "SUB,";
                    break;
                }
                case 7: {
                    returnStr += "JIH,";
                    break;
                }
                default: {
                    returnStr += "LOAD" + b + ",";
                    break;
                }
            }

        }
        returnStr += "]" + "\n" + "Fitness = " + this.fitness +
                "; " + "probability: " + this.probability +
                " opCode length: " + hypotheseSize +
                " vmOperations: " + this.vmOperations + "\n";
        returnStr += "PrimeNumbers found: " + primeNumbers + "\n";
        return returnStr;
    }
}