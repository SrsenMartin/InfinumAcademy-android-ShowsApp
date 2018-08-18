package srsen.martin.infinum.co.hw3_and_on.database.repository;

import android.content.Context;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import srsen.martin.infinum.co.hw3_and_on.database.DatabaseCallback;
import srsen.martin.infinum.co.hw3_and_on.database.runnable.GetCommentsRunnable;
import srsen.martin.infinum.co.hw3_and_on.database.runnable.GetEpisodesRunnable;
import srsen.martin.infinum.co.hw3_and_on.database.runnable.InsertCommentsRunnable;
import srsen.martin.infinum.co.hw3_and_on.database.runnable.InsertEpisodesRunnable;
import srsen.martin.infinum.co.hw3_and_on.models.Comment;
import srsen.martin.infinum.co.hw3_and_on.models.Episode;

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

    @Override
    public void getEpisodeComments(String episodeId, DatabaseCallback<List<Comment>> callback) {
        cancel();
        this.task = executor.submit(new GetCommentsRunnable(episodeId, context, callback));
    }

    @Override
    public void insertComments(List<Comment> comments, DatabaseCallback<Void> callback) {
        cancel();
        this.task = executor.submit(new InsertCommentsRunnable(context, comments, callback));
    }

    private void cancel() {
        if (task != null && !task.isDone()) {
            task.cancel(true);
        }
    }
}
