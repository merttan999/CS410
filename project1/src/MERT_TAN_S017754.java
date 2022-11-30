import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

public class MERT_TAN_S017754 {
    public static void main(String[] args) throws IOException {
        URL path = MERT_TAN_S017754.class.getResource("NFA1.txt");
        File file = new File(path.getFile());
        Scanner scan = new Scanner(new FileReader(file));
        ArrayList<String> text = new ArrayList<String>();
        ArrayList<String> alphabet = new ArrayList<String>();
        ArrayList<String> states = new ArrayList<String>();
        ArrayList<String> transitions = new ArrayList<String>();
        String startState = "";
        ArrayList<String> finalStates = new ArrayList<String>();

        //reading txt file
        while (scan.hasNextLine()){
            text.add(scan.nextLine());
        }
        for(int i = text.indexOf("ALPHABET"); i< text.indexOf("STATES")-1; i++){
            alphabet.add(text.get(i+1));
        }
        for(int i = text.indexOf("STATES"); i< text.indexOf("START")-1; i++){
            states.add(text.get(i+1));
        }
        for(int i = text.indexOf("START"); i< text.indexOf("FINAL")-1; i++){
            startState = text.get(i+1);
        }
        for(int i = text.indexOf("FINAL"); i< text.indexOf("TRANSITIONS")-1; i++){
            finalStates.add(text.get(i+1));
        }
        for(int i = text.indexOf("TRANSITIONS"); i< text.indexOf("END")-1; i++){
            transitions.add(text.get(i+1));
        }


        // Creating the NFA Diagram
        String[][] NFADiagram = new String[states.size()][alphabet.size()];
        for(int i = 0; i< transitions.size() ; i++){
            String line = transitions.get(i);
            String state = line.split(" ")[0];
            String alphabetChar = line.split(" ")[1];
            String result = line.split(" ")[2];
            if(NFADiagram[states.indexOf(state)][alphabet.indexOf(alphabetChar)] == null) {
                NFADiagram[states.indexOf(state)][alphabet.indexOf(alphabetChar)] = result;
            } else {
                NFADiagram[states.indexOf(state)][alphabet.indexOf(alphabetChar)] += result;
            }
        }

        //Converting to DFA
        ArrayList<String> DFAStates = new ArrayList<String>();
        ArrayList<String> DFAAlphabet = new ArrayList<String>();
        ArrayList<String> DFATransitions = new ArrayList<String>();
        ArrayList<String> DFAFinalState = new ArrayList<String>();
        DFAStates.add(states.get(0));
        DFAAlphabet.addAll(alphabet);
        for(int i = 0; i< alphabet.size(); i++) {
            if(NFADiagram[0][i]!=null) {
                DFATransitions.add(states.get(0) + " " + alphabet.get(i) + " " + NFADiagram[0][i]);
            } else {
                DFATransitions.add(states.get(0) + " " + alphabet.get(i) + " " + "empty");
            }

        }
        for(int i = 0; i< DFATransitions.size(); i++) {
            String line = DFATransitions.get(i);
            String state = line.split(" ")[0];
            String result = line.split(" ")[2];
            if (!DFAStates.contains(result)) {
                if(result.equals("empty")) {
                    DFAStates.add(result);
                } else {
                    DFAStates.add(result);
                    for (int j = 0;j < alphabet.size(); j++){
                        String newTransition = "";
                        for(int k = 0; k<result.length() ; k++) {
                            if(NFADiagram[states.indexOf(Character.toString(result.charAt(k)))][j] != null) {
                                newTransition += NFADiagram[states.indexOf(Character.toString(result.charAt(k)))][j];
                            }
                        }
                        if(newTransition.equals("")) {
                            DFATransitions.add(result + " " + Integer.toString(j) + " " + "empty");
                        } else {
                            DFATransitions.add(result + " " + Integer.toString(j) + " " + removeDuplicates(newTransition));
                        }
                    }
                }

            }


        }
        // Adding death state to show in DFA transitions
        for (int i = 0; i<DFATransitions.size(); i++) {
            String line = DFATransitions.get(i);
            String state = line.split(" ")[0];
            String result = line.split(" ")[2];
            if(state.equals("empty") && result.equals("empty")) {

            } else if (result.equals("empty")) {
                for(int j = 0; j<DFAAlphabet.size(); j++) {
                    DFATransitions.add("empty" + " " + Integer.toString(j) + " " + "empty");
                }
                break;
            }
        }
        // Creating DFA final states
        for (int i = 0; i<DFATransitions.size(); i++) {
            String line = DFATransitions.get(i);
            String result = line.split(" ")[2];
            if(!DFAFinalState.contains(result)) {
                for(int j = 0; j<finalStates.size(); j++) {
                    if(result.contains(finalStates.get(j))) {
                        DFAFinalState.add(result);
                    }
                }
            }

        }


        // Writing output on the console
        String fileContent = "";
        fileContent = fileContent.concat("ALPHABET\n");
        for(int i = 0; i< alphabet.size(); i++){
            fileContent = fileContent.concat(alphabet.get(i) + "\n");
        }
        fileContent = fileContent.concat("STATES\n");
        for(int i = 0; i< DFAStates.size(); i++){
            fileContent = fileContent.concat(DFAStates.get(i) + "\n");
        }
        fileContent = fileContent.concat("START\n");
        fileContent = fileContent.concat(startState + "\n");
        fileContent = fileContent.concat("FINAL\n");
        for(int i = 0; i< DFAFinalState.size(); i++){
            fileContent = fileContent.concat(DFAFinalState.get(i) + "\n");
        }
        fileContent = fileContent.concat("TRANSITIONS\n");
        for(int i = 0; i< DFATransitions.size(); i++){
            fileContent = fileContent.concat(DFATransitions.get(i) + "\n");
        }
        fileContent = fileContent.concat("END\n");

        System.out.println(fileContent);


    }

        public static String removeDuplicates(String st) {
            char[] chars = st.toCharArray();
            Set<Character> charSet = new LinkedHashSet<Character>();
            for (char c : chars) {
                charSet.add(c);
            }

            StringBuilder sb = new StringBuilder();
            for (Character character : charSet) {
                sb.append(character);
            }
            st = sb.toString();
            return st;
        }


}