package srsen.martin.infinum.co.hw3_and_on;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class EpisodesActivity extends AppCompatActivity {

    public static final String EXTRA_SHOW_NAME = "srsen.martin.infinum.co.showName";

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

        String showName = getIntent().getStringExtra(EXTRA_SHOW_NAME);
        getSupportActionBar().setTitle(showName);
    }

    private void checkExtras() {
        if(getIntent() == null || getIntent().getStringExtra(EXTRA_SHOW_NAME) == null){
            Toast.makeText(this, getString(R.string.name_extra), Toast.LENGTH_SHORT).show();
            finish();
        }else if(!ShowsDB.containsShow(getIntent().getStringExtra(EXTRA_SHOW_NAME))){
            Toast.makeText(this, getString(R.string.not_contained_show), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.addIcon:
                Toast.makeText(EpisodesActivity.this, "Pressed to add", Toast.LENGTH_SHORT).show();
                return true;
        }

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    public static Intent newIntentInstance(Context context, String name){
        Intent intent = new Intent(context, EpisodesActivity.class);
        intent.putExtra(EXTRA_SHOW_NAME, name);

        return intent;
    }
}
