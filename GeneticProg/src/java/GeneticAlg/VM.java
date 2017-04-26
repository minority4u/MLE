package GeneticAlg;

/**
 * Created by minority on 02.11.16.
 */
public class VM {

    final int MAX = 1000;
    final byte LOAD = 0;
    final byte PUSH = 1;
    final byte POP = 2;
    final byte MUL = 3;
    final byte DIV = 4;
    final byte ADD = 5;
    final byte SUB = 6;
    final byte JIH = 7;

    public void setMem(int[] mem) {
        this.mem = mem;
    }

    public void setStack(int[] stack) {
        this.stack = stack;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }

    public void setSp(int sp) {
        this.sp = sp;
    }

    public void setReg(int reg) {
        this.reg = reg;
    }

    int mem[] = new int[MAX];
    int stack[] = new int[MAX];
    int pc, sp, reg;


    public VM() {
        pc = 0;
        sp = 0;
        reg = 0;
    }

    void push(int x) {
        stack[sp++] = x;
    }

    int pop() {
        if (sp >= 1) {
            sp--;
        }
        return stack[sp];
    }

    public void simulate() {
        do {
            switch (mem[pc] & 7) {
                case LOAD: {
                    reg = mem[pc] >> 3;
                    pc++;
                    break;
                }
                case PUSH: {
                    push(reg);
                    pc++;
                    break;
                }
                case POP: {
                    reg = pop();
                    pc++;
                    break;
                }
                case MUL: {
                    reg = reg * pop();
                    pc++;
                    break;
                }
                case DIV: {
                    reg = reg / pop();
                    pc++;
                    break;
                }
                case ADD: {
                    reg = reg + pop();
                    pc++;
                    break;
                }
                case SUB: {
                    reg = reg - pop();
                    pc++;
                    break;
                }
                case JIH: {
                    if (reg > 0) {
                        pc = (pc + pop() % MAX);
                    } else {
                        pc++;
                    }
                    break;
                }

            }
        } while (pc < MAX && sp >= 0);
    }


}
