package srsen.martin.infinum.co.hw3_and_on;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import srsen.martin.infinum.co.hw3_and_on.database.repository.EpisodesRepository;
import srsen.martin.infinum.co.hw3_and_on.database.repository.EpisodesRepositoryImpl;
import srsen.martin.infinum.co.hw3_and_on.database.repository.ShowsRepository;
import srsen.martin.infinum.co.hw3_and_on.database.repository.ShowsRepositoryImpl;
import srsen.martin.infinum.co.hw3_and_on.networking.ApiService;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class Util {

    public static final String BASE_URL = "https://api.infinum.academy/";

    private static final String SEPARATOR = "ß-//-";
    private static final String NEWLINE_SEPARATOR = "ß-ß--";

    private static final Pattern email_regex_pattern = Pattern.compile(
            "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$"
    );

    private static ApiService apiService;
    private static ShowsRepository showsRepository;
    private static EpisodesRepository episodesRepository;
    private static Dialog progressDialog;


    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static Uri getImageUri(String imagePartUri){
        return Uri.parse(Util.BASE_URL + imagePartUri.substring(1));
    }

    public static OkHttpClient createOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
    }

    public static ApiService initApiService(){
        if(apiService == null){
            apiService = getApiService();
        }

        return apiService;
    }

    public static ShowsRepository initShowsRepository(Context context){
        if(showsRepository == null){
            showsRepository = new ShowsRepositoryImpl(context);
        }

        return showsRepository;
    }

    public static EpisodesRepository initEpisodesRepository(Context context){
        if(episodesRepository == null){
            episodesRepository = new EpisodesRepositoryImpl(context);
        }

        return episodesRepository;
    }

    private static ApiService getApiService() {
        return new Retrofit.Builder()
                .baseUrl(Util.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(createOkHttpClient())
                .build()
                .create(ApiService.class);
    }

    public static void hideProgress(Dialog progressDialog) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public static void setImage(Context context, Uri uri, ImageView container, View defaultView){
        if(defaultView == null){
            Glide.with(context).load(uri).into(container);
            return;
        }

        Glide.with(context).load(uri).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                defaultView.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                defaultView.setVisibility(View.INVISIBLE);
                return false;
            }
        }).into(container);
    }

    public static Dialog showProgress(Context context, String title, String message, boolean indeterminate, boolean cencelable) {
        return ProgressDialog.show(context, title, message, indeterminate, cencelable);
    }

    public static void showError(Context context, String title) {
        new android.support.v7.app.AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(R.string.unknown_error)
                .setPositiveButton("OK", null)
                .create()
                .show();
    }

    public static boolean checkEmailEdit(Context context, TextInputLayout emailEdit){
        String email = emailEdit.getEditText().getText().toString();

        boolean validEmail = Util.isValidEmail(email);

        if(validEmail){
            emailEdit.setError(null);
        }else{
            emailEdit.setError(context.getString(R.string.invalid_email));
        }

        return validEmail;
    }

    public static boolean checkPasswordEdit(Context context, TextInputLayout passwordEdit){
        String password = passwordEdit.getEditText().getText().toString();
        boolean validPassword = Util.isValidPassword(password);

        if(validPassword){
            passwordEdit.setError(null);
        }else{
            passwordEdit.setError(context.getString(R.string.password_invalid));
        }

        return validPassword;
    }

    public static boolean askPermission(Activity context, String permission, int messageId, int requestCode){
        if(ActivityCompat.checkSelfPermission(context, permission)
                == PERMISSION_GRANTED)   return true;

        String[] permissions = new String[]{permission};
        if(ActivityCompat.shouldShowRequestPermissionRationale(context, permission)){
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setMessage(messageId);
            alert.setOnDismissListener(dialog -> requestNeededPermission(context, permissions, requestCode));

            alert.create().show();
        }else{
            requestNeededPermission(context, permissions, requestCode);
        }

        return false;
    }

    private static void requestNeededPermission(Activity context, String[] permissions, int requestCode){
            ActivityCompat.requestPermissions(context, permissions, requestCode);
    }

    public static File createImageFile(Context context) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        return image;
    }

    public static boolean isValidEmail(String email){
        Matcher emailMatcher = email_regex_pattern.matcher(email);

        if(emailMatcher.matches())  return true;

        return false;
    }

    public static boolean isValidPassword(String password){
        if(password.length() < 5)   return false;

        return true;
    }

}
