package srsen.martin.infinum.co.hw3_and_on.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import srsen.martin.infinum.co.hw3_and_on.R;
import srsen.martin.infinum.co.hw3_and_on.Util;
import srsen.martin.infinum.co.hw3_and_on.models.Episode;

public class EpisodeDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_EPISODE = "srsen.martin.infinum.co.details.episode";

    @BindView(R.id.detailsImage)
    ImageView detailsImage;

    @BindView(R.id.detailsName)
    TextView detailsName;

    @BindView(R.id.detailsDescription)
    TextView detailsDesctiption;

    @BindView(R.id.detailsEpisode)
    TextView detailsEpisode;

    @BindView(R.id.detailsToolbar)
    Toolbar toolbar;

    private Episode episode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_details);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        showDetails();
    }

    private void showDetails(){
        if(getIntent() == null || getIntent().getParcelableExtra(EXTRA_EPISODE) == null){
            Toast.makeText(this, R.string.episode_extra, Toast.LENGTH_SHORT).show();
            finish();
        }

        episode = getIntent().getParcelableExtra(EXTRA_EPISODE);

        Uri imageUri = Util.getImageUri(episode.getImageUri());
        Util.setImage(this, imageUri, detailsImage, findViewById(R.id.emptyPlaceholder));

        detailsName.setText(episode.getTitle());
        detailsDesctiption.setText(episode.getDescription());
        detailsEpisode.setText(String.format(getString(R.string.episode_shower), episode.getSeason(), episode.getEpisode()));
    }

    @OnClick(R.id.commentsButton)
    public void onCommentsPressed(){
        Intent intent = CommentsActivity.newIntentInstance(this, episode.getId());
        startActivity(intent);
    }

    public static Intent newIntentInstance(Context context, Episode episode){
        Intent intent = new Intent(context, EpisodeDetailsActivity.class);
        intent.putExtra(EXTRA_EPISODE, episode);

        return intent;
    }
}
