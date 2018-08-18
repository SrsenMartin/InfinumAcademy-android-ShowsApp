package srsen.martin.infinum.co.hw3_and_on.activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import srsen.martin.infinum.co.hw3_and_on.adapter.ShowsAdapter;
import srsen.martin.infinum.co.hw3_and_on.database.DatabaseCallback;
import srsen.martin.infinum.co.hw3_and_on.models.Data;
import srsen.martin.infinum.co.hw3_and_on.models.Show;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.showsRecyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.showsToolbar)
    Toolbar toolbar;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.changeLayoutButton)
    FloatingActionButton changeLayoutButton;

    @BindView(R.id.listEmptyLayout)
    ViewGroup emptyShower;

    private ShowsAdapter adapter;

    private LinearLayoutManager linearManager;
    private GridLayoutManager gridManager;

    private boolean linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        initRecycler();

        getShows();
        setOnRefreshListener();
    }

    private void getShows(){
        loadShowsFromDatabase();

        if(Util.isInternetAvailable(this)){
            loadShowsFromInternet();
        }
    }

    private void loadShowsFromDatabase(){
        Provider.getShowsRepository(this).getShows(new DatabaseCallback<List<Show>>() {
            @Override
            public void onSuccess(List<Show> data) {
                if (data.isEmpty()) {
                    if(!Util.isInternetAvailable(MainActivity.this)){
                        Toast.makeText(MainActivity.this, R.string.database_empty, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    setShows(data);
                }
            }

            @Override
            public void onError(Throwable t) {
                Util.showError(MainActivity.this, getString(R.string.shows));
            }
        });
    }

    private void loadShowsFromInternet(){
        Util.showProgress(this, getString(R.string.shows), getString(R.string.loading), true, false);

        Provider.getApiService().getShows().enqueue(new Callback<Data<List<Show>>>() {
            @Override
            public void onResponse(@NonNull Call<Data<List<Show>>> call, @NonNull Response<Data<List<Show>>> response) {
                Util.hideProgress();

                if(response.isSuccessful()){
                    List<Show> showsList = response.body().getData();
                    setShows(showsList);
                    insertShowsDatabase(showsList);
                }else{
                    Toast.makeText(MainActivity.this, R.string.data_unable, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Data<List<Show>>> call, @NonNull Throwable t) {
                Util.hideProgress();
                Util.showError(MainActivity.this, getString(R.string.shows));
            }
        });
    }

    private void setShows(List<Show> showsList){
        adapter.setShows(showsList);
        emptyShower.setVisibility(View.INVISIBLE);
    }

    private void initAdapter(){
        adapter = new ShowsAdapter(new ArrayList<>(), action(), R.layout.item_show_grid);
    }

    private ShowsAdapter.OnItemClickAction action(){
        return (showID, sharedView) -> {
            Intent intent = EpisodesActivity.newIntentInstance(this, showID);

            ActivityOptions transitionActivityOptions =
                    ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, sharedView, getString(R.string.show_image));

            startActivity(intent, transitionActivityOptions.toBundle());
        };
    }

    private void initRecycler(){
        linearManager = new LinearLayoutManager(this);
        gridManager = new GridLayoutManager(this, 2);

        recyclerView.setHasFixedSize(true);
        setLayout();
    }

    private void setLayout(){
        if(linear){
            recyclerView.setLayoutManager(linearManager);
        }else{
            recyclerView.setLayoutManager(gridManager);
        }

        if(adapter == null) initAdapter();

        if(linear){
            adapter.setLayout(R.layout.item_show);
        }else{
            adapter.setLayout(R.layout.item_show_grid);
        }

        recyclerView.setAdapter(adapter);
    }

    private void logOut(){
        new AlertDialog.Builder(this)
                .setTitle(R.string.log_out_title)
                .setMessage(getString(R.string.log_out))
                .setNegativeButton(getString(R.string.cancel), null)
                .setPositiveButton(getString(R.string.plain_yes), (dialog, which) -> {
                    logOutAction();
                    Provider.clearToken(MainActivity.this);
                })
                .create()
                .show();
    }

    private void logOutAction(){
        Intent intent = LoginActivity.newIntentInstance(this);
        startActivity(intent);
        finish();
    }

    private void insertShowsDatabase(List<Show> showsList) {
        Provider.getShowsRepository(this).insertShows(showsList, new DatabaseCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
            }

            @Override
            public void onError(Throwable t) {
                Util.showError(MainActivity.this, getString(R.string.shows));
            }
        });
    }

    private void setOnRefreshListener(){
        swipeRefresh.setOnRefreshListener(() -> {
            swipeRefresh.setRefreshing(true);

            if(!Util.isInternetAvailable(MainActivity.this)){
                Toast.makeText(MainActivity.this, R.string.no_connection, Toast.LENGTH_SHORT).show();
            }else{
                loadShowsFromInternet();
            }

            swipeRefresh.setRefreshing(false);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_layout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logOutItem:
                logOut();
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.changeLayoutButton)
    void changeLayout(){
        if(linear){
            linear = false;
            changeLayoutButton.setImageResource(R.drawable.ic_list_icon_white);
        }else{
            linear = true;
            changeLayoutButton.setImageResource(R.drawable.ic_grid_white);
        }

        setLayout();
    }

    public static Intent newIntentInstance(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }
}
