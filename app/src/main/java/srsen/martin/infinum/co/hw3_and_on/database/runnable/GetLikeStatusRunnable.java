package srsen.martin.infinum.co.hw3_and_on.database.runnable;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import srsen.martin.infinum.co.hw3_and_on.database.DatabaseCallback;
import srsen.martin.infinum.co.hw3_and_on.database.factory.DatabaseFactory;

public class GetLikeStatusRunnable implements Runnable {

    private final DatabaseCallback<Boolean> callback;
    private final Context context;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private String showId;

    public GetLikeStatusRunnable(Context context, String showId, DatabaseCallback<Boolean> callback) {
        this.context = context;
        this.callback = callback;
        this.showId = showId;
    }

    @Override
    public void run() {
        try {
            final Boolean likeStatus = DatabaseFactory.db(context).showsDao().getLikeStatus(showId);
            mainHandler.post(() -> callback.onSuccess(likeStatus));
        } catch (final Exception e) {
            mainHandler.post(() -> callback.onError(e));
        }
    }
}
