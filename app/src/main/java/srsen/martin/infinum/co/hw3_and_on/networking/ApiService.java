package srsen.martin.infinum.co.hw3_and_on.networking;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import srsen.martin.infinum.co.hw3_and_on.models.Comment;
import srsen.martin.infinum.co.hw3_and_on.models.Data;
import srsen.martin.infinum.co.hw3_and_on.models.Episode;
import srsen.martin.infinum.co.hw3_and_on.models.MediaResponse;
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

    @POST("/api/shows/{showId}/like")
    Call<Void> likeShow(@Header("Authorization") String token, @Path("showId") String showId);

    @POST("/api/shows/{showId}/dislike")
    Call<Void> dislikeShow(@Header("Authorization") String token, @Path("showId") String showId);

    @POST("/api/media")
    @Multipart
    Call<Data<MediaResponse>> uploadMedia(@Header("Authorization") String token, @Part("file\"; filename=\"image.jpg\"") RequestBody request);

    @POST("/api/episodes")
    Call<Data<Episode>> addEpisode(@Header("Authorization") String token, @Body Episode episode);

    @GET("/api/episodes/{episodeId}/comments")
    Call<Data<List<Comment>>> getComments(@Path("episodeId") String episodeId);

    @POST("/api/comments")
    Call<Data<Comment>> postComment(@Header("Authorization") String token, @Body Comment comment);
}
