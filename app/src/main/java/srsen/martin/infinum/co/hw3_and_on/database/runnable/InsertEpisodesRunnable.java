package srsen.martin.infinum.co.hw3_and_on.database.runnable;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.List;

import srsen.martin.infinum.co.hw3_and_on.database.DatabaseCallback;
import srsen.martin.infinum.co.hw3_and_on.database.factory.EpisodesDatabaseFactory;
import srsen.martin.infinum.co.hw3_and_on.models.Episode;

public class InsertEpisodesRunnable implements Runnable {

    private final DatabaseCallback<Void> callback;
    private final Context context;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final List<Episode> episodeList;

    public InsertEpisodesRunnable(Context context, List<Episode> episodeList, DatabaseCallback<Void> callback) {
        this.context = context;
        this.episodeList = episodeList;
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            EpisodesDatabaseFactory.db(context).episodesDao().insertEpisodes(episodeList);
            mainHandler.post(() -> callback.onSuccess(null));
        } catch (final Exception e) {
            mainHandler.post(() -> callback.onError(e));
        }
    }
}
