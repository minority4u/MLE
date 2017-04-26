package RL;

import java.util.Random;

/**
 * Created by minority on 28.11.16.
 */
public class Constants {

    public static Random random = new Random();


    // TODO: how many states?!?
    // new state calculation:
    // 11*10*10*2*2 + 10*10*2*2 + 10*2*2 + 2*2 + 2 = 4846

    public static int STATES = 5000;
    // defines max index size for Q[states][actions]
    public static int ACTIONS = 3;

    public static int BALL_Y_MAX = 10;
    public static int BALL_X_MAX = 10;
    public static int SCHLAEGER_X_MAX = 10;
    public static int V_X_MAX = 2;
    public static int V_Y_MAX = 2;
    public static double ALPHA = 0.1;
    public static double GAMMA = 0.5;
    public static double GREEDY_ACTION_PART = 0.05;
    // + 0.000xxxx to ramdomize the values
    public static double INITIAL_REWARD_PER_STATE = 0.001;
    public static int ROUNDS_MAX = 1000000;


}
