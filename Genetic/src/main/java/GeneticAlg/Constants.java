package GeneticAlg;

import java.util.Random;

/**
 * Created by minority on 14.10.16.
 */
public class Constants {

    // Reused random lib
    public static Random random = new Random();

    // General settings
    public static int EVOLUTIONTHREADS = 1;

    // Population settings
    // size of one population = number of hypothesises
    public static int POPULATIONSIZE = 500;
    // percent part for the selection of a new population
    public static double PERCENTPARTFORCROSSOVER = 0.5;
    // how many hypothesises should be mutated?
    public static double PERCENTPARTFORMUTATION = 0.3;
    // maximum generation limits
    public static int MAXGENERATION = 50;
    // percentage mutation survivor
    public static double PERCENTPARTMUTATIONSURVIVORS = 0.01;
    // fix numbers of crossover survivors
    public static double MUTATIONSURVIVOR = 3;

    // Hypothesis settings
    public static int SIZEOFONEHYP = 50;
    public static double FITNESSGOAL = 2000;
    public static int MAXVALUEFORLOAD = 100000;

    // Settings for a loaded Hypothese
    public static String PATHTOSAVETHEBESTOPCODE = "results/";
    public static double PERCENTPARTOFLOADEDOPCODE = 0.01;

    // VM settings
    public static double MAXOPERATIONPERVMSIMULATION = 100000;
    public static float MINSIZEOFTHEPRIME = 2;




}
