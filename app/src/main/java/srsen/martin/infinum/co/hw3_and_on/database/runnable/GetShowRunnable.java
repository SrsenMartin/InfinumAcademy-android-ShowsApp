package srsen.martin.infinum.co.hw3_and_on.database.runnable;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import srsen.martin.infinum.co.hw3_and_on.models.Show;
import srsen.martin.infinum.co.hw3_and_on.database.DatabaseCallback;
import srsen.martin.infinum.co.hw3_and_on.database.factory.DatabaseFactory;

public class GetShowRunnable implements Runnable {

    private final DatabaseCallback<Show> callback;
    private final Context context;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private String showId;

    public GetShowRunnable(Context context, DatabaseCallback<Show> callback, String showId) {
        this.context = context;
        this.callback = callback;
        this.showId = showId;
    }

    @Override
    public void run() {
        try {
            final Show show = DatabaseFactory.db(context).showsDao().getShow(showId);
            mainHandler.post(() -> callback.onSuccess(show));
        } catch (final Exception e) {
            mainHandler.post(() -> callback.onError(e));
        }
    }
}
