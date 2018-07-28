package srsen.martin.infinum.co.hw3_and_on.database.runnable;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import srsen.martin.infinum.co.hw3_and_on.models.Show;
import srsen.martin.infinum.co.hw3_and_on.database.DatabaseCallback;
import srsen.martin.infinum.co.hw3_and_on.database.factory.DatabaseFactory;

public class InsertShowRunnable implements Runnable {

    private final DatabaseCallback<Void> callback;
    private final Context context;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final Show show;

    public InsertShowRunnable(Context context, Show show, DatabaseCallback<Void> callback) {
        this.context = context;
        this.show = show;
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            DatabaseFactory.db(context).showsDao().insertShow(show);
            mainHandler.post(() -> callback.onSuccess(null));
        } catch (final Exception e) {
            mainHandler.post(() -> callback.onError(e));
        }
    }
}
