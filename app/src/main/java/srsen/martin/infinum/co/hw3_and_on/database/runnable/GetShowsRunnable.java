package srsen.martin.infinum.co.hw3_and_on.database.runnable;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.List;

import srsen.martin.infinum.co.hw3_and_on.models.Show;
import srsen.martin.infinum.co.hw3_and_on.database.DatabaseCallback;
import srsen.martin.infinum.co.hw3_and_on.database.factory.DatabaseFactory;

public class GetShowsRunnable implements Runnable {

    private final DatabaseCallback<List<Show>> callback;
    private final Context context;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public GetShowsRunnable(Context context, DatabaseCallback<List<Show>> callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            final List<Show> showsList = DatabaseFactory.db(context).showsDao().getAll();
            mainHandler.post(() -> callback.onSuccess(showsList));
        } catch (final Exception e) {
            mainHandler.post(() -> callback.onError(e));
        }
    }
}
