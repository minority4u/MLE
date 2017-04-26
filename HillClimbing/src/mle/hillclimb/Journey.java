package mle.hillclimb;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by minority on 19.01.17.
 */
public class Journey {

    public double journeyLength;
    public int[] journey;


    public Journey(int size){
        // initialize journey with big values
        this.journeyLength = size * 1000;
        this.journey = new int[size];
    }



    public Journey(double journeyLength, int[] journey){
        this.journeyLength = journeyLength;
        this.journey = journey;
    }

    public void resetJourney(){
        this.journeyLength = this.journey.length * 1000;
        randomizeJourney();
    }


    private void randomizeJourney(){
        Random rgen = new Random();
        int randomPosition, temp;

        for (int i=0; i< journey.length; i++) {
            randomPosition = rgen.nextInt(journey.length);
            temp = journey[i];
            journey[i] = journey[randomPosition];
            journey[randomPosition] = temp;
        }
    }



    public void printJourney() {
        System.out.print("Journey: " + journeyLength + "\n");
        System.out.println(Arrays.toString(journey));
        System.out.println();
    }

}
