import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class MERT_TAN_S017754 {
    public static void main(String[] args) throws IOException {
        URL path = MERT_TAN_S017754.class.getResource("Input_MERT_TAN_S017754.txt");
        File file = new File(path.getFile());
        Scanner scan = new Scanner(new FileReader(file));
        ArrayList<String> text = new ArrayList<String>();
        ArrayList<String> tapeAlphabet = new ArrayList<String>();
        String blank = "";
        ArrayList<String> states = new ArrayList<String>();
        String startState = "";
        String accept = "";
        String reject = "";
        ArrayList<String> rules = new ArrayList<String>();
        ArrayList<String> inputString = new ArrayList<String>();

        int index = 0;

        //reading txt file
        while (scan.hasNextLine()){
            text.add(scan.nextLine());
        }
        for(int i = text.indexOf("TAPEALPHABET"); i< text.indexOf("BLANK")-1; i++){
            tapeAlphabet.add(text.get(i+1));
        }
        for(int i = text.indexOf("BLANK"); i< text.indexOf("STATES")-1; i++){
            blank = text.get(i+1);
        }
        for(int i = text.indexOf("STATES"); i< text.indexOf("START")-1; i++){
            states.add(text.get(i+1));
        }
        for(int i = text.indexOf("START"); i< text.indexOf("ACCEPT")-1; i++){
            startState = text.get(i+1);
        }
        for(int i = text.indexOf("ACCEPT"); i< text.indexOf("REJECT")-1; i++){
            accept = text.get(i+1);
        }
        for(int i = text.indexOf("REJECT"); i< text.indexOf("RULES")-1; i++){
            reject = text.get(i+1);
        }
        for(int i = text.indexOf("RULES"); i< text.indexOf("ENDRULES")-1; i++){
            rules.add(text.get(i+1));
        }
        for(int i = text.indexOf("ENDRULES"); i< text.size()-1; i++){
            inputString.add(text.get(i+1));
        }

        inputString.add(blank);
        ArrayList<String> usedStates = new ArrayList<String>();
        usedStates.add(startState);
        String dynamicState = startState;
        String finalSituation = "";
        int loopIndex = 0;
        Boolean search = true;

        while (search == true) {

            for(int i = 0; i< rules.size(); i++) {
                String line = rules.get(i);
                String initialState = line.split(" ")[0];
                String indexToRead = line.split(" ")[1];
                String indexToWrite = line.split(" ")[2];
                String tapeDirection = line.split(" ")[3];
                String nextState = line.split(" ")[4];
                if (dynamicState.equals(initialState) && (inputString.get(index)).equals(indexToRead)) {
                    inputString.set(index,indexToWrite);
                    if (tapeDirection.equals("R")) {
                        index++;
                    } else {
                        index--;
                    }
                    if(nextState.equals(accept) || nextState.equals(reject)) {
                        if(nextState.equals(accept)) {
                            finalSituation = "accepted";
                            usedStates.add(accept);
                            search = false;
                        } else {
                            finalSituation = "rejected";
                            usedStates.add(reject);
                            search = false;
                        }
                    } else {
                        dynamicState = nextState;
                        usedStates.add(dynamicState);
                    }
                }

            }
            loopIndex++;
            if(loopIndex>5000) {
                finalSituation = "looped";
                search = true;
            }
        }

        String fileContent = "";
        fileContent = fileContent.concat("ROUT: ");
        for(int i = 0; i< usedStates.size(); i++){
            fileContent = fileContent.concat(usedStates.get(i) + " ");
        }
        fileContent = fileContent.concat("\n");
        fileContent = fileContent.concat("RESULT: ");
        fileContent = fileContent.concat(finalSituation );

        System.out.println(fileContent);


    }
}