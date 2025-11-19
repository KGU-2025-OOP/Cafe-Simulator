package menu;

import java.util.ArrayList;

public class OptionList extends MenuItemList<Option> {
    
    public OptionList() {
        super("options.txt", Option::new);
    }

    public ArrayList<Option> getOptionList() {
        return getItemList();
    }
}
