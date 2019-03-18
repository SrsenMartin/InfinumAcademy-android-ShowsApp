package srsen.martin.infinum.co.hw3_and_on.database.runnable;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.List;

import srsen.martin.infinum.co.hw3_and_on.database.DatabaseCallback;
import srsen.martin.infinum.co.hw3_and_on.database.factory.EpisodesDatabaseFactory;
import srsen.martin.infinum.co.hw3_and_on.models.Comment;

public class InsertCommentsRunnable implements Runnable {

    private final DatabaseCallback<Void> callback;
    private final Context context;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final List<Comment> commentList;

    public InsertCommentsRunnable(Context context, List<Comment> commentList, DatabaseCallback<Void> callback) {
        this.context = context;
        this.commentList = commentList;
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            EpisodesDatabaseFactory.db(context).episodesDao().insertComments(commentList);
            mainHandler.post(() -> callback.onSuccess(null));
        } catch (final Exception e) {
            mainHandler.post(() -> callback.onError(e));
        }
    }
}
