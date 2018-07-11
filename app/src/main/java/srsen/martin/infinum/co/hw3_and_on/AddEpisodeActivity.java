package srsen.martin.infinum.co.hw3_and_on;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class AddEpisodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_episode);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.addEpisodeToolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }
}
