package srsen.martin.infinum.co.hw3_and_on.database.runnable;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.List;

import srsen.martin.infinum.co.hw3_and_on.database.DatabaseCallback;
import srsen.martin.infinum.co.hw3_and_on.database.factory.EpisodesDatabaseFactory;
import srsen.martin.infinum.co.hw3_and_on.models.Episode;

public class GetEpisodesRunnable implements Runnable {

    private final DatabaseCallback<List<Episode>> callback;
    private final Context context;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final String showId;

    public GetEpisodesRunnable(String showId, Context context, DatabaseCallback<List<Episode>> callback) {
        this.context = context;
        this.callback = callback;
        this.showId = showId;
    }

    @Override
    public void run() {
        try {
            final List<Episode> episodesList = EpisodesDatabaseFactory.db(context).episodesDao().getAllEpisodes(showId);
            mainHandler.post(() -> callback.onSuccess(episodesList));
        } catch (final Exception e) {
            mainHandler.post(() -> callback.onError(e));
        }
    }
}
