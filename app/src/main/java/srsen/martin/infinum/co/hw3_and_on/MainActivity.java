package srsen.martin.infinum.co.hw3_and_on;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.showsRecyclerView)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        List<Show> showsList = ShowsDB.getShows();
        loadEpisodes(showsList);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ShowsAdapter adapter = new ShowsAdapter(showsList, (context, show) -> {
            Intent intent = EpisodesActivity.newIntentInstance(context, show.getID());
            context.startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
    }

    private void loadEpisodes(List<Show> showList){
        for(Show show : showList){
            Util.loadShowEpisodes(this, show.getID());
        }
    }
}
