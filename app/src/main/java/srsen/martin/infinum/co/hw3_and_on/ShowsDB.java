package srsen.martin.infinum.co.hw3_and_on;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ShowsDB {

    private static Map<String, Show> showsList = new LinkedHashMap<>();

    static{
        showsList.put("Zabranjena ljubav", new Show("Zabranjena ljubav", null));
        showsList.put("Ljubav je na selu", new Show("Ljubav je na selu", null));
        showsList.put("Gossip girl", new Show("Gossip girl", null));
        showsList.put("Pokemon", new Show("Pokemon", null));
        showsList.put("Naruto", new Show("Naruto", null));
    }

    public static List<Show> getShows(){
        Collection col =  showsList.values();

        List<Show> showList = new ArrayList<>(col);
        return showList;
}

    public static Show getShowByName(String name){
        return showsList.get(name);
    }

    public static boolean containsShow(String name){
        return showsList.containsKey(name);
    }
}
