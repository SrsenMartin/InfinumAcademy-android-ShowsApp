package srsen.martin.infinum.co.hw3_and_on;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Show {

    private final String ID;
    private String name;
    private List<Episode> episodes;

    public Show(String name, List<Episode> episodes){
        if(episodes == null){
            this.episodes = new ArrayList<>();
        }else{
            this.episodes = episodes;
        }

        this.name = name;
        ID = UUID.randomUUID().toString();
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes){
        this.episodes = episodes;
    }

    public String getName() {
        return name;
    }

    public void addEpisode(Episode episode){
        episodes.add(episode);
    }

    public String getID(){
        return ID;
    }
}
