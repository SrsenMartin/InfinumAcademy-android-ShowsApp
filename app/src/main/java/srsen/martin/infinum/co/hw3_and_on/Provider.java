package srsen.martin.infinum.co.hw3_and_on;

import android.content.Context;
import android.content.SharedPreferences;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import srsen.martin.infinum.co.hw3_and_on.database.repository.EpisodesRepository;
import srsen.martin.infinum.co.hw3_and_on.database.repository.EpisodesRepositoryImpl;
import srsen.martin.infinum.co.hw3_and_on.database.repository.ShowsRepository;
import srsen.martin.infinum.co.hw3_and_on.database.repository.ShowsRepositoryImpl;
import srsen.martin.infinum.co.hw3_and_on.networking.ApiService;

import static android.content.Context.MODE_PRIVATE;

public class Provider {

    public static final String SHARED_PREFERENCES_KEY = "login_shared_prederences";
    public static final String SHARED_PREFERENCES_TOKEN_KEY = "login_token";
    public static final String SHARED_PREFERENCES_REMEMBER_KEY = "remember";

    private static ApiService apiService;
    private static ShowsRepository showsRepository;
    private static EpisodesRepository episodesRepository;

    public static EpisodesRepository getEpisodesRepository(Context context){
        if(episodesRepository == null){
            episodesRepository = new EpisodesRepositoryImpl(context);
        }

        return episodesRepository;
    }

    private static ApiService createApiService() {
        return new Retrofit.Builder()
                .baseUrl(Util.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(createOkHttpClient())
                .build()
                .create(ApiService.class);
    }

    public static ApiService getApiService(){
        if(apiService == null){
            apiService = createApiService();
        }

        return apiService;
    }

    public static ShowsRepository getShowsRepository(Context context){
        if(showsRepository == null){
            showsRepository = new ShowsRepositoryImpl(context);
        }

        return showsRepository;
    }

    private static OkHttpClient createOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
    }

    public static String getToken(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_KEY, MODE_PRIVATE);

        String token = sharedPreferences.getString(SHARED_PREFERENCES_TOKEN_KEY, null);
        return token;
    }

    public static void saveToken(Context context, String token, boolean remember){
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFERENCES_KEY, MODE_PRIVATE).edit();

        editor.putString(SHARED_PREFERENCES_TOKEN_KEY, token);
        editor.putBoolean(SHARED_PREFERENCES_REMEMBER_KEY, remember);

        editor.apply();
    }

    public static void clearToken(Context context){
        context.getSharedPreferences(SHARED_PREFERENCES_KEY, MODE_PRIVATE)
                .edit()
                .clear()
                .apply();
    }

    public static boolean getRememberState(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_KEY, MODE_PRIVATE);

        boolean rememberState = sharedPreferences.getBoolean(SHARED_PREFERENCES_REMEMBER_KEY, false);
        return rememberState;
    }
}
