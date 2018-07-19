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
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EpisodesActivity extends AppCompatActivity {

    public static final String EXTRA_SHOW_ID = "srsen.martin.infinum.co.showId";
    public static final int REQUEST_CODE_EPISODE = 99;

    @BindView(R.id.episodesRecyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.listEmptyLayout)
    ViewGroup emptyShower;

    private List<Episode> episodes;
    private EpisodesAdapter adapter;
    private Show show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodes);
        ButterKnife.bind(this);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.episodesToolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        setShowDetails();
    }

    private void setShowDetails() {
        checkExtras();

        String showId = getIntent().getStringExtra(EXTRA_SHOW_ID);
        show = ShowsDB.getShowById(showId);
        if(show == null)    return;

        getSupportActionBar().setTitle(show.getName());

        episodes = show.getEpisodes();
        checkEmptyShowerIcon();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EpisodesAdapter(episodes, (context, episode) -> {
            Intent intent = EpisodeDetailsActivity.newIntentInstance(context, episode, show.getName());
            context.startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
    }

    private void checkExtras() {
        if(getIntent() == null || getIntent().getStringExtra(EXTRA_SHOW_ID) == null){
            Toast.makeText(this, getString(R.string.id_extra), Toast.LENGTH_SHORT).show();
            finish();
        }else if(!ShowsDB.containsShow(getIntent().getStringExtra(EXTRA_SHOW_ID))){
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
                    Episode episode = (Episode) data.getParcelableExtra(AddEpisodeActivity.EXTRA_EPISODE);
                    addEpisode(episode);
                }
        }
    }

    private void addEpisode(Episode episode){
        episodes.add(episode);
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

    public static Intent newIntentInstance(Context context, String showId){
        Intent intent = new Intent(context, EpisodesActivity.class);
        intent.putExtra(EXTRA_SHOW_ID, showId);

        return intent;
    }

    @Override
    protected void onStop() {
        super.onStop();

        Util.saveShowEpisodes(this, show.getID());
    }
}
