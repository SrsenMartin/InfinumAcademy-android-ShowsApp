package srsen.martin.infinum.co.hw3_and_on;

import java.util.ArrayList;
import java.util.List;

public class Show {

    private static int counter = 1;

    private final int ID;
    private String name;
    private List<Episode> episodes;

    public Show(String name, List<Episode> episodes){
        if(episodes == null){
            this.episodes = new ArrayList<>();
        }else{
            this.episodes = episodes;
        }

        this.name = name;
        ID = counter++;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public String getName() {
        return name;
    }

    public void addEpisode(Episode episode){
        episodes.add(episode);
    }

    public int getID(){
        return ID;
    }
}
