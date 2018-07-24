package srsen.martin.infinum.co.hw3_and_on;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;

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

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class Util {

    public static final String BASE_URL = "https://api.infinum.academy/";

    private static final String SEPARATOR = "ß-//-";
    private static final String NEWLINE_SEPARATOR = "ß-ß--";

    private static final Pattern email_regex_pattern = Pattern.compile(
            "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$"
    );

    private static ApiService apiService;

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
