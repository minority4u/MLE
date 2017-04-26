import java.io.*;


import java.awt.*;
import javax.swing.*;

public class MNISTReader extends JFrame {

    static int m_z = 12345, m_w = 45678;

    static final int NEURONS = 28 * 28 + 10;
    static final int MAX_PATTERNS = 1000;
    int numLabels;
    int numImages;
    int numRows;
    int numCols;

    double trainLabel[] = new double[MAX_PATTERNS];
    double trainImage[][] = new double[MAX_PATTERNS][28 * 28];
    double weights[][] = new double[NEURONS][NEURONS];
    double output[] = new double[NEURONS];
    double input[] = new double[NEURONS];
    double reconstructed_input[] = new double[NEURONS];

    int randomGen() {
        m_z = Math.abs(36969 * (m_z & 65535) + (m_z >> 16));
        m_w = Math.abs(18000 * (m_w & 65535) + (m_w >> 16));
        return Math.abs((m_z << 16) + m_w);
    }

    public void paint(Graphics g) {
        // left image
        int i = 0;
        for (int colIdx = 0; colIdx < 28; colIdx++) {
            for (int rowIdx = 0; rowIdx < 28; rowIdx++) {
                int c = (int) (input[i++]);
                if (c > 0.0) {
                    g.setColor(Color.red);
                } else {
                    g.setColor(Color.black);
                }

                g.fillRect(10 + rowIdx * 10, 10 + colIdx * 10, 8, 8);
            }
        }
        // left control-bits
        for (int t = 0; t < 10; t++) {
            int c = (int) (input[i++]);
            if (c > 0.0) {
                g.setColor(Color.red);
            } else {
                g.setColor(Color.black);
            }
            g.fillRect(10 + t * 10, 10 + 28 * 10, 8, 8);
        }
        // middle image
        i = 0;
        for (int colIdx = 0; colIdx < 28; colIdx++) {
            for (int rowIdx = 0; rowIdx < 28; rowIdx++) {
                int c = (int) (output[i++] + 0.5);
                if (c > 0.0) {
                    g.setColor(Color.blue);
                } else {
                    g.setColor(Color.black);
                }

                g.fillRect(300 + 10 + rowIdx * 10, 10 + colIdx * 10, 8, 8);
            }
        }
        // middle control-bits
        for (int t = 0; t < 10; t++) {
            int c = (int) (output[i++] + 0.5);
            if (c > 0.0) {
                g.setColor(Color.blue);
            } else {
                g.setColor(Color.black);
            }
            g.fillRect(300 + 10 + t * 10, 10 + 28 * 10, 8, 8);
        }
        // right image
        i = 0;
        for (int colIdx = 0; colIdx < 28; colIdx++) {
            for (int rowIdx = 0; rowIdx < 28; rowIdx++) {
                int c = (int) (reconstructed_input[i++] + 0.5);
                if (c > 0.0) {
                    g.setColor(Color.blue);
                } else {
                    g.setColor(Color.black);
                }

                g.fillRect(600 + 10 + rowIdx * 10, 10 + colIdx * 10, 8, 8);
            }
        }
        // right control-image
        for (int t = 0; t < 10; t++) {
            int c = (int) (reconstructed_input[i++] + 0.5);
            if (c > 0.0) {
                g.setColor(Color.blue);
            } else {
                g.setColor(Color.black);
            }
            g.fillRect(600 + 10 + t * 10, 10 + 28 * 10, 8, 8);
        }

    }

