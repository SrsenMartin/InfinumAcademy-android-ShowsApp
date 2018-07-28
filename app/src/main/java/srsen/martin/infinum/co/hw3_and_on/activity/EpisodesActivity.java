package srsen.martin.infinum.co.hw3_and_on.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import srsen.martin.infinum.co.hw3_and_on.networking.ApiService;
import srsen.martin.infinum.co.hw3_and_on.R;
import srsen.martin.infinum.co.hw3_and_on.Util;
import srsen.martin.infinum.co.hw3_and_on.adapter.EpisodesAdapter;
import srsen.martin.infinum.co.hw3_and_on.database.DatabaseCallback;
import srsen.martin.infinum.co.hw3_and_on.database.repository.EpisodesRepository;
import srsen.martin.infinum.co.hw3_and_on.database.repository.ShowsRepository;
import srsen.martin.infinum.co.hw3_and_on.models.Data;
import srsen.martin.infinum.co.hw3_and_on.models.Episode;
import srsen.martin.infinum.co.hw3_and_on.models.Show;

public class EpisodesActivity extends AppCompatActivity {

    public static final String EXTRA_SHOW_ID = "srsen.martin.infinum.co.showId";
    public static final int REQUEST_CODE_EPISODE = 99;

    @BindView(R.id.showImage)
    ImageView showImage;

    @BindView(R.id.showTitle)
    TextView showTitle;

    @BindView(R.id.showDescription)
    TextView showDescription;

    @BindView(R.id.episodesRecyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.listEmptyLayout)
    ViewGroup emptyShower;

    @BindView(R.id.counter)
    TextView episodesCounter;

    @BindView(R.id.likesCount)
    TextView likesCount;

    @BindView(R.id.dislikesCount)
    TextView dislikesCount;

    @BindView(R.id.episodesSwiper)
    SwipeRefreshLayout episodesSwiper;

