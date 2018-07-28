package srsen.martin.infinum.co.hw3_and_on.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import srsen.martin.infinum.co.hw3_and_on.models.Episode;
import srsen.martin.infinum.co.hw3_and_on.models.Show;
import srsen.martin.infinum.co.hw3_and_on.database.dao.EpisodesDao;
import srsen.martin.infinum.co.hw3_and_on.database.dao.ShowsDao;

@Database(
        entities = {
                Show.class,
                Episode.class
        },
        version = 1
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ShowsDao showsDao();
    public abstract EpisodesDao episodesDao();
}
