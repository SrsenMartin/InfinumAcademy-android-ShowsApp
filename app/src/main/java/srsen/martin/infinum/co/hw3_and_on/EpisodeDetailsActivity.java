package srsen.martin.infinum.co.hw3_and_on;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EpisodeDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_EPISODE = "srsen.martin.infinum.co.details.episode";
    public static final String EXTRA_SHOW_NAME = "srsen.martin.infinum.co.details.show.name";

    @BindView(R.id.detailsImage)
    ImageView detailsImage;

    @BindView(R.id.detailsName)
    TextView detailsName;

    @BindView(R.id.detailsDescription)
    TextView detailsDesctiption;

    @BindView(R.id.detailsEpisode)
    TextView detailsEpisode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_details);
        ButterKnife.bind(this);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.detailsToolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        showDetails();
    }

    private void showDetails(){
        if(getIntent() == null || getIntent().getParcelableExtra(EXTRA_EPISODE) == null ||
                getIntent().getStringExtra(EXTRA_SHOW_NAME) == null){
            Toast.makeText(this, getString(R.string.id_extra), Toast.LENGTH_SHORT).show();
            finish();
        }

        Episode episode = getIntent().getParcelableExtra(EXTRA_EPISODE);
        String showName = getIntent().getStringExtra(EXTRA_SHOW_NAME);
        getSupportActionBar().setTitle(showName);

        Glide.with(this).load(episode.getImageUri()).into(detailsImage);
        detailsName.setText(episode.getName());
        detailsDesctiption.setText(episode.getDescription());
        detailsEpisode.setText(String.format(getString(R.string.episode_shower), episode.getSeason(), episode.getEpisode()));
    }

    public static Intent newIntentInstance(Context context, Episode episode, String showName){
        Intent intent = new Intent(context, EpisodeDetailsActivity.class);
        intent.putExtra(EXTRA_EPISODE, episode);
        intent.putExtra(EXTRA_SHOW_NAME, showName);

        return intent;
    }
}
