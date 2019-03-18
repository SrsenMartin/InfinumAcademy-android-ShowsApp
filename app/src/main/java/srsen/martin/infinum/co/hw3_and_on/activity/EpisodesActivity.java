package srsen.martin.infinum.co.hw3_and_on.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import srsen.martin.infinum.co.hw3_and_on.Provider;
import srsen.martin.infinum.co.hw3_and_on.R;
import srsen.martin.infinum.co.hw3_and_on.Util;
import srsen.martin.infinum.co.hw3_and_on.adapter.EpisodesAdapter;
import srsen.martin.infinum.co.hw3_and_on.database.DatabaseCallback;
import srsen.martin.infinum.co.hw3_and_on.models.Data;
import srsen.martin.infinum.co.hw3_and_on.models.Episode;
import srsen.martin.infinum.co.hw3_and_on.models.Show;

public class EpisodesActivity extends AppCompatActivity {

    public static final String EXTRA_SHOW_ID = "srsen.martin.infinum.co.showId";
    public static final int REQUEST_CODE_EPISODE = 99;

    @BindView(R.id.showImage)
    ImageView showImage;

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

    @BindView(R.id.episodesToolbar)
    Toolbar toolbar;

    @BindView(R.id.likeIcon)
    ImageView likeIcon;

    @BindView(R.id.dislikeIcon)
    ImageView dislikeIcon;

//    @BindView(R.id.episodesSwiper)
//    SwipeRefreshLayout episodesSwiper;

    @BindView(R.id.collapseToolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    private EpisodesAdapter adapter;
    private String showId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodes);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //setSwiperListener();

        showId = getShowId();
        if(showId == null)  return;

        setShowDetails();
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
        Util.showProgress(this,null, getString(R.string.loading), true, false);

