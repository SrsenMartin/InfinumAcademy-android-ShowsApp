package srsen.martin.infinum.co.hw3_and_on.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import srsen.martin.infinum.co.hw3_and_on.models.Episode;

@Dao
public interface EpisodesDao {

    @Query("SELECT * FROM episode where showId = :showId")
    List<Episode> getAllEpisodes(String showId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEpisodes(List<Episode> episodes);
}
