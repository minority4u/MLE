package GeneticAlg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by minority on 18.11.16.
 */
public class OpcodeLoader {
    String path;
    String file;
    String opcodeAsString;

    int[] opCode;

    public int[] getOpCode() {
        return opCode;
    }

    public OpcodeLoader(String path) {
        this.path = path;
        this.file = "";
        this.opcodeAsString = "";
        try {
            loadFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        extractOpCodeFromFile();
        transformOpcodeFromStringtoInt();
    }

    private void loadFile() throws IOException {
        Scanner s = null;
        try {
            s = new Scanner(new BufferedReader(new FileReader(path)));

            while (s.hasNext()) {
                file += s.next();
            }
        } finally {
            if (s != null) {
                s.close();
            }
        }
    }

    // search for the opcode with regex -->  "[LOAD1231,ADD,PUSH,...]"
    // returns  "LOAD1231,ADD,PUSH,..." as string
    private void extractOpCodeFromFile() {
        String patternString = "\\[(.*?)\\]";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(file);
        if (matcher.find()) {
            opcodeAsString = matcher.group(1);
        }

    }
    // spits the opcode string by ","
    // and transform the string commands to int
    private void transformOpcodeFromStringtoInt() {
        List<String> opCodes = Arrays.asList(opcodeAsString.split(","));
        opCode = new int[opCodes.size()];
        int index = 0;
        int cmd = 0;

        for (String opcode : opCodes) {

            switch (opcode) {
                case "PUSH":
                    cmd = 1;
                    break;
                case "POP":
                    cmd = 2;
                    break;
                case "MUL":
                    cmd = 3;
                    break;
                case "DIV":
                    cmd = 4;
                    break;
                case "ADD":
                    cmd = 5;
                    break;
                case "SUB":
                    cmd = 6;
                    break;
                case "JIH":
                    cmd = 7;
                    break;
                default:
                    // extract the loaded number
                    opcode = opcode.replaceAll("LOAD", "");
                    cmd = Integer.parseInt(opcode);
            }
            opCode[index] = cmd;
            index++;
        }
    }
}
