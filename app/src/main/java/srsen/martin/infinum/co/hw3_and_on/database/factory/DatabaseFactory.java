package srsen.martin.infinum.co.hw3_and_on.database.factory;

import android.arch.persistence.room.Room;
import android.content.Context;

import srsen.martin.infinum.co.hw3_and_on.database.AppDatabase;

public class DatabaseFactory {

    private static AppDatabase database = null;

    private DatabaseFactory() {
    }

    public static AppDatabase db(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context, AppDatabase.class, "shows-database").build();
        }

        return database;
    }
}
