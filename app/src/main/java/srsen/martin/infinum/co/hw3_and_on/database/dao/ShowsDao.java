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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Show> showList);

    @Query("SELECT * FROM show WHERE ID = :showId")
    Show getShow(String showId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertShow(Show show);
}
