package mle.hillclimb;


/**
 * Created by minority on 06.10.16.
 */


public class Map {

    private double[][] map;
    private Journey currentJourney;
    private int numberOfChanges;
    private int size;
    private int minDistance;
    private int maxDistance;
    private float temperature;
    private float epsilon;


    public Map(int size, int numberOfChanges) {
        this.size = size;
        // simulated Annealing
        this.numberOfChanges = numberOfChanges;
        this.temperature = numberOfChanges;
        this.epsilon = temperature / numberOfChanges;
        this.currentJourney = new Journey(size);
        initialize();
    }

    private void initialize() {

        this.minDistance = 10;
        this.maxDistance = 99;
        this.map = new double[size][size];

        // initialize cities
        for (int i = 0; i < map.length; i++) {
            // initialize distances
            for (int j = 1; j < map[i].length; j++) {
                // no distance necessary for travelling from Berlin to Berlin
                if (i != j) {
                    int dist = getRandomDistance();
                    map[i][j] = map[j][i] = dist;
                }
            }
        }

        // initialize journey
        for (int x = 0; x < size; x++) {
            //journey[x] = x;
            this.currentJourney.journey[x] = x;

        }

        calculateJourney();
        System.out.println("Initial Journey");
        this.currentJourney.printJourney();

    }

    private void resetMap(){
        this.currentJourney = new Journey(numberOfChanges);
        // shuffle start order of this journey
        this.currentJourney.resetJourney();
        this.temperature = numberOfChanges;
        this.epsilon = temperature / numberOfChanges;

    }




    // run the hill climber + simulateded annealing and return the distance
    public Journey runHillclimberWithNewJourneyOrder(){

        //reset Map values and shuffle journey
        resetMap();

        // modify journey n times and check if the journey gets shorter
        for (int i = 0; i < this.numberOfChanges; i++){
            modifyJourneySequence();
        }
        return this.currentJourney;
    }





    private int getRandomDistance() {
        return minDistance + (int) (Math.random() * ((maxDistance - minDistance) + 1));
    }

    private void calculateJourney() {
        //actualDistanceForTheJourney = 0;
        currentJourney.journeyLength = 0;
        double len = this.currentJourney.journey.length;
        int actualCity, nextCity;
        for (int i = 0; i <len; i++) {

            // There is one distance less than cities for each journey
            // we have to avoid index out of bound exception
            if (i < len - 1) {
                actualCity = currentJourney.journey[i];
                nextCity = currentJourney.journey[i + 1];
                currentJourney.journeyLength += map[actualCity][nextCity];
            } else {
                // connect the last city with the first.
                actualCity = currentJourney.journey[i];
                nextCity = currentJourney.journey[0];
                currentJourney.journeyLength += map[actualCity][nextCity];
            }
        }
    }


    private void modifyJourneySequence() {
        double distNew = 0;
        double distOld = currentJourney.journeyLength;
        int firstCityToSwap, secondCityToSwap;

        // get two random cities to swap
        firstCityToSwap = (int) (Math.random() * size);
        secondCityToSwap = (int) (Math.random() * size);

        // swap two elements and calculate the new distance
        swapToCitiesInTheJourneyArray(firstCityToSwap, secondCityToSwap);
        distNew = currentJourney.journeyLength;

        // undo the last swap if the new journey length is longer
        if (distNew <= distOld) {
            //System.out.print("better Distance: ");
            //System.out.println(currentJourney.journeyLength);
            // Simulated Annealing, keep the new journey order due to the annealing factor
        } else if (randomProzentSmallerThanAnnealing(distNew, distOld)) {
            //System.out.print("simulated annealing Distance: ");
            //System.out.println(currentJourney.journeyLength);
            //System.out.println(currentJourney.journeyLength);
        } else {
            swapToCitiesInTheJourneyArray(secondCityToSwap, firstCityToSwap);
        }
        this.temperature = temperature - epsilon;
    }

    //
    private boolean randomProzentSmallerThanAnnealing(double newDist, double oldDist) {

        float annealing = (float) Math.exp(-1 * (((newDist - oldDist) / this.temperature)));
        float randomPercent = (float) Math.random();
        //System.out.println("randomPercent: " + randomPercent);
        //System.out.println("Annealing: " + annealing);

        if (randomPercent < annealing) {
            return true;
        } else {
            return false;
        }
    }


    private void swapToCitiesInTheJourneyArray(int firstCity, int secondCity) {
        int temp;
        temp = currentJourney.journey[firstCity];
        currentJourney.journey[firstCity] = currentJourney.journey[secondCity];
        currentJourney.journey[secondCity] = temp;

        calculateJourney();
    }




    public void printArray() {

        System.out.print("City-Grid");
        System.out.println();
        System.out.print("---------------------------------");
        this.currentJourney.printJourney();
        System.out.print("Actual Distance: ");
        System.out.println(currentJourney.journeyLength);
        System.out.println();


        for (int i = 0; i < map.length; ++i) {
            for (int j = 0; j < map[i].length; ++j) {
                // change the distance field with distance to the city itself for better usability
                if (i == j) {
                    System.out.print("[" + "---" + "]");
                    System.out.print(" ");
                } else {
                    System.out.print("[" + this.map[i][j] + "]");
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }


}
