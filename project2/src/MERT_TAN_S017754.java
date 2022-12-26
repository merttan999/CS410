import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MERT_TAN_S017754 {
    public static void main(String[] args) throws IOException {
        URL path = MERT_TAN_S017754.class.getResource("G2.txt");
        File file = new File(path.getFile());
        Scanner scan = new Scanner(new FileReader(file));
        ArrayList<String> text = new ArrayList<String>();
        ArrayList<String> nonTerminal = new ArrayList<String>();
        ArrayList<String> terminal = new ArrayList<String>();
        ArrayList<String> rules = new ArrayList<String>();
        String startState = "";
        ArrayList<String> usableAlphabetsForSymbols = new ArrayList<String>();
        int index = 0;


        //reading txt file
        while (scan.hasNextLine()){
            text.add(scan.nextLine());
        }
        for(int i = text.indexOf("NON-TERMINAL"); i< text.indexOf("TERMINAL")-1; i++){
            nonTerminal.add(text.get(i+1));
        }
        for(int i = text.indexOf("TERMINAL"); i< text.indexOf("RULES")-1; i++){
            terminal.add(text.get(i+1));
        }
        for(int i = text.indexOf("RULES"); i< text.indexOf("START")-1; i++){
            rules.add(text.get(i+1));
        }
        for(int i = text.indexOf("START"); i< text.size()-1; i++){ // -1 yazilabilir calismazsa
            startState = text.get(i+1);
        }

        //Creating symbols to use when need to create new rules.
        for(char i = 'A'; i<'Z';i++ ){
            String tmp = Character.toString(i);
            if (!nonTerminal.contains(tmp)){
                usableAlphabetsForSymbols.add(tmp);
            }
        }

        //elimination of epsilon
        // - Including directly contains epsilon
        ArrayList<String> containEpsilon = new ArrayList<String>();
        for(int i = 0; i< rules.size(); i++) {
            String line = rules.get(i);
            String leftSide = line.split(":")[0];
            String rightSide = line.split(":")[1];
            if (rightSide.contains("e")) {
                containEpsilon.add(leftSide);
            }
        }
        // - Including indirectly contains epsilon
        for(int i = 0; i< rules.size(); i++) {
            String line = rules.get(i);
            String leftSide = line.split(":")[0];
            String rightSide = line.split(":")[1];
            for(int j = 0; j< containEpsilon.size(); j++) {
                if(!leftSide.equals(startState) && rightSide.contains(containEpsilon.get(j)) && !leftSide.equals(containEpsilon.get(j))) {
                    containEpsilon.add(leftSide);
                }
            }
        }

        // applying epsilon elimination
        ArrayList<String> rulesWithoutEpsilon = new ArrayList<String>();

        rulesWithoutEpsilon.addAll(rules);
        for(int i = 0; i< rules.size(); i++) {
            String line = rules.get(i);
            String leftSide = line.split(":")[0];
            String rightSide = line.split(":")[1];
            for(int j = 0; j< containEpsilon.size(); j++) {
                String newString = "empty";
                if(rightSide.length() != 1 && rightSide.contains(containEpsilon.get(j))) {
                    newString = rightSide.replaceFirst(containEpsilon.get(j),"");
                }
                rulesWithoutEpsilon.add(leftSide + ":" + newString);
            }
        }
        ArrayList<String> temp = new ArrayList<String>();
        temp.addAll(rulesWithoutEpsilon);
        for(int i = 0; i< rulesWithoutEpsilon.size(); i++) {
            String line = rulesWithoutEpsilon.get(i);
            String leftSide = line.split(":")[0];
            String rightSide = line.split(":")[1];
            if(rightSide.equals("e") || rightSide.equals("empty")) {
                temp.remove(rulesWithoutEpsilon.get(i));
            }
        }
        rulesWithoutEpsilon = temp;
        rulesWithoutEpsilon= (ArrayList<String>) rulesWithoutEpsilon.stream().distinct().collect(Collectors.toList());


        //Elimination of Unit Production
        ArrayList<String> rulesWithoutUnitProduction = new ArrayList<String>();
        for(int i = 0; i< rulesWithoutEpsilon.size(); i++) {
            String line = rulesWithoutEpsilon.get(i);
            String leftSide = line.split(":")[0];
            String rightSide = line.split(":")[1];
            if(rightSide.length()==1) {
                char chr = rightSide.charAt(0);
                if(Character.isUpperCase(chr)) {
                    for(int j = 0; j< rulesWithoutEpsilon.size(); j++) {
                        String line1 = rulesWithoutEpsilon.get(j);
                        String leftSide1 = line1.split(":")[0];
                        String rightSide1 = line1.split(":")[1];
                        if(leftSide1.equals(rightSide)) {
                            rulesWithoutUnitProduction.add(leftSide + ":" + rightSide1);
                        }
                    }
                }
            }
        }
        rulesWithoutUnitProduction.addAll(rulesWithoutEpsilon);
        ArrayList<String> temp2 = new ArrayList<String>();
        temp2.addAll(rulesWithoutUnitProduction);
        for(int i = 0; i< rulesWithoutUnitProduction.size(); i++) {
            String line = rulesWithoutUnitProduction.get(i);
            String leftSide = line.split(":")[0];
            String rightSide = line.split(":")[1];
            if(rightSide.length()==1) {
                char chr = rightSide.charAt(0);
                if (Character.isUpperCase(chr)) {
                    temp2.remove(rulesWithoutUnitProduction.get(i));
                }
            }
        }
        rulesWithoutUnitProduction = temp2;
        rulesWithoutUnitProduction= (ArrayList<String>) rulesWithoutUnitProduction.stream().distinct().collect(Collectors.toList());

        //Writing rules with CNF
        ArrayList<String> CNFRules = new ArrayList<String>();
        ArrayList<String> temp3 = new ArrayList<String>();
        temp3.addAll(rulesWithoutUnitProduction);

        for(int i = 0; i< rulesWithoutUnitProduction.size(); i++) {
            String line = rulesWithoutUnitProduction.get(i);
            String leftSide = line.split(":")[0];
            String rightSide = line.split(":")[1];
            if(rightSide.length() == 1) {
                CNFRules.add(rulesWithoutUnitProduction.get(i));
                temp3.remove(rulesWithoutUnitProduction.get(i));

            } else if (rightSide.length() == 2 && isUpperCase(rightSide)) {
                CNFRules.add(rulesWithoutUnitProduction.get(i));
                temp3.remove(rulesWithoutUnitProduction.get(i));
            }
        }
        String str = "";
//        ArrayList<String> temp4 = new ArrayList<String>();
//        temp4.addAll(CNFRules);
        ArrayList<String> newNonTerminal = new ArrayList<>();
        for(int i = 0; i< temp3.size(); i++) {
            String line = temp3.get(i);
            String leftSide = line.split(":")[0];
            String rightSide = line.split(":")[1];
            for(int j = 0; j<rightSide.length(); j++) {
                if (terminal.contains(String.valueOf(rightSide.charAt(j))) && !str.equals(String.valueOf(rightSide.charAt(j)))) {
                    CNFRules.add(usableAlphabetsForSymbols.get(index) + ":" + rightSide.charAt(j));
                    newNonTerminal.add(usableAlphabetsForSymbols.get(index));
                    index++;
                    str = String.valueOf(rightSide.charAt(j));
                }
            }
            if(rightSide.length() > 1) {
                for(int k = 0; k< CNFRules.size(); k++) {
                    String line1 = CNFRules.get(k);
                    String leftSide1 = line1.split(":")[0];
                    String rightSide1 = line1.split(":")[1];
                    if(rightSide.contains(rightSide1) && !nonTerminal.contains(leftSide1)) {
                        String newString = rightSide.replace(rightSide1,leftSide1);
                        CNFRules.add(leftSide + ":" + newString);

                    }
                }
            }

        }
        //editing cnf rules that don't satisfy the rule
        ArrayList<String> temp4 = new ArrayList<>();
        temp4.addAll(CNFRules);
        for(int i = 0; i< temp4.size(); i++) {
            String line = temp4.get(i);
            String leftSide = line.split(":")[0];
            String rightSide = line.split(":")[1];
            if(rightSide.length()>2) {
                if(rightSide.length() % 2 == 0) {
                    for(int j = 0; j<rightSide.length(); j+=2) {
                        String newRule = rightSide.substring(j,j+2);
                        CNFRules.add(usableAlphabetsForSymbols.get(index) + ":" + newRule);
                        newNonTerminal.add(usableAlphabetsForSymbols.get(index));
                        index ++;
                    }
                } else {
                    String singleItem = rightSide.substring(0,1);
                    String secondItem = "";
                    for(int k = 1; k<rightSide.length() ; k+=2) {
                        String newRule = rightSide.substring(k,k+2);
                        CNFRules.add(usableAlphabetsForSymbols.get(index) + ":" + newRule);
                        newNonTerminal.add(usableAlphabetsForSymbols.get(index));
                        secondItem = usableAlphabetsForSymbols.get(index);
                        index ++;
                    }
                    CNFRules.add(usableAlphabetsForSymbols.get(index) + ":" + singleItem+secondItem);
                    newNonTerminal.add(usableAlphabetsForSymbols.get(index));
                    index++;

                }
            }
        }
        // Removing rules that do not comply with CNF
        ArrayList<String> toBeRemoved = new ArrayList<>();
        for(int i = 0; i< CNFRules.size(); i++) {
            String line = CNFRules.get(i);
            String leftSide = line.split(":")[0];
            String rightSide = line.split(":")[1];
            if(rightSide.length() > 2 && rightSide.equals(rightSide.toUpperCase())) {
                toBeRemoved.add(CNFRules.get(i));
            }
        }
        for(int i = 0; i< toBeRemoved.size(); i++) {
            String line = toBeRemoved.get(i);
            int j = CNFRules.indexOf(line);
            CNFRules.remove(j);
        }
        nonTerminal.addAll(newNonTerminal);

        // Writing output on the console
        String fileContent = "";
        fileContent = fileContent.concat("NON-TERMINAL\n");
        for(int i = 0; i< nonTerminal.size(); i++){
            fileContent = fileContent.concat(nonTerminal.get(i) + "\n");
        }
        fileContent = fileContent.concat("TERMINAL\n");
        for(int i = 0; i< terminal.size(); i++){
            fileContent = fileContent.concat(terminal.get(i) + "\n");
        }
        fileContent = fileContent.concat("RULES\n");
        for(int i = 0; i< CNFRules.size(); i++){
            fileContent = fileContent.concat(CNFRules.get(i) + "\n");
        }
        fileContent = fileContent.concat("START\n");
        fileContent = fileContent.concat(startState + "\n");

        System.out.println(fileContent);


    }

    public static boolean isUpperCase(String str) {
        char chr1 = str.charAt(0);
        char chr2 = str.charAt(1);
        if(Character.isUpperCase(chr1) && Character.isUpperCase(chr2)) {
            return true;
        } else {
            return false;
        }
    }
}