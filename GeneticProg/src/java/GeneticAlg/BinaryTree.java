package GeneticAlg;

import java.util.*;

import static GeneticAlg.Constants.*;


/**
 * Created by minority on 03.11.16.
 */
public class BinaryTree {

    public BinaryTree(int size) {
        this.random = new Random();
        this.treeSize = size;
    }


    public Node root;
    public int treeSize;
    Random random;


    //public void addNode(int key, String name) {
    public void addNode(int key) {

        // Create a new Node and initialize it
        //

        Node newNode;

/*        if (this.getSize(this.root) >= treeSize){
            // if treesize is reached, add only a leaf = value
            newNode = new Node(key,"" + random.nextInt(maxValue) + minValue);
        } else {
            newNode = new Node(key);
        }*/

        newNode = new Node(key);

/*        if (newNode.isOperand){
            //System.out.println("LEAF LEAF");
            newNode.leftChild = new Node(newNode.key-1, true);
            newNode.rightChild = new Node(newNode.key+1, true);
        }*/

        // If there is no root this becomes root

        if (root == null) {

            root = newNode;

        } else {

            // Set root as the Node we will start
            // with as we traverse the tree

            Node focusNode = root;

            // Future parent for our new Node

            Node parent;

            while (true) {

                // root is the top parent so we start
                // there

                parent = focusNode;

                // Check if the new node should go on
                // the left side of the parent node

                if (key < focusNode.key) {

                    // Switch focus to the left child

                    focusNode = focusNode.leftChild;

                    // If the left child has no children

                    if (focusNode == null) {

                        // then place the new node on the left of it

                        parent.leftChild = newNode;
                        return; // All Done

                    }

                } else { // If we get here put the node on the right

                    focusNode = focusNode.rightChild;

                    // If the right child has no children

                    if (focusNode == null) {

                        // then place the new node on the right of it

                        parent.rightChild = newNode;
                        return; // All Done

                    }

                }

            }
        }

    }

    // All nodes are visited in ascending order
    // Recursion is used to go to one node and
    // then go to its child nodes and so forth

    public void inOrderTraverseTree(Node focusNode) {

        if (focusNode != null) {

            if (focusNode.isOperand) {
                System.out.print("(");
            }

            // Traverse the left node
            inOrderTraverseTree(focusNode.leftChild);

            // Visit the currently focused on node

            System.out.print(focusNode);

            // Traverse the right node
            inOrderTraverseTree(focusNode.rightChild);
            System.out.print(")");

        }

    }


    public void preorderTraverseTree(Node focusNode) {

        if (focusNode != null) {

            System.out.print(focusNode);

            preorderTraverseTree(focusNode.leftChild);
            preorderTraverseTree(focusNode.rightChild);

        }

    }

    public void postOrderTraverseTree(Node focusNode) {

        if (focusNode != null) {

            postOrderTraverseTree(focusNode.leftChild);
            postOrderTraverseTree(focusNode.rightChild);

            System.out.print(focusNode);

        }

    }

    public Node findNode(int key) {

        // Start at the top of the tree
        Node focusNode = root;
        // While we haven't found the Node
        // keep looking
        while (focusNode.key != key) {
            // If we should search to the left
            if (key < focusNode.key) {
                // Shift the focus Node to the left child
                focusNode = focusNode.leftChild;
            } else {
                // Shift the focus Node to the right child
                focusNode = focusNode.rightChild;
            }
            // The node wasn't found
            if (focusNode == null)
                return null;
        }
        return focusNode;
    }

    public int getSize(Node root) {
        if (root == null) {
            return 0;
        }
        return 1 + getSize(root.leftChild) + getSize(root.rightChild);
    }

    public void printTree(Node tmpRoot) {

        Queue<Node> currentLevel = new LinkedList<Node>();
        Queue<Node> nextLevel = new LinkedList<Node>();

        currentLevel.add(tmpRoot);

        while (!currentLevel.isEmpty()) {
            Iterator<Node> iter = currentLevel.iterator();
            while (iter.hasNext()) {
                Node currentNode = iter.next();
                if (currentNode.leftChild != null) {
                    nextLevel.add(currentNode.leftChild);
                }
                if (currentNode.rightChild != null) {
                    nextLevel.add(currentNode.rightChild);
                }
                System.out.print(currentNode.value + " ");
            }
            System.out.println();
            currentLevel = nextLevel;
            nextLevel = new LinkedList<Node>();

        }
    }

