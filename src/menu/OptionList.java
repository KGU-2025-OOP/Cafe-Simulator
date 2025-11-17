package menu;

import java.util.Scanner;
import java.util.ArrayList;


public class OptionList {
    ArrayList<Option> optionList;

    public OptionList() {
        this.optionList = new ArrayList<Option>();
        try {
            java.io.File file = new java.io.File("resources/options.txt");
            Scanner filein = new Scanner(file, "UTF-8");
            while (filein.hasNext()) {
                this.optionList.add(new Option(filein));
            }
            filein.close();
        } catch (java.io.FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
            return;
        }
    }

    public ArrayList<Option> getOptionList() {
        return this.optionList;
    }
}
