package srsen.martin.infinum.co.hw3_and_on;

import android.app.Application;

import java.util.List;

public class Initialization extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        List<Show> showsList = ShowsDB.getShows();
        loadEpisodes(showsList);
    }

    private void loadEpisodes(List<Show> showList){
        for(Show show : showList){
            //Util.loadShowEpisodes(this, show.getID());
        }
    }
}
