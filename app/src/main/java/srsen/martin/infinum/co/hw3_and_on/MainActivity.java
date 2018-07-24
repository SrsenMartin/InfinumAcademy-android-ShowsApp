package srsen.martin.infinum.co.hw3_and_on;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import srsen.martin.infinum.co.hw3_and_on.models.Data;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.showsRecyclerView)
    RecyclerView recyclerView;

    private ShowsAdapter adapter;
    private Dialog progressDialog;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        apiService = Util.initApiService();
        initRecycler();
        getShows();
    }

    private void getShows(){
        progressDialog = Util.showProgress(this,
                getString(R.string.login), getString(R.string.loading), true, false);

        apiService.getShows().enqueue(new Callback<Data<List<Show>>>() {
            @Override
            public void onResponse(@NonNull Call<Data<List<Show>>> call, @NonNull Response<Data<List<Show>>> response) {
                Util.hideProgress(progressDialog);

                if(response.isSuccessful()){
                    List<Show> showsList = response.body().getData();
                    setShows(showsList);
                }else{
                    Toast.makeText(MainActivity.this, R.string.data_unable, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Data<List<Show>>> call, @NonNull Throwable t) {
                Util.hideProgress(progressDialog);
                Util.showError(MainActivity.this, getString(R.string.error_show_loading));
            }
        });
    }

    private void setShows(List<Show> showsList){
        if(adapter == null){
            adapter = new ShowsAdapter(showsList, (context, show) -> {
                Intent intent = EpisodesActivity.newIntentInstance(context, show.getID());
                context.startActivity(intent);
            }, R.layout.item_show_grid);
            recyclerView.setAdapter(adapter);
        }else{
            adapter.setShows(showsList);
        }
    }

    private void initRecycler(){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    private void logOut(){
        new AlertDialog.Builder(this)
                .setTitle(R.string.log_out_title)
                .setMessage(getString(R.string.log_out))
                .setNegativeButton(getText(R.string.plain_no), null)
                .setPositiveButton(getString(R.string.plain_yes), (dialog, which) -> {
                    super.onBackPressed();
                    getSharedPreferences(LoginActivity.SHARED_PREFERENCES_KEY, MODE_PRIVATE)
                            .edit()
                            .clear()
                            .apply();
                })
                .create()
                .show();
    }

    @Override
    public void onBackPressed() {
        logOut();
    }

    public static Intent newIntentInstance(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }
}
