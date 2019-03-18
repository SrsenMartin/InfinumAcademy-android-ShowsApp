package srsen.martin.infinum.co.hw3_and_on.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import srsen.martin.infinum.co.hw3_and_on.BuildConfig;
import srsen.martin.infinum.co.hw3_and_on.Provider;
import srsen.martin.infinum.co.hw3_and_on.R;
import srsen.martin.infinum.co.hw3_and_on.Util;
import srsen.martin.infinum.co.hw3_and_on.models.Data;
import srsen.martin.infinum.co.hw3_and_on.models.Episode;
import srsen.martin.infinum.co.hw3_and_on.models.MediaResponse;

import static android.content.pm.PackageManager.PERMISSION_DENIED;

public class AddEpisodeActivity extends AppCompatActivity {

    public static final String EXTRA_SHOW_ID = "extra_show_id";

    @BindView(R.id.nameEdit)
    TextInputLayout nameEdit;

    @BindView(R.id.descriptionEdit)
    TextInputLayout descriptionEdit;

    @BindView(R.id.saveButton)
    Button saveButton;

    @BindView(R.id.addPhotoArea)
    RelativeLayout addPhotoArea;

    @BindView(R.id.chosenSeasonEpisode)
    TextView chosenEpisodeSeason;

    @BindView(R.id.chosenImage)
    ImageView chosenImage;

    private int selectedEpisode;
    private int selectedSeason;
    private Uri imageUri;
    private boolean imageSet;

    private String showId;
    private File imageFile;

    public static final String EXTRA_EPISODE = "srsen.martin.infinum.co.episode";
    public static final int REQUEST_CODE_TAKE_IMAGE = 199;
    public static final int REQUEST_CODE_CHOOSE_IMAGE = 200;
    public static final int REQUEST_CODE_PERMISSION_CAMERA = 201;
    public static final int REQUEST_CODE_PERMISSION_EXTERNAL_STORAGE = 202;

    private static final String SAVE_EPISODE_NUMBER_KEY = "save_episode_number";
    private static final String SAVE_SEASON_NUMBER_KEY = "save_season_number";
    private static final String SAVE_IMAGE_KEY = "save_image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_episode);
        ButterKnife.bind(this);

        if(savedInstanceState != null)  restoreState(savedInstanceState);

