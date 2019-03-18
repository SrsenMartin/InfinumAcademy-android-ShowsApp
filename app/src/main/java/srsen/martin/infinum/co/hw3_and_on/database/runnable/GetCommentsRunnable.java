package srsen.martin.infinum.co.hw3_and_on.database.runnable;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.List;

import srsen.martin.infinum.co.hw3_and_on.database.DatabaseCallback;
import srsen.martin.infinum.co.hw3_and_on.database.factory.EpisodesDatabaseFactory;
import srsen.martin.infinum.co.hw3_and_on.models.Comment;

public class GetCommentsRunnable implements Runnable {

    private final DatabaseCallback<List<Comment>> callback;
    private final Context context;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final String episodeId;

    public GetCommentsRunnable(String episodeId, Context context, DatabaseCallback<List<Comment>> callback) {
        this.context = context;
        this.callback = callback;
        this.episodeId = episodeId;
    }

    @Override
    public void run() {
        try {
            final List<Comment> commentList = EpisodesDatabaseFactory.db(context).episodesDao().getEpisodeComments(episodeId);
            mainHandler.post(() -> callback.onSuccess(commentList));
        } catch (final Exception e) {
            mainHandler.post(() -> callback.onError(e));
        }
    }
}
