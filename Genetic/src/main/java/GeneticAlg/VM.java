package GeneticAlg;

import java.util.ArrayList;

import static GeneticAlg.Constants.MAXOPERATIONPERVMSIMULATION;
import static GeneticAlg.Constants.MINSIZEOFTHEPRIME;

/**
 * Created by minority on 02.11.16.
 */
public class VM {

    int MAX = 1000;

    public int getCounter() {
        return counter;
    }

    int counter = 0;
    final byte LOAD = 0;
    final byte PUSH = 1;
    final byte POP = 2;
    final byte MUL = 3;
    final byte DIV = 4;
    final byte ADD = 5;
    final byte SUB = 6;
    final byte JIH = 7;

    // change the vm parameters according to the opcode length
    // set vm memory = opcode
    public void setMemAndResizeMAX(int[] mem) {
        this.mem = mem;
        this.MAX = mem.length;
        this.stack = new int[MAX];
    }

    int mem[] = new int[MAX];
    int stack[] = new int[MAX];
    int pc, sp, reg;
    ArrayList primeNumbers;

    public ArrayList getPrimeNumbers() {
        return primeNumbers;
    }


    public VM() {
        primeNumbers = new ArrayList<>();
        pc = 0;
        sp = 0;
        reg = 0;
    }

    // we add every pushed prime number to primeNumbersList for later fitness calculation
    void push(int x) {
        // avoid nullpointer - ignore all other push statements
        if (sp >= 0 && sp < MAX) {
            stack[sp++] = x;
            addIfPrimeToPrimeNumbers(x);
        }
    }

    int pop() {
        if (sp >= 1) {
            sp--;
        }
        return stack[sp];
    }

    public void simulate() {
        int pop = 0;
        counter = 0;
        do {
            //System.out.println("VM: sp= " + sp + " pc= " + pc);
            counter++;
            try {
                switch (mem[pc] & 7) {
                    case LOAD: {
                        reg = mem[pc] >> 3;
                        //System.out.println("LOAD " + reg);
                        pc++;
                        break;
                    }
                    case PUSH: {
                        //System.out.println("PUSH " + reg + " to stack[" + sp + "]");
                        push(reg);
                        pc++;
                        break;
                    }
                    case POP: {
                        //System.out.print("POP ");
                        reg = pop();
                        //System.out.println(reg + " from stack[" + sp + "]");
                        pc++;
                        break;
                    }
                    case MUL: {
                        pop = pop();
                        //System.out.print("MUL " + reg + "*" + pop + "=");
                        reg = reg * pop;
                        push(reg);
                        //System.out.println(reg);
                        pc++;
                        break;
                    }
                    case DIV: {
                        pop = pop();
                        //System.out.print("DIV " + reg + "/" + pop + "=");
                        if (pop != 0) {
                            reg = reg / pop;
                        }
                        //System.out.println(reg);
                        push(reg);
                        pc++;
                        break;
                    }
                    case ADD: {
                        pop = pop();
                        //System.out.print("ADD " + reg + "+" + pop + "=");
                        reg = reg + pop;
                        //System.out.println(reg);
                        push(reg);
                        pc++;
                        break;
                    }
                    case SUB: {
                        pop = pop();
                        //System.out.print("SUB " + reg + "-" + pop + "=");
                        reg = reg - pop;
                        //System.out.println(reg);
                        push(reg);
                        pc++;
                        break;
                    }
                    case JIH: {
                        //System.out.println("JIH");
                        if (reg > 0) {
                            // TODO: infinite JIH if pop() = 0
                            pop = pop();
                            if (pop != 0 && ((pc + pop) > 0)) {
                                //System.out.println("pc= " + pc + " pop= " + pop + " MAX= " + MAX );
                                // TODO: ArrayIndexOutOfBoundException if reg + pop() = negative
                                pc = ((pc + pop) % MAX);
                                //System.out.println("new pc : " + pc);
                            }
                            pc++;

                        } else {
                            pc++;
                        }
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Something is wrong - check your vm");
            }
        } while (pc < MAX && pc > 0 && counter < MAXOPERATIONPERVMSIMULATION && sp >= 0);
    }


    // If the given elem is prime and not in primeNumbers add it
    private void addIfPrimeToPrimeNumbers(float elem) {
        // avoid negative numbers
        float elemAbs = Math.abs(elem);
        // only prime numbers bigger than the defined MINSIZEOFTHEPRIME will be added
        if (elem > MINSIZEOFTHEPRIME) {
            // check if elem is prime
            if (isPrime(elemAbs)) {
                // add only new prime numbers
                if (!primeNumbers.contains(elemAbs)) {
                    // new prime number --> add
                    primeNumbers.add(elemAbs);
                } else {
                    // prime is known in primeNumbers
                    // nothing to do
                }
            }
        }
    }

    private boolean isPrime(float elem) {
        //check if elem is a multiple of 2
        if (elem % 2 == 0) return false;
        //if not, then just check the odds
        for (int i = 3; i * i <= elem; i += 2) {
            if (elem % i == 0)
                return false;
        }
        return true;
    }

    // call this method after each simulation
    public void reset() {
        this.mem = new int[MAX];
        this.stack = new int[MAX];
        this.primeNumbers = new ArrayList<>();
        pc = 0;
        sp = 0;
        reg = 0;
    }
}