        Toolbar toolbar = findViewById(R.id.addEpisodeToolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        showId = getShowId();
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

    private void restoreState(Bundle savedInstanceState){
        selectedEpisode = savedInstanceState.getInt(SAVE_EPISODE_NUMBER_KEY);
        selectedSeason = savedInstanceState.getInt(SAVE_SEASON_NUMBER_KEY);
        imageUri = savedInstanceState.getParcelable(SAVE_IMAGE_KEY);

        setImage(imageUri);

        if(selectedSeason == 0) return;
        chosenEpisodeSeason.setText(
                String.format(getString(R.string.episode_shower), selectedSeason + "", selectedEpisode + "")
        );
    }

    @OnClick(R.id.saveButton)
    void saveAction() {
        String name = nameEdit.getEditText().getText().toString();
        String description = descriptionEdit.getEditText().getText().toString();

        if(!checkEditText(name, description) || chosenEpisodeSeason.getText().equals("Unknown") || !imageSet){
            Toast.makeText(this, getString(R.string.empty_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        if(!Util.isInternetAvailable(this)){
            Toast.makeText(this, "Connection needed to add episode", Toast.LENGTH_LONG).show();
            return;
        }

        Episode episode = new Episode(null, name, description, selectedSeason + "", selectedEpisode + "", imageUri.toString(), showId);
        addEpisodeInternet(episode);
    }

    private void addEpisodeInternet(Episode episode){
        Util.showProgress(this, null, getString(R.string.episode_add), true, false);

        if(imageFile == null)   imageFile = new File(Util.getPath(this, imageUri));

        Provider.getApiService().uploadMedia(Provider.getToken(this), RequestBody.create(MediaType.parse("image/jpg"), imageFile))
                .enqueue(new Callback<Data<MediaResponse>>() {
                    @Override
                    public void onResponse(Call<Data<MediaResponse>> call, Response<Data<MediaResponse>> response) {
                        if(!response.isSuccessful()){
                            Util.hideProgress();
                            Util.showError(AddEpisodeActivity.this, getString(R.string.upload_image));
                            return;
                        }

                        String mediaId = response.body().getData().getMediaId();
                        episode.setMediaId(mediaId);

                        uploadEpisode(episode);
                    }

                    @Override
                    public void onFailure(Call<Data<MediaResponse>> call, Throwable t) {
                        Util.hideProgress();
                        Util.showError(AddEpisodeActivity.this, t.getMessage());
                    }
                });
    }

    private void uploadEpisode(Episode episode){
        Provider.getApiService().addEpisode(Provider.getToken(this), episode).enqueue(new Callback<Data<Episode>>() {
            @Override
            public void onResponse(Call<Data<Episode>> call, Response<Data<Episode>> response) {
                Util.hideProgress();

                if(!response.isSuccessful()){
                    Util.showError(AddEpisodeActivity.this, getString(R.string.add_episode));
                }else{
                    Episode returnEpisode = response.body().getData();
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_EPISODE, returnEpisode);
                    setResult(RESULT_OK, intent);

                    finish();
                    Toast.makeText(AddEpisodeActivity.this, getString(R.string.episode_success), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Data<Episode>> call, Throwable t) {
                Util.showError(AddEpisodeActivity.this, getString(R.string.add_episode));
                Util.hideProgress();
            }
        });
    }

    private boolean checkEditText(String name, String description){
        boolean validName = !name.isEmpty();
        boolean validDescription = Util.isValidDescription(description);

        if(validName){
            nameEdit.setError(null);
        }else{
            nameEdit.setError(getString(R.string.episode_empty_message));
        }

        if(validDescription){
            descriptionEdit.setError(null);
        }else{
            descriptionEdit.setError("Description must have at least 50 characters");
        }

        return validName && validDescription;
    }

    @OnClick(R.id.addPhotoArea)
    void addPhotoAction(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.ThemeOverlay_AppCompat_Dialog);
        View dialogView = getLayoutInflater().inflate(R.layout.image_option_chooser, null);

        LinearLayout takePhoto = dialogView.findViewById(R.id.takePhoto);
        LinearLayout gallery = dialogView.findViewById(R.id.gallery);

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        takePhoto.setOnClickListener(v -> {
            dialog.dismiss();
            onCameraClicked();
        });
        gallery.setOnClickListener(v -> {
            dialog.dismiss();
            onGalleryClicked();
        });

        dialog.show();
    }

    @OnClick(R.id.chooseEpisode)
    void choseEpisodeNumber(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.ThemeOverlay_AppCompat_Dialog);
        View dialogView = getLayoutInflater().inflate(R.layout.number_picker_layout, null);

        NumberPicker episodePicker = dialogView.findViewById(R.id.episodePicker);
        NumberPicker seasonPicker = dialogView.findViewById(R.id.seasonPicker);
        seasonPicker.setMinValue(1);
        episodePicker.setMinValue(1);
        seasonPicker.setMaxValue(20);
        episodePicker.setMaxValue(99);
        if(selectedEpisode != 0){
            seasonPicker.setValue(selectedSeason);
            episodePicker.setValue(selectedEpisode);
        }

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        Button saveEpisode = dialogView.findViewById(R.id.savePickedEpisode);
        saveEpisode.setOnClickListener(v -> onSaveEpisodeNumber(episodePicker, seasonPicker, dialog));

        dialog.show();
    }

    private void onCameraClicked(){
        if(!Util.askPermission(this, Manifest.permission.CAMERA, R.string.camera_permission,
                REQUEST_CODE_PERMISSION_CAMERA)) return;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) == null){
            Toast.makeText(this, R.string.camera_unavailable, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            imageFile = Util.createImageFile(this);
        } catch (IOException ex) {
            Toast.makeText(this, R.string.error_file, Toast.LENGTH_SHORT).show();
            return;
        }

        imageUri = FileProvider.getUriForFile(this,
                BuildConfig.APPLICATION_ID,
                imageFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        startActivityForResult(intent, REQUEST_CODE_TAKE_IMAGE);
    }

    private void onGalleryClicked(){
        if(!Util.askPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, R.string.external_storage_permission,
                REQUEST_CODE_PERMISSION_EXTERNAL_STORAGE)) return;

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");

        startActivityForResult(intent, REQUEST_CODE_CHOOSE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_TAKE_IMAGE:
                if(resultCode == RESULT_OK){
                    setImage(imageUri);
                }else{
                    Toast.makeText(this, R.string.unable_to_photo, Toast.LENGTH_SHORT).show();
                }
                break;

            case REQUEST_CODE_CHOOSE_IMAGE:
                if(resultCode == RESULT_OK && data != null){
                    Uri content = data.getData();
                    setImage(content);
                }else{
                    Toast.makeText(this, R.string.unable_to_photo, Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void setImage(Uri imageUri){
        if(imageUri == null)    return;

        this.imageUri = imageUri;
        Glide.with(this).load(imageUri).into(chosenImage);

        addPhotoArea.setVisibility(View.INVISIBLE);
        imageSet = true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults.length == 0 || grantResults[0] == PERMISSION_DENIED){
            Toast.makeText(this, R.string.permission_needed, Toast.LENGTH_SHORT).show();
            return;
        }

        switch(requestCode){
            case REQUEST_CODE_PERMISSION_CAMERA:
                onCameraClicked();
                break;
            case REQUEST_CODE_PERMISSION_EXTERNAL_STORAGE:
                onGalleryClicked();
        }
    }

    private void onSaveEpisodeNumber(NumberPicker episodePicker, NumberPicker seasonPicker, AlertDialog dialog){
        selectedSeason = seasonPicker.getValue();
        selectedEpisode = episodePicker.getValue();

        chosenEpisodeSeason.setText(
                String.format(getString(R.string.episode_shower), selectedSeason + "", selectedEpisode + "")
        );

        dialog.dismiss();
    }

    public static Intent newIntentInstance(Context context, String showId){
        Intent intent = new Intent(context, AddEpisodeActivity.class);
        intent.putExtra(EXTRA_SHOW_ID, showId);

        return intent;
    }

    @Override
    public void onBackPressed() {
        checkToGoBack();
    }

    private void checkToGoBack(){
        String name = nameEdit.getEditText().getText().toString();
        String description = descriptionEdit.getEditText().getText().toString();

        if(name.isEmpty() && description.isEmpty() && chosenEpisodeSeason.getText().equals("Unknown") && !imageSet){
            super.onBackPressed();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.back_action))
                .setNegativeButton(getString(R.string.no), null)
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> super.onBackPressed())
                .create()
                .show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(SAVE_IMAGE_KEY, imageUri);
        outState.putInt(SAVE_EPISODE_NUMBER_KEY, selectedEpisode);
        outState.putInt(SAVE_SEASON_NUMBER_KEY, selectedSeason);
    }
}
