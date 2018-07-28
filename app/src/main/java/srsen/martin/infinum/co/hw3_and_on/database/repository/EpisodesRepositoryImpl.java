package srsen.martin.infinum.co.hw3_and_on.database.repository;

import android.content.Context;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import srsen.martin.infinum.co.hw3_and_on.models.Episode;
import srsen.martin.infinum.co.hw3_and_on.database.DatabaseCallback;
import srsen.martin.infinum.co.hw3_and_on.database.runnable.GetEpisodesRunnable;
import srsen.martin.infinum.co.hw3_and_on.database.runnable.InsertEpisodesRunnable;

public class EpisodesRepositoryImpl implements EpisodesRepository {

    private final Context context;
    private final ExecutorService executor;
    private Future task;

    public EpisodesRepositoryImpl(Context context) {
        this.context = context;
        this.executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void getEpisodes(String showId, DatabaseCallback<List<Episode>> callback) {
        cancel();
        this.task = executor.submit(new GetEpisodesRunnable(showId, context, callback));
    }

    @Override
    public void insertEpisodes(List<Episode> episodesList, DatabaseCallback<Void> callback) {
        cancel();
        this.task = executor.submit(new InsertEpisodesRunnable(context, episodesList, callback));
    }

    private void cancel() {
        if (task != null && !task.isDone()) {
            task.cancel(true);
        }
    }
}
