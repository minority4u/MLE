package RL; /**
 * Created by minority on 28.11.16.
 */

import static RL.Constants.*;


public class Agent {
    // Q[state][action]
    double Q[][];
    private int currentState;
    private int lastState;
    private int lastAction;


    public Agent() {
        // init with unique values
        currentState = -1;
        lastState = -1;
        lastAction = -1;
        initialize();
    }

    private void initialize() {
        this.Q = new double[STATES][ACTIONS];

        // initialize Q
        // there are 3 possible actions for each state i
        // Q[i][0] = left, Q[i][1] = stay, Q[i][2] = right
        for (int i = 0; i < STATES; i++) {
            for (int j = 0; j < ACTIONS; j++) {
                // small and random values
                Q[i][j] = INITIAL_REWARD_PER_STATE + random.nextDouble()/1000;
                // alls values are the
                //Q[i][j] = INITIAL_REWARD_PER_STATE;
            }
        }
    }


    // returns 0 (left), 1 (stay), 2 (right)
    public double getNextAction() {
        double moveLeft = 0, moveRight = 0, stay = 0;

        // start with a random action if all actions have the same expected reward
        this.lastAction = random.nextInt(3);

        // TODO: we get negative reward for this greedy decisions
        // in 5% of all decisions we do a random greedy action
        if (random.nextDouble() < GREEDY_ACTION_PART) {
            return lastAction;
        }

        moveLeft = getQ(currentState, 0);
        stay = getQ(currentState, 1);
        moveRight = getQ(currentState, 2);


        if (moveLeft > Math.max(stay, moveRight)) {
            // move left
            lastAction = 0;
        }
        if (stay > Math.max(moveLeft, moveRight)) {
            // stay
            lastAction = 1;
        }
        if (moveRight > Math.max(moveLeft, stay)) {
            // move right
            lastAction = 2;
        }
        // return best action or random if decision is not clear
        return lastAction;
    }


    private double getQ(int state, int action) {
        return this.Q[state][action];
    }

    // reward the last action
    public void getRewardForLastAction(int reward) {
        // dont reward the first move
        if (lastState != -1) {
            double maxNewAction = Math.max(getQ(currentState, 0), getQ(currentState, 1));
            maxNewAction = Math.max(maxNewAction, getQ(currentState, 2));
            Q[lastState][lastAction] = getQ(lastState, lastAction) + ALPHA * (reward + GAMMA * maxNewAction - getQ(lastState, lastAction));
        }
    }

    // calculates the currentState and remember the lastState
    public int calculateCurrentState(int xBall, int yBall, int xSchlaeger, int xV, int yV) {
        int state = 0;
        // shift currentState to last state
        if (currentState != -1) {
            this.lastState = this.currentState;
        }

        // TODO: xV and yV could be +1 or -1 - done: 1 = 1; -1 = 0
        // we only have positive states, convert the speed to positive values
        // between ( 1 = 1; -1 = 0)
        if (xV == -1) {
            xV = 0;
        }
        if (yV == -1) {
            yV = 0;
        }

        // shrink all 5 dimensions to one state
        state += xBall * BALL_Y_MAX * SCHLAEGER_X_MAX * V_X_MAX * V_Y_MAX;
        state += yBall * SCHLAEGER_X_MAX * V_X_MAX * V_Y_MAX;
        state += xSchlaeger * V_X_MAX * V_Y_MAX;
        state += xV * V_Y_MAX;
        state += yV;

        this.currentState = state;
        return this.currentState;

    }
}

// transform a state back into each dimension
//        xBall = state /(BALL_Y_MAX * SCHLAEGER_X_MAX * V_X_MAX * V_Y_MAX);
//        state -= xBall * BALL_Y_MAX * SCHLAEGER_X_MAX * V_X_MAX * V_Y_MAX;
//        yBall = state / (SCHLAEGER_X_MAX * V_X_MAX * V_Y_MAX);
//        state -= yBall            * SCHLAEGER_X_MAX * V_X_MAX * V_Y_MAX;
//        xSchlaeger = state/(V_X_MAX * V_Y_MAX);
//        state -=  xSchlaeger                       * V_X_MAX * V_Y_MAX;
//        xV = state / (V_Y_MAX);
//        state -= xV                                       * V_Y_MAX;
//        yV = state;
