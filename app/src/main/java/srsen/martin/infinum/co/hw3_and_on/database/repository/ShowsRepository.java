package srsen.martin.infinum.co.hw3_and_on.database.repository;

import java.util.List;

import srsen.martin.infinum.co.hw3_and_on.models.Show;
import srsen.martin.infinum.co.hw3_and_on.database.DatabaseCallback;

public interface ShowsRepository {

    void getShows(DatabaseCallback<List<Show>> callback);
    void insertShows(List<Show> shows, DatabaseCallback<Void> callback);
    void getShow(String showId, DatabaseCallback<Show> callback);

    void updateShowDescription(String showDescription, String showId, DatabaseCallback<Void> callback);

    void updateLikeStatus(Boolean status, int newLikes, String showId, DatabaseCallback<Void> callback);
    void getLikeStatus(String showId, DatabaseCallback<Boolean> callback);
}
