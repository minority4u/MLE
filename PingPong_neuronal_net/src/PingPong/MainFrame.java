package PingPong;

import RL.Agent;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;

import javax.swing.JFrame;

import static RL.Constants.ROUNDS_MAX;


@SuppressWarnings("serial")
public class MainFrame extends JFrame {

    public static final int imageWidth = 360;
    public static final int imageHeight = 360;
    public InputOutput inputOutput = new InputOutput(this);
    public boolean stop = false;
    ImagePanel canvas = new ImagePanel();
    ImageObserver imo = null;
    Image renderTarget = null;
    public int mousex, mousey, mousek;
    public int key;
    public Agent QAgent;
    public boolean showGraphic = true;
    public int threadSleepingTime = 100;

    public MainFrame(String[] args) {
        super("PingPong");

        QAgent = new Agent();

        getContentPane().setSize(imageWidth + 50 , imageHeight + 50);
        setSize(imageWidth + 50, imageHeight + 50);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        canvas.img = createImage(imageWidth, imageHeight);

        add(canvas);

        run();
    }

    public void run() {

        // initialize ball and player
        int xBall = 1, yBall = 9, xSchlaeger = 1, xV = -1, yV = 1;
        int score = 0;
        int round = 0;
        int reward = 0;

        // calculates the current state initial
        QAgent.calculateCurrentState(xBall, yBall, xSchlaeger, xV, yV);

        while (!stop) {

            if (showGraphic) {
                // draw field
                inputOutput.fillRect(0, 0, imageWidth, imageHeight, Color.black);
                // draw ball
                inputOutput.fillRect(xBall * 30, yBall * 30, 30, 30, Color.green);
                // draw player
                inputOutput.fillRect(xSchlaeger * 30, 11 * 30 + 20, 90, 10, Color.orange);
            }


            // returns a new action based on the current state
            double action = QAgent.getNextAction();

            // take action a
            // left
            if (action == 0) {
                xSchlaeger--;
            }
            // right
            if (action == 2) {
                xSchlaeger++;
            }
            // acion == 1 == stay --> nothing to do

            // avoid the player to run out of gamefield
            if (xSchlaeger < 0) {
                xSchlaeger = 0;
            }
            if (xSchlaeger > 9) {
                xSchlaeger = 9;
            }

            // move ball
            xBall += xV;
            yBall += yV;

            // ball hits wall --> change direction
            if (xBall > 9 || xBall < 1) {
                xV = -xV;
            }
            if (yBall > 10 || yBall < 1) {
                yV = -yV;
            }

            // ball is at the ground of the gamefield
            if (yBall == 10) {
                round++;
                // bat is 3 fields wide
                if (xSchlaeger == xBall || xSchlaeger == xBall - 1 || xSchlaeger == xBall - 2) {
                    //positive reward
                    reward = 1;
                    score++;

                } else {
                    reward = -1;
                }
                // print the gained score every 1000 rounds
                // for fast forward learning
                if (round % 1000 == 0) {
                    System.out.println("Round: " + round + " Score: " + score);
                    score = 0;
                }
            }


            // calculate new state after action
            QAgent.calculateCurrentState(xBall, yBall, xSchlaeger, xV, yV);

            // Q(st, at)=Q(st, at)+α[rt+γ max aQ(st+1, a)−Q(st, at)]
            QAgent.getRewardForLastAction(reward);
            // reset reward
            reward = 0;

            try {
                Thread.sleep(threadSleepingTime);                 //1000 milliseconds is one second.
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            if (showGraphic) {
                repaint();
                validate();
            }


            // terminal s
            if (score > ROUNDS_MAX) {
                stop = true;
            }
        }

        setVisible(false);
        dispose();
    }


    public void mouseReleased(MouseEvent e) {
        mousex = e.getX();
        mousey = e.getY();
        mousek = e.getButton();
    }

    public void mousePressed(MouseEvent e) {
        mousex = e.getX();
        mousey = e.getY();
        mousek = e.getButton();
    }

    public void mouseExited(MouseEvent e) {
        mousex = e.getX();
        mousey = e.getY();
        mousek = e.getButton();
    }

    public void mouseEntered(MouseEvent e) {
        mousex = e.getX();
        mousey = e.getY();
        mousek = e.getButton();
    }

    public void mouseClicked(MouseEvent e) {
        // toggle graphic
        showGraphic = !showGraphic;
        // change the speed if visible or not
        if (showGraphic) {
            threadSleepingTime = 50;
        } else {
            threadSleepingTime = 0;
        }
        mousex = e.getX();
        mousey = e.getY();
        mousek = e.getButton();
    }

    public void mouseMoved(MouseEvent e) {
        // System.out.println(e.toString());
        mousex = e.getX();
        mousey = e.getY();
        mousek = e.getButton();
    }

    public void mouseDragged(MouseEvent e) {
        mousex = e.getX();
        mousey = e.getY();
        mousek = e.getButton();
    }

    public void keyTyped(KeyEvent e) {
        key = e.getKeyCode();
    }

    public void keyReleased(KeyEvent e) {
        key = e.getKeyCode();
    }

    public void keyPressed(KeyEvent e) {
        //System.out.println(e.toString());
    }

    /**
     * Construct main frame
     *
     * @param args passed to PingPong.MainFrame
     */
    public static void main(String[] args) {
        new MainFrame(args);
    }
}
