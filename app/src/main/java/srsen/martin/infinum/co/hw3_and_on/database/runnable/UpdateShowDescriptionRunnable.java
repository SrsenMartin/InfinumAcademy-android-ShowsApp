package srsen.martin.infinum.co.hw3_and_on.database.runnable;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import srsen.martin.infinum.co.hw3_and_on.database.DatabaseCallback;
import srsen.martin.infinum.co.hw3_and_on.database.factory.DatabaseFactory;

public class UpdateShowDescriptionRunnable implements Runnable {

    private final DatabaseCallback<Void> callback;
    private final Context context;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private final String showDescription;
    private final String showId;

    public UpdateShowDescriptionRunnable(Context context, String showDescription, String showId, DatabaseCallback<Void> callback){
        this.callback = callback;
        this.context = context;

        this.showDescription = showDescription;
        this.showId = showId;
    }

    @Override
    public void run() {
        try {
            DatabaseFactory.db(context).showsDao().updateShowDescription(showDescription, showId);
            mainHandler.post(() -> callback.onSuccess(null));
        } catch (final Exception e) {
            mainHandler.post(() -> callback.onError(e));
        }
    }
}
