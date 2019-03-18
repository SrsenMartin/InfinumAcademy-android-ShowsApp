package srsen.martin.infinum.co.hw3_and_on.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import srsen.martin.infinum.co.hw3_and_on.models.Show;

@Dao
public interface ShowsDao {

    @Query("SELECT * FROM show")
    List<Show> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<Show> showList);

    @Query("SELECT * FROM show WHERE ID = :showId")
    Show getShow(String showId);

    @Query("UPDATE show SET description = :description WHERE ID = :showId")
    void updateShowDescription(String description, String showId);

    @Query("UPDATE show SET likeStatus = :status, likes = :newLikes WHERE ID = :showId")
    void updateLikeStatus(Boolean status, int newLikes, String showId);

    @Query("SELECT likeStatus FROM show WHERE ID = :showId")
    Boolean getLikeStatus(String showId);
}