        Provider.getApiService().getShowDetails(showId).enqueue(new Callback<Data<Show>>() {
            @Override
            public void onResponse(@NonNull Call<Data<Show>> call, @NonNull Response<Data<Show>> response) {
                Util.hideProgress();

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
                Util.hideProgress();
                Util.showError(EpisodesActivity.this, getString(R.string.shows));
            }
        });
    }

    private void saveShowDetailsIntoDatabase(Show show){
        Provider.getShowsRepository(this).updateShowDescription(show.getDescription(), show.getID(), new DatabaseCallback<Void>() {
            @Override
            public void onSuccess(Void data) {}

            @Override
            public void onError(Throwable t) {
                Util.showError(EpisodesActivity.this, getString(R.string.shows));
            }
        });
    }

    private void loadShowDetailsDatabase(){
        Provider.getShowsRepository(this).getShow(showId, new DatabaseCallback<Show>() {
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

        showDescription.setText(show.getDescription());
        collapsingToolbarLayout.setTitle(show.getTitle());

        likesCount.setText(show.getLikes() + "");
        likeStateAction((status) ->  setLikeIcon(status));

        setEpisodeDetails();
    }

    private void setEpisodeDetails(){
        if(Util.isInternetAvailable(this)){
            loadEpisodesFromInternet();
        }else{
            loadEpisodesFromDatabase();
        }
    }

    private void loadEpisodesFromInternet(){
        Util.showProgress(this,null, getString(R.string.loading), true, false);

        Provider.getApiService().getEpisodes(showId).enqueue(new Callback<Data<List<Episode>>>() {
            @Override
            public void onResponse(@NonNull Call<Data<List<Episode>>> call, @NonNull Response<Data<List<Episode>>> response) {
                Util.hideProgress();

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
                Util.hideProgress();
                Util.showError(EpisodesActivity.this, getString(R.string.episode));
            }
        });
    }

    private void loadEpisodesFromDatabase(){
        Provider.getEpisodesRepository(this).getEpisodes(showId, new DatabaseCallback<List<Episode>>() {
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

        List<Episode> episodeCont = new ArrayList<>();
        episodeCont.add(episode);
        insertEpisodesDatabase(episodeCont);
    }

    private void initAdapter(List<Episode> episodeList){
        adapter = new EpisodesAdapter(episodeList, selectedEpisode -> {
            Intent intent = EpisodeDetailsActivity.newIntentInstance(this, selectedEpisode);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
    }

    private void insertEpisodesDatabase(List<Episode> episodesList) {
        Provider.getEpisodesRepository(this).insertEpisodes(episodesList, new DatabaseCallback<Void>() {
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
        Intent intent = AddEpisodeActivity.newIntentInstance(this, showId);
        startActivityForResult(intent, REQUEST_CODE_EPISODE);
    }

    @OnClick(R.id.likeIcon)
    void likeClicked(){
        likeStateAction(status -> {
                if(status == null){
                    likeUpdateAction(true, 1);
                }else if(status){
                    likeUpdateAction(null, -1);
                }else{
                    likeUpdateAction(true, 2);
                }
        });
    }

    @OnClick(R.id.dislikeIcon)
    void dislikeClicked(){
        likeStateAction(status -> {
                if(status == null){
                    likeUpdateAction(false, -1);
                }else if(status){
                    likeUpdateAction(false, -2);
                }else{
                    likeUpdateAction(null, 1);
                }
        });
    }

    private void likeUpdateAction(Boolean status, int increase){
        if(!Util.isInternetAvailable(this)){
            Toast.makeText(this, R.string.internt_like, Toast.LENGTH_LONG).show();
            return;
        }

        int currentLikes = Integer.parseInt(likesCount.getText().toString());
        int newLikes = currentLikes + increase;
        likesCount.setText(Integer.toString(newLikes));

        updateLikeDatabase(status, newLikes);
        updateLikeApi(increase);

        setLikeIcon(status);
    }

    private void setLikeIcon(Boolean status){
        if(status == null){
            likeIcon.setImageResource(R.drawable.like_icon_background);
            dislikeIcon.setImageResource(R.drawable.dislike_icon_background);
        }else if(status){
            likeIcon.setImageResource(R.drawable.like_icon_background_full);
            dislikeIcon.setImageResource(R.drawable.dislike_icon_background);
        }else{
            likeIcon.setImageResource(R.drawable.like_icon_background);
            dislikeIcon.setImageResource(R.drawable.dislike_icon_background_full);
        }
    }

    private void updateLikeDatabase(Boolean status, int newLikes){
        Provider.getShowsRepository(this).updateLikeStatus(status, newLikes, showId, new DatabaseCallback<Void>() {
            @Override
            public void onSuccess(Void data) {}

            @Override
            public void onError(Throwable t) {
                Util.showError(EpisodesActivity.this, getString(R.string.update_status));
            }
        });
    }

    private void updateLikeApi(int increase){
        Util.showProgress(this, null, getString(R.string.loading), true, false);

        while(increase != 0){
            if(increase > 0){
                --increase;
                likeShowInternet();
            }else{
                ++increase;
                dislikeShowInternet();
            }
        }
    }

    private void likeShowInternet(){
        Provider.getApiService().likeShow(Provider.getToken(this), showId).enqueue(updateInternetStatusCallback(R.string.wasnt_liked));
    }

    private void dislikeShowInternet(){
        Provider.getApiService().dislikeShow(Provider.getToken(this), showId).enqueue(updateInternetStatusCallback(R.string.wasnt_disliked));
    }

    private Callback<Void> updateInternetStatusCallback(int messageId){
        return new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Util.hideProgress();

                if(!response.isSuccessful()){
                    Toast.makeText(EpisodesActivity.this, messageId, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Util.hideProgress();
                Util.showError(EpisodesActivity.this, getString(R.string.update_status));
            }
        };
    }

    private void likeStateAction(LikeStatusUpdater updater){
        Provider.getShowsRepository(this).getLikeStatus(showId, new DatabaseCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                updater.update(data);
            }

            @Override
            public void onError(Throwable t) {
                Util.showError(EpisodesActivity.this, getString(R.string.update_status));
            }
        });
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

//    private void setSwiperListener(){
//        episodesSwiper.setOnRefreshListener(() -> {
//            episodesSwiper.setRefreshing(true);
//
//            if(!Util.isInternetAvailable(EpisodesActivity.this)){
//                Toast.makeText(EpisodesActivity.this, R.string.no_connection, Toast.LENGTH_SHORT).show();
//            }else{
//                loadEpisodesFromInternet();
//            }
//
//            episodesSwiper.setRefreshing(false);
//        });
//    }

    private void checkEmptyShowerIcon(){
        emptyShower.setVisibility(View.INVISIBLE);
    }

    private void refreshCounter(){
        episodesCounter.setText(adapter.getItemCount() + "");
    }

    public static Intent newIntentInstance(Context context, String showId){
        Intent intent = new Intent(context, EpisodesActivity.class);
        intent.putExtra(EXTRA_SHOW_ID, showId);

        return intent;
    }

    private interface LikeStatusUpdater{
        void update(Boolean status);
    }
}
