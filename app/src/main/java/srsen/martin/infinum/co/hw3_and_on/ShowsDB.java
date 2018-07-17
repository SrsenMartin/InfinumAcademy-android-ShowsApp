package srsen.martin.infinum.co.hw3_and_on;

import java.util.ArrayList;
import java.util.List;

public class ShowsDB {

    private static List<Show> showsList = new ArrayList<>();

    static{
        showsList.add(new Show("Zabranjena ljubav", null));
        showsList.add(new Show("Ljubav je na selu", null));
        showsList.add(new Show("Gossip girl", null));
        showsList.add(new Show("Pokemon", null));
        showsList.add(new Show("Naruto", null));
    }

    public static List<Show> getShows(){
        return showsList;
}

    public static boolean containsShow(int ID){
        for(Show show : showsList){
            if(show.getID() == ID)  return true;
        }

        return false;
    }

    public static Show getShowById(int ID){
        for(Show show : showsList){
            if(show.getID() == ID)  return show;
        }

        return null;
    }
}