    public void byLevel(Node root) {
        Queue<Node> level = new LinkedList<>();
        level.add(root);
        while (!level.isEmpty()) {
            Node node = level.poll();
            System.out.print(node);
            if (node.leftChild != null)
                level.add(node.leftChild);
            if (node.rightChild != null)
                level.add(node.rightChild);
        }
    }

    public void displayTree() {
        Stack globalStack = new Stack();
        globalStack.push(root);
        int emptyLeaf = 32;
        boolean isRowEmpty = false;
        System.out.println("****......................................................****");
        while (isRowEmpty == false) {

            Stack localStack = new Stack();
            isRowEmpty = true;
            for (int j = 0; j < emptyLeaf; j++)
                System.out.print(' ');
            while (globalStack.isEmpty() == false) {
                Node temp = (Node) globalStack.pop();
                if (temp != null) {
                    System.out.print(temp.getValue());
                    localStack.push(temp.leftChild);
                    localStack.push(temp.rightChild);
                    if (temp.leftChild != null || temp.rightChild != null)
                        isRowEmpty = false;
                } else {
                    System.out.print("--");
                    localStack.push(null);
                    localStack.push(null);
                }
                for (int j = 0; j < emptyLeaf * 2 - 2; j++)
                    System.out.print(' ');
            }
            System.out.println();
            emptyLeaf /= 2;
            while (localStack.isEmpty() == false)
                globalStack.push(localStack.pop());
        }
        System.out.println("****......................................................****");
    }

    public void fillTreeWithLeafs(Node currentRoot) {


        if (currentRoot.rightChild != null) {
            fillTreeWithLeafs(currentRoot.rightChild);
        } else {
            if (currentRoot.isOperand) {
                currentRoot.rightChild = new Node(currentRoot.key + 1, true);
            }
        }
        if (currentRoot.leftChild != null) {
            fillTreeWithLeafs(currentRoot.leftChild);
        } else {
            if (currentRoot.isOperand) {
                currentRoot.leftChild = new Node(currentRoot.key - 1, true);
            }
        }

    }


}

class Node {

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public Node getLeftChild() {
        return leftChild;
    }

    public Node getRightChild() {
        return rightChild;
    }

    int key;
    String value;
    Boolean isOperand;
    //Random random;

    public Node leftChild;
    public Node rightChild;

    Node(int key, String value) {
        //this.random = new Random();
        this.key = key;
        this.value = value;

    }

    Node(int key) {
        //this.random = new Random();
        this.key = key;
        this.isOperand = true;
        this.value = getRandomOperatorValue();
    }

    Node(int key, boolean getLeaf) {
        //System.out.println("Fill with leaf");
        //this.random = new Random();
        this.isOperand = false;
        this.key = key;
        this.value = getRandomLeafValue();
    }


    // no more used now
    private String getRandomValue() {
        // decide if we insert an operation or number
        // if (random.nextBoolean()){
        if (false) {
            this.isOperand = false;
            return getRandomLeafValue();
        } else {
            this.isOperand = true;
            return getRandomOperatorValue();
        }
    }

    private String getRandomLeafValue() {
        if (random.nextBoolean()) {
            return "x";
        } else {
            return "" + (random.nextInt(maxValue) + minValue);
        }

    }


    private String getRandomOperatorValue() {

        int operator = (random.nextInt(4) + 0);
        // System.out.println("Operator: " + operator);
        switch (operator) {
            case 0:
                return "MUL ";
            case 1:
                return "DIV ";
            case 2:
                return "ADD ";
            case 3:
                return "SUB ";
            default:
                return "error";
        }
    }


    public String toString() {

        return value;
        //return "key: " + key + " value: " + value + "\n";

        //return value + " has the key " + key;

        //return "key: " + key + " value: " + value + "\nLeftChild: " + leftChild +
        //"\nRight Child: " + rightChild + "\n";


        //return value + " has the key " + key + "\nLeft Child: " + leftChild +
        //"\nRight Child: " + rightChild + "\n";

    }

}