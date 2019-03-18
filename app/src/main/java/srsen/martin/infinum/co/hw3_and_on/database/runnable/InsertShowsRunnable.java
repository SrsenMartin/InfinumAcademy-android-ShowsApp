package srsen.martin.infinum.co.hw3_and_on.database.runnable;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.List;

import srsen.martin.infinum.co.hw3_and_on.models.Show;
import srsen.martin.infinum.co.hw3_and_on.database.DatabaseCallback;
import srsen.martin.infinum.co.hw3_and_on.database.factory.DatabaseFactory;

public class InsertShowsRunnable implements Runnable {
    private final DatabaseCallback<Void> callback;
    private final Context context;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final List<Show> showsList;

    public InsertShowsRunnable(Context context, List<Show> showsList, DatabaseCallback<Void> callback) {
        this.context = context;
        this.showsList = showsList;
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            DatabaseFactory.db(context).showsDao().insertAll(showsList);
            mainHandler.post(() -> callback.onSuccess(null));
        } catch (final Exception e) {
            mainHandler.post(() -> callback.onError(e));
        }
    }
}
