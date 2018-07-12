package srsen.martin.infinum.co.hw3_and_on;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddEpisodeActivity extends AppCompatActivity {

    private EditText nameEdit;
    private EditText descriptionEdit;
    private Button saveButton;

    public static final String EXTRA_EPISODE_NAME = "srsen.martin.infinum.co.episodeName";
    public static final String EXTRA_EPISODE_DESCRIPTION = "srsen.martin.infinum.co.episodeDescription";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_episode);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.addEpisodeToolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        nameEdit = findViewById(R.id.nameEdit);
        descriptionEdit = findViewById(R.id.descriptionEdit);
        saveButton = findViewById(R.id.saveButton);

        addSaveAction();
    }

    private void addSaveAction() {
        saveButton.setOnClickListener(v -> {
            String name = nameEdit.getText().toString();
            String description = descriptionEdit.getText().toString();

            if(name.isEmpty() || description.isEmpty()){
                Toast.makeText(this, getString(R.string.empty_fields), Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = EpisodesActivity.newIntentInstance(this);
            intent.putExtra(EXTRA_EPISODE_NAME, name);
            intent.putExtra(EXTRA_EPISODE_DESCRIPTION, description);
            setResult(RESULT_OK, intent);

            finish();
        });
    }

    public static Intent newIntentInstance(Context context){
        Intent intent = new Intent(context, AddEpisodeActivity.class);
        return intent;
    }
}
