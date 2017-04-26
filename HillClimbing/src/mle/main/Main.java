package mle.main;

import mle.hillclimb.Journey;
import mle.hillclimb.Map;

/**
 * Created by minority on 06.10.16.
 */
public class Main {
    public static int numberOfCities = 100;
    public static int numberOfChanges = 100000;
    public static int numberOfStartJourneys = 10;

    public static void main(String[] args) {



        Map myMap = new Map(numberOfCities, numberOfChanges);
        Journey tmpJourney;
        Journey bestJourney = new Journey(numberOfCities);

        for (int i = 0; i < numberOfStartJourneys; i++){
            tmpJourney = myMap.runHillclimberWithNewJourneyOrder();
            System.out.println("tmp:" + tmpJourney.journeyLength);
            System.out.println("best:" + tmpJourney.journeyLength);
            if(tmpJourney.journeyLength < bestJourney.journeyLength){
                System.out.println("Better journey:");
                bestJourney = tmpJourney;
            }
        }
        System.out.println("Best journey:");
        bestJourney.printJourney();






    }


}
