package srsen.martin.infinum.co.hw3_and_on;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.pm.PackageManager.PERMISSION_DENIED;

public class AddEpisodeActivity extends AppCompatActivity {

    @BindView(R.id.nameEdit)
    EditText nameEdit;

    @BindView(R.id.descriptionEdit)
    EditText descriptionEdit;

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

    public static final String EXTRA_EPISODE = "srsen.martin.infinum.co.episode";
    public static final int REQUEST_CODE_TAKE_IMAGE = 199;
    public static final int REQUEST_CODE_CHOOSE_IMAGE = 200;
    public static final int REQUEST_CODE_PERMISSION_CAMERA = 201;
    public static final int REQUEST_CODE_PERMISSION_EXTERNAL_STORAGE = 202;

    private static final String SAVE_NAME_KEY = "save_name";
    private static final String SAVE_DESCRIPTION_KEY = "save_description";
    private static final String SAVE_EPISODE_NUMBER_KEY = "save_episode_number";
    private static final String SAVE_SEASON_NUMBER_KEY = "save_season_number";
    private static final String SAVE_IMAGE_KEY = "save_image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_episode);
        ButterKnife.bind(this);

        if(savedInstanceState != null)  restoreState(savedInstanceState);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.addEpisodeToolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    private void restoreState(Bundle savedInstanceState){
        nameEdit.setText(savedInstanceState.getString(SAVE_NAME_KEY));
        descriptionEdit.setText(savedInstanceState.getString(SAVE_DESCRIPTION_KEY));
        selectedEpisode = savedInstanceState.getInt(SAVE_EPISODE_NUMBER_KEY);
        selectedSeason = savedInstanceState.getInt(SAVE_SEASON_NUMBER_KEY);
        imageUri = savedInstanceState.getParcelable(SAVE_IMAGE_KEY);

        setImage(imageUri);

        if(selectedSeason == 0) return;
        chosenEpisodeSeason.setText(
                String.format(getString(R.string.episode_shower), selectedSeason, selectedEpisode)
        );
    }

    @OnClick(R.id.saveButton)
    void saveAction() {
        String name = nameEdit.getText().toString();
        String description = descriptionEdit.getText().toString();

        if(name.isEmpty() || description.isEmpty() || chosenEpisodeSeason.getText().equals("Unknown") || imageUri == null){
            Toast.makeText(this, getString(R.string.empty_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        Episode episode = new Episode(name, description, selectedSeason, selectedEpisode, imageUri);
        Intent intent = new Intent();
        intent.putExtra(EXTRA_EPISODE, episode);
        setResult(RESULT_OK, intent);

        finish();
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

    @OnClick(R.id.chosenSeasonEpisode)
    void choseEpisodeNumber(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.ThemeOverlay_AppCompat_Dialog);
        View dialogView = getLayoutInflater().inflate(R.layout.number_picker_layout, null);

        NumberPicker episodePicker = dialogView.findViewById(R.id.episodePicker);
        NumberPicker seasonPicker = dialogView.findViewById(R.id.seasonPicker);
        seasonPicker.setMinValue(1);
        episodePicker.setMinValue(1);
        seasonPicker.setMaxValue(20);
        episodePicker.setMaxValue(99);

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
        startActivityForResult(intent, REQUEST_CODE_TAKE_IMAGE);
    }

    private void onGalleryClicked(){
        if(!Util.askPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, R.string.external_storage_permission,
                REQUEST_CODE_PERMISSION_EXTERNAL_STORAGE)) return;

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_CHOOSE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_TAKE_IMAGE:
                if(resultCode == RESULT_OK){
                    Bitmap imgBitmap = (Bitmap) data.getExtras().get("data");
                    Uri imageUri = Util.saveImageAndGetUri(this, imgBitmap);
                    setImage(imageUri);
                }else{
                    Toast.makeText(this, R.string.unable_to_photo, Toast.LENGTH_SHORT).show();
                }
                break;

            case REQUEST_CODE_CHOOSE_IMAGE:
                if(resultCode == RESULT_OK && data != null){
                    Uri content = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), content);
                        content = Util.saveImageAndGetUri(this, bitmap);
                    } catch (IOException e) {
                        Toast.makeText(this, R.string.unable_to_photo, Toast.LENGTH_SHORT).show();
                    }

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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults.length == 0 || grantResults[0] == PERMISSION_DENIED)    return;

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
                String.format(getString(R.string.episode_shower), selectedSeason, selectedEpisode)
        );

        dialog.dismiss();
    }

    public static Intent newIntentInstance(Context context){
        Intent intent = new Intent(context, AddEpisodeActivity.class);
        return intent;
    }

    @Override
    public void onBackPressed() {
        checkToGoBack();
    }

    private void checkToGoBack(){
        String name = nameEdit.getText().toString();
        String description = descriptionEdit.getText().toString();

        if(name.isEmpty() && description.isEmpty() && chosenEpisodeSeason.getText().equals("Unknown") && imageUri == null){
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

        outState.putString(SAVE_NAME_KEY, nameEdit.getText().toString());
        outState.putString(SAVE_DESCRIPTION_KEY, descriptionEdit.getText().toString());
        outState.putParcelable(SAVE_IMAGE_KEY, imageUri);
        outState.putInt(SAVE_EPISODE_NUMBER_KEY, selectedEpisode);
        outState.putInt(SAVE_SEASON_NUMBER_KEY, selectedSeason);
    }
}
