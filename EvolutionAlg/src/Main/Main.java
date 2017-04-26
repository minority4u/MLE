package Main;

import EvolutionAlg.EvoAlg;
import EvolutionAlg.Hypothese;


/**
 * Created by minority on 12.10.16.
 */
public class Main {

    public static void main(String[] args) {

        // create a random goal
        Hypothese goalHyp = new Hypothese();

        // create a random evoAlg and start the evolution
        EvoAlg evoAlg = new EvoAlg(goalHyp);

        evoAlg.run();
    }

}
