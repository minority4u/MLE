package NeuronalNetExampleOne;

import java.util.ArrayList;

/**
 * Created by minority on 28.09.16.
 */
public class Netz {

    ArrayList<Neuron> input = new ArrayList<Neuron>();
    ArrayList<Neuron> hidden = new ArrayList<Neuron>();
    ArrayList<Neuron> output = new ArrayList<Neuron>();

    public Netz(int input, int hidden, int output){
        for (int i = 0; i < input; i++){
            this.input.add(new Neuron(this.hidden));
        }
        for (int i = 0; i < hidden; i++){
            this.hidden.add(new Neuron(this.output));
        }
        for (int i = 0; i < output; i++){
            this.output.add(new Neuron(null));
        }
    }

    public int[] compute(double[] inputVector){
        int[] inputResult = new int[this.input.size()];
        int[] hiddenResult = new int[this.hidden.size()];
        int[] outputResult = new int[this.output.size()];

        for(int i = 0; i < this.input.size(); i++){
            inputResult[i] = this.input.get(i).fire(inputVector);
        }
        for(int i = 0; i < this.hidden.size(); i++){
            hiddenResult[i] = this.hidden.get(i).fire(inputResult);
        }
        for(int i = 0; i < this.output.size(); i++){
            outputResult[i] = this.output.get(i).fire(hiddenResult);
        }

        return outputResult;

    }







}