    /**
     * @param args args[0]: label file; args[1]: data file.
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException,
            InterruptedException {


        MNISTReader frame = new MNISTReader();

        frame.readMnistDatabase();
        frame.setSize(900, 350);
        System.out.println("Learning step:");
        frame.trainOrTestNet(true, 1000, frame);


        System.out.println("Teststep:");
        frame.trainOrTestNet(false, 1000, frame);
    }

    public void init(double weights[][]) {
        for (int t = 0; t < NEURONS; t++) {
            for (int neuron = 0; neuron < NEURONS; neuron++) {
                weights[neuron][t] = randomGen() % 2000 / 1000.0 - 1.0;
                //	System.out.println("weight["+neuron+"]["+t+"]="+weights[neuron][t]);
            }
        }
    }

    public void activateForward(double in[], double w[][], double out[]) {
        double summe =0;

        for (int t = 0; t < NEURONS; t++) {
            summe = 0;
            for (int j = 0; j < NEURONS; j++) {
                summe += w[t][j] * in[j];
            }
            // Standardsigmoide
            out[t] = (1/(1+Math.exp(-summe)));
        }
    }

    public void activateReconstruction(double rec[], double w[][], double out[]) {

        double summe =0;

        for (int t = 0; t < NEURONS; t++) {
            summe = 0;
            for (int j = 0; j < NEURONS; j++) {
                summe += w[j][t] * out[j];
            }
            // Standardsigmoide
            rec[t] = (1/(1+Math.exp(-summe)));
        }




    }

    public void contrastiveDivergence(double inp[], double out[], double rec[], double w[][]) {

        for (int j = 0; j < NEURONS; j++) {
            for (int i = 0; i< NEURONS; i++){

                // Perzeptron Lernregel
//                if (rec[j] <= 0.5 && inp[j] >= 0.5 && out[i] >= 0.5){
//                    w[i][j] = w[i][j] + out[i];
//                }
//                if (rec[j] >= 0.5 && inp[j] <= 0.5 && out[i] >= 0.5){
//                    w[i][j] = w[i][j] - out[i];
//                }

                // Delta Lernregel
                w[j][i] = w[j][i] + 0.5 * (inp[i] - rec[i]) * out[j];

            }

        }


    }

    void trainOrTestNet(boolean train, int maxCount, MNISTReader frame) {
        int correct = 0;

        if (train) {
            init(weights);
        }
        int pattern = 0;
        for (int count = 1; count < maxCount; count++) {
            // --- training phase

            for (int t = 0; t < NEURONS - 10; t++) {
                input[t] = trainImage[pattern % 100][t]; // initialize original pattern
            }
            for (int t = NEURONS - 10; t < NEURONS; t++) {
                input[t] = 0;
            }
            if (train) {
                // --- use the label also as input!
                if (trainLabel[pattern % 100] >= 0 && trainLabel[pattern % 100] < 10) {
                    input[NEURONS - 10 + (int) trainLabel[pattern % 100]] = 1.0;
                }
            }

//			drawActivity(0,0,input,red,green,blue);

            // --- Contrastive divergence
            // Activation
            input[0] = 1;                    // bias neuron!
            activateForward(input, weights, output); // positive Phase
            output[0] = 1;                    // bias neuron!

//			drawActivity(300,0,output,red,green,blue);

            activateReconstruction(reconstructed_input, weights, output); // negative phase/ reconstruction

//			drawActivity(600,0,reconstructed_input,red,green,blue);
            if (train) {
                contrastiveDivergence(input, output, reconstructed_input, weights);
            }

            if (count % 111 == 0) {
                System.out.println("Zahl:" + trainLabel[pattern % 100]);
                System.out.println("Trainingsmuster:" + count + "                 Erkennungsrate:" + (float) (correct) / (float) (count) + " %");


                frame.validate();
                frame.setVisible(true);
                frame.repaint();
                try {
                    Thread.sleep(20);                 //20 milliseconds is one second.
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }

            }

            if (!train) {
                int number = 0;
                for (int t = NEURONS - 10; t < NEURONS; t++) {
                    if (reconstructed_input[t] > reconstructed_input[NEURONS - 10 + number]) {
                        number = t - (NEURONS - 10);
                    }
                }

                if (frame.trainLabel[pattern % 100] == number) {
                    System.out.println("Muster: " + frame.trainLabel[pattern % 100] + ", Erkannt: " + number + " KORREKT!!!\n");
                    correct++;
                } else {
                    System.out.println("Muster: " + frame.trainLabel[pattern % 100] + ", Erkannt: " + number);
                }

            }

            pattern++;
        }


    }


    public void readMnistDatabase() throws IOException {
        {
            DataInputStream labels = new DataInputStream(new FileInputStream(
                    "train-labels-idx1-ubyte"));
            DataInputStream images = new DataInputStream(new FileInputStream(
                    "train-images-idx3-ubyte"));
            int magicNumber = labels.readInt();
            if (magicNumber != 2049) {
                System.err.println("Label file has wrong magic number: "
                        + magicNumber + " (should be 2049)");
                System.exit(0);
            }
            magicNumber = images.readInt();
            if (magicNumber != 2051) {
                System.err.println("Image file has wrong magic number: "
                        + magicNumber + " (should be 2051)");
                System.exit(0);
            }
            numLabels = labels.readInt();
            numImages = images.readInt();
            numRows = images.readInt();
            numCols = images.readInt();

            long start = System.currentTimeMillis();
            int numLabelsRead = 0;
            int numImagesRead = 0;

            while (labels.available() > 0 && numLabelsRead < MAX_PATTERNS) {// numLabels

                byte label = labels.readByte();
                numLabelsRead++;
                trainLabel[numImagesRead] = label;
                double pos = 0, neg = 0;
                int i = 0;
                for (int colIdx = 0; colIdx < numCols; colIdx++) {
                    for (int rowIdx = 0; rowIdx < numRows; rowIdx++) {
                        if (images.readUnsignedByte() > 0) {
                            trainImage[numImagesRead][i++] = 1.0;
                        } else {
                            trainImage[numImagesRead][i++] = 0;
                        }

                    }
                }

                numImagesRead++;

                // At this point, 'label' and 'image' agree and you can do
                // whatever you like with them.

                if (numLabelsRead % 10 == 0) {
                    System.out.print(".");
                }
                if ((numLabelsRead % 800) == 0) {
                    System.out.print(" " + numLabelsRead + " / " + numLabels);
                    long end = System.currentTimeMillis();
                    long elapsed = end - start;
                    long minutes = elapsed / (1000 * 60);
                    long seconds = (elapsed / 1000) - (minutes * 60);
                    System.out
                            .println("  " + minutes + " m " + seconds + " s ");

                }

            }

            System.out.println();
            long end = System.currentTimeMillis();
            long elapsed = end - start;
            long minutes = elapsed / (1000 * 60);
            long seconds = (elapsed / 1000) - (minutes * 60);
            System.out.println("Read " + numLabelsRead + " samples in "
                    + minutes + " m " + seconds + " s ");

            labels.close();
            images.close();

        }

    }
    /*
	  public static void writeFile(String fileName, byte[] buf)
	    {
			
			FileOutputStream fos = null;
			
			try
			{
			   fos = new FileOutputStream(fileName);
			   fos.write(buf);
			}
			catch(IOException ex)
			{
			   System.out.println(ex);
			}
			finally
			{
			   if(fos!=null)
			      try
			      {
			         fos.close();
			      }
			      catch(Exception ex)
			      {
			      }
			}
	    }
*/
}
