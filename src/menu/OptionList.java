package menu;

import java.util.ArrayList;

public class OptionList extends MenuItemList<Option> {
    public static final String OPTION_PATH = "resources/options.txt";
    
    public OptionList() {
        super(OPTION_PATH, Option::new);
    }

    public ArrayList<Option> getOptionList() {
        return getItemList();
    }
}
