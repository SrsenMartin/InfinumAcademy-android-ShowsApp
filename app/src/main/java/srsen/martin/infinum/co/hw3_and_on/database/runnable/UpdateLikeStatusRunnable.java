package srsen.martin.infinum.co.hw3_and_on.database.runnable;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import srsen.martin.infinum.co.hw3_and_on.database.DatabaseCallback;
import srsen.martin.infinum.co.hw3_and_on.database.factory.DatabaseFactory;

public class UpdateLikeStatusRunnable implements Runnable {

    private final DatabaseCallback<Void> callback;
    private final Context context;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private final Boolean status;
    private final String showId;
    private final int newLikes;

    public UpdateLikeStatusRunnable(Context context, Boolean status, int newLikes, String showId, DatabaseCallback<Void> callback){
        this.callback = callback;
        this.context = context;

        this.status = status;
        this.showId = showId;
        this.newLikes = newLikes;
    }

    @Override
    public void run() {
        try {
            DatabaseFactory.db(context).showsDao().updateLikeStatus(status, newLikes, showId);
            mainHandler.post(() -> callback.onSuccess(null));
        } catch (final Exception e) {
            mainHandler.post(() -> callback.onError(e));
        }
    }
}
