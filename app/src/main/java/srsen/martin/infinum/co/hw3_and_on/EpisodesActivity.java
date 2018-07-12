package srsen.martin.infinum.co.hw3_and_on;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

public class EpisodesActivity extends AppCompatActivity {

    public static final String EXTRA_SHOW_ID = "srsen.martin.infinum.co.showId";
    public static final int REQUEST_CODE_EPISODE = 99;

    private RecyclerView recyclerView;
    private List<Episode> episodes;
    private EpisodesAdapter adapter;
    private LinearLayout emptyShower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodes);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.episodesToolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        setShowDetails();
    }

    private void setShowDetails() {
        checkExtras();

        int showId = getIntent().getIntExtra(EXTRA_SHOW_ID, -1);
        Show show = ShowsDB.getShowById(showId);
        if(show == null)    return;

        getSupportActionBar().setTitle(show.getName());

        episodes = show.getEpisodes();
        emptyShower = findViewById(R.id.listEmptyLayout);
        checkEmptyShowerIcon();

        recyclerView = findViewById(R.id.episodesRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EpisodesAdapter(episodes);
        recyclerView.setAdapter(adapter);
    }

    private void checkExtras() {
        if(getIntent() == null || getIntent().getIntExtra(EXTRA_SHOW_ID, -1) == -1){
            Toast.makeText(this, getString(R.string.id_extra), Toast.LENGTH_SHORT).show();
            finish();
        }else if(!ShowsDB.containsShow(getIntent().getIntExtra(EXTRA_SHOW_ID, -1))){
            Toast.makeText(this, getString(R.string.not_contained_show), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.addIcon:
                onAddClicked();
                return true;
        }

        return false;
    }

    private void onAddClicked(){
        Intent intent = AddEpisodeActivity.newIntentInstance(this);
        startActivityForResult(intent, REQUEST_CODE_EPISODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_EPISODE:
                if(resultCode == RESULT_OK){
                    String name = data.getStringExtra(AddEpisodeActivity.EXTRA_EPISODE_NAME);
                    String description = data.getStringExtra(AddEpisodeActivity.EXTRA_EPISODE_DESCRIPTION);

                    addEpisode(name, description);
                }
        }
    }

    private void addEpisode(String name, String description){
        episodes.add(new Episode(name, description));
        checkEmptyShowerIcon();

        adapter.notifyDataSetChanged();
    }

    private void checkEmptyShowerIcon(){
        if(episodes.isEmpty()){
            emptyShower.setVisibility(View.VISIBLE);
        }else{
            emptyShower.setVisibility(View.INVISIBLE);
        }
    }

    public static Intent newIntentInstance(Context context, int showId){
        Intent intent = new Intent(context, EpisodesActivity.class);
        intent.putExtra(EXTRA_SHOW_ID, showId);

        return intent;
    }

    public static Intent newIntentInstance(Context context){
        Intent intent = new Intent(context, EpisodesActivity.class);
        return intent;
    }
}
