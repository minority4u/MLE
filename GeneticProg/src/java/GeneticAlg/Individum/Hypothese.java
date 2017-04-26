package GeneticAlg.Individum;

import GeneticAlg.BinaryTree;

import static GeneticAlg.Constants.*;


/**
 * Created by minority on 03.11.16.
 */
public class Hypothese implements Comparable<Hypothese> {

    public double fitness;
    public double probability;
    public BinaryTree codeTree;
    int indexOfRoot = 100;
    int sizeOfThisHypothese = sizeOfOneHyp;
    Boolean insertLeft;

    public Hypothese() {
        codeTree = new BinaryTree(sizeOfThisHypothese);
        fitness = 0;
        probability = 0;
        insertLeft = true;
        initialize();

    }

    private void initialize() {


        for (int i = 0; i < sizeOfThisHypothese; i++) {

            addNewOperation();

        }
        codeTree.fillTreeWithLeafs(codeTree.root);
        //codeTree.printTree(codeTree.root);
        //codeTree.byLevel(codeTree.root);
        codeTree.displayTree();
        //codeTree.preorderTraverseTree(codeTree.root);
//        System.out.println("Postordertraverse:");
//        codeTree.postOrderTraverseTree(codeTree.root);
//        System.out.println();
//        System.out.println("Inordertraverse:");
//        codeTree.inOrderTraverseTree(codeTree.root);
    }

    private void addNewOperation() {
        // add operand
        // for better looking trees we toggle adding the nodes right and left
        if (insertLeft) {
            int tempKey = random.nextInt(maxKey) + minKey;
            codeTree.addNode(indexOfRoot - tempKey);
            insertLeft = false;
        } else {
            int tempKey = random.nextInt(maxKey) + minKey;
            codeTree.addNode(indexOfRoot + tempKey);
            insertLeft = true;
        }
    }


    public void calcFitness() {

    }

    public void run() {
        for (int x = 0; x < 1024; x++) {

        }


    }

    public Hypothese mutate() {
        return this;
    }


    @Override
    public int compareTo(Hypothese o) {
        int returnVal = 0;

        if (this.probability < o.probability) {
            returnVal = 1;
        } else if (this.probability > o.probability) {
            returnVal = -1;
        } else if (this.probability == o.probability) {
            returnVal = 0;
        }
        return returnVal;
    }
}
