package srsen.martin.infinum.co.hw3_and_on.networking;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import srsen.martin.infinum.co.hw3_and_on.models.Data;
import srsen.martin.infinum.co.hw3_and_on.models.Episode;
import srsen.martin.infinum.co.hw3_and_on.models.Show;
import srsen.martin.infinum.co.hw3_and_on.models.Token;
import srsen.martin.infinum.co.hw3_and_on.models.User;

public interface ApiService {

    @POST("/api/users")
    Call<Void> registerUser(@Body User user);

    @POST("/api/users/sessions")
    Call<Data<Token>> loginUser(@Body User user);

    @GET("/api/shows")
    Call<Data<List<Show>>> getShows();

    @GET("/api/shows/{showId}")
    Call<Data<Show>> getShowDetails(@Path("showId") String showId);

    @GET("/api/shows/{showId}/episodes")
    Call<Data<List<Episode>>> getEpisodes(@Path("showId") String showId);
}
