package srsen.martin.infinum.co.hw3_and_on;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import srsen.martin.infinum.co.hw3_and_on.models.Data;
import srsen.martin.infinum.co.hw3_and_on.models.Token;
import srsen.martin.infinum.co.hw3_and_on.models.User;

public interface ApiService {

    @POST("/api/users")
    Call<Void> registerUser(@Body User user);

    @POST("/api/users/sessions")
    Call<Data<Token>> loginUser(@Body User user);

    @GET("/api/shows")
    Call<Data<List<Show>>> getShows();
}