    private EpisodesAdapter adapter;
    private String showId;
    private Dialog progressDialog;
    private ApiService apiService;
    private ShowsRepository showsRepository;
    private EpisodesRepository episodesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodes);
        ButterKnife.bind(this);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.episodesToolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        apiService = Util.initApiService();
        showsRepository = Util.initShowsRepository(this);
        episodesRepository = Util.initEpisodesRepository(this);
        setSwiperListener();

        showId = getShowId();
        if(showId == null)  return;

        setShowDetails();
        setEpisodeDetails();
    }

    private String getShowId(){
        checkExtras();

        showId = getIntent().getStringExtra(EXTRA_SHOW_ID);
        return showId;
    }

    private void checkExtras() {
        if(getIntent() == null || getIntent().getStringExtra(EXTRA_SHOW_ID) == null){
            Toast.makeText(this, getString(R.string.id_extra), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setShowDetails() {
        if(Util.isInternetAvailable(this)){
            loadShowDetailsInternet();
        }else{
            loadShowDetailsDatabase();
        }
    }

    private void loadShowDetailsInternet(){
        progressDialog = Util.showProgress(this,
                null, getString(R.string.loading), true, false);

        apiService.getShowDetails(showId).enqueue(new Callback<Data<Show>>() {
            @Override
            public void onResponse(@NonNull Call<Data<Show>> call, @NonNull Response<Data<Show>> response) {
                Util.hideProgress(progressDialog);

                if(response.isSuccessful()){
                    Show show = response.body().getData();
                    setShow(show);
                    saveShowDetailsIntoDatabase(show);
                }else{
                    Toast.makeText(EpisodesActivity.this, R.string.data_unable, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Data<Show>> call, @NonNull Throwable t) {
                Util.hideProgress(progressDialog);
                Util.showError(EpisodesActivity.this, getString(R.string.shows));
            }
        });
    }

    private void saveShowDetailsIntoDatabase(Show show){
        showsRepository.insertShow(show, new DatabaseCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
            }

            @Override
            public void onError(Throwable t) {
                Util.showError(EpisodesActivity.this, getString(R.string.shows));
            }
        });
    }

    private void loadShowDetailsDatabase(){
        showsRepository.getShow(showId, new DatabaseCallback<Show>() {
            @Override
            public void onSuccess(Show data) {
                setShow(data);
            }

            @Override
            public void onError(Throwable t) {
                Util.showError(EpisodesActivity.this, getString(R.string.shows));
            }
        });
    }

    private void setShow(Show show){
        Uri imageUri = Util.getImageUri(show.getImageUrl());
        Util.setImage(this, imageUri, showImage, findViewById(R.id.emptyPlaceholder));

        showTitle.setText(show.getTitle());
        showDescription.setText(show.getDescription());

        likesCount.setText(show.getLikes() + "");
        dislikesCount.setText(show.getDislikes() + "");
    }

    private void setEpisodeDetails(){
        if(Util.isInternetAvailable(this)){
            loadEpisodesFromInternet();
        }else{
            loadEpisodesFromDatabase();
        }
    }

    private void loadEpisodesFromInternet(){
        apiService.getEpisodes(showId).enqueue(new Callback<Data<List<Episode>>>() {
            @Override
            public void onResponse(@NonNull Call<Data<List<Episode>>> call, @NonNull Response<Data<List<Episode>>> response) {
                if(response.isSuccessful()){
                    List<Episode> episodes = response.body().getData();

                    setEpisodesList(episodes);
                    insertEpisodesDatabase(episodes);
                }else{
                    Toast.makeText(EpisodesActivity.this, R.string.data_unable, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Data<List<Episode>>> call, @NonNull Throwable t) {
                Util.showError(EpisodesActivity.this, getString(R.string.episode));
            }
        });
    }

    private void loadEpisodesFromDatabase(){
        episodesRepository.getEpisodes(showId, new DatabaseCallback<List<Episode>>() {
            @Override
            public void onSuccess(List<Episode> data) {
                if (data.isEmpty()) {
                    if(!Util.isInternetAvailable(EpisodesActivity.this)){
                        Toast.makeText(EpisodesActivity.this, R.string.database_empty, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    setEpisodesList(data);
                }
            }

            @Override
            public void onError(Throwable t) {
                Util.showError(EpisodesActivity.this, getString(R.string.episode));
            }
        });
    }

    private void setEpisodesList(List<Episode> episodes){
        for(Episode episode : episodes){
            episode.setShowId(showId);
        }

        if(adapter == null){
            initAdapter(episodes);
            checkEmptyShowerIcon();
        }else{
            adapter.setEpisodesList(episodes);
        }

        refreshCounter();
    }

    private void addEpisode(Episode episode){
        if(adapter == null){
            initAdapter(new ArrayList<>());
            checkEmptyShowerIcon();
        }

        adapter.addEpisode(episode);
        refreshCounter();
    }

    private void initAdapter(List<Episode> episodeList){
        adapter = new EpisodesAdapter(episodeList, selectedEpisode -> {
            Intent intent = EpisodeDetailsActivity.newIntentInstance(this, selectedEpisode, showTitle.getText().toString());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }

    private void insertEpisodesDatabase(List<Episode> episodesList) {
        episodesRepository.insertEpisodes(episodesList, new DatabaseCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
            }

            @Override
            public void onError(Throwable t) {
                Util.showError(EpisodesActivity.this, getString(R.string.episode));
            }
        });

    }

    @OnClick(R.id.addEpisodeButton)
    void createNewEpisode(){
        Intent intent = AddEpisodeActivity.newIntentInstance(this);
        startActivityForResult(intent, REQUEST_CODE_EPISODE);
    }

    @OnClick(R.id.likeIcon)
    void likeClicked(){
        Toast.makeText(this, "Like clicked", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.dislikeIcon)
    void dislikeClicked(){
        Toast.makeText(this, "Disike clicked", Toast.LENGTH_SHORT).show();
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

    private void checkEmptyShowerIcon(){
        emptyShower.setVisibility(View.INVISIBLE);
    }

    private void refreshCounter(){
        episodesCounter.setText(adapter.getItemCount() + "");
    }

    private void setSwiperListener(){
        episodesSwiper.setOnRefreshListener(() -> {
            episodesSwiper.setRefreshing(true);

            if(!Util.isInternetAvailable(EpisodesActivity.this)){
                Toast.makeText(EpisodesActivity.this, R.string.no_connection, Toast.LENGTH_SHORT).show();
            }else{
                loadShowDetailsInternet();
                loadEpisodesFromInternet();
            }

            episodesSwiper.setRefreshing(false);
        });
    }

    public static Intent newIntentInstance(Context context, String showId){
        Intent intent = new Intent(context, EpisodesActivity.class);
        intent.putExtra(EXTRA_SHOW_ID, showId);

        return intent;
    }
}
