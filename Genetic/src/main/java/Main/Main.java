package Main;

import GeneticAlg.EvolutionAlg.EvoAlg;
import static GeneticAlg.Constants.EVOLUTIONTHREADS;

/**
 * Created by minority on 11.11.16.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Start evolution");
        String path = "results/14967_Len7_Gen1_Id738.txt";


//        for (int i = 0; i < EVOLUTIONTHREADS; i++) {
//            EvoAlg geneticAlg = new EvoAlg(path);
//            geneticAlg.start();
//        }

        for (int i = 0; i < EVOLUTIONTHREADS; i++) {
            EvoAlg geneticAlg = new EvoAlg();
            geneticAlg.start();
        }


    }
}
