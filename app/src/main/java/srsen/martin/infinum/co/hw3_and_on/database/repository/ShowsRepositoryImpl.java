package srsen.martin.infinum.co.hw3_and_on.database.repository;

import android.content.Context;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import srsen.martin.infinum.co.hw3_and_on.models.Show;
import srsen.martin.infinum.co.hw3_and_on.database.DatabaseCallback;
import srsen.martin.infinum.co.hw3_and_on.database.runnable.GetShowRunnable;
import srsen.martin.infinum.co.hw3_and_on.database.runnable.GetShowsRunnable;
import srsen.martin.infinum.co.hw3_and_on.database.runnable.InsertShowRunnable;
import srsen.martin.infinum.co.hw3_and_on.database.runnable.InsertShowsRunnable;

public class ShowsRepositoryImpl implements ShowsRepository {

    private final Context context;
    private final ExecutorService executor;
    private Future task;

    public ShowsRepositoryImpl(Context context) {
        this.context = context;
        this.executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void getShows(DatabaseCallback<List<Show>> callback) {
        cancel();
        this.task = executor.submit(new GetShowsRunnable(context, callback));
    }

    @Override
    public void insertShows(List<Show> showsList, DatabaseCallback<Void> callback) {
        cancel();
        this.task = executor.submit(new InsertShowsRunnable(context, showsList, callback));
    }

    @Override
    public void getShow(String showId, DatabaseCallback<Show> callback){
        cancel();
        this.task = executor.submit(new GetShowRunnable(context, callback, showId));
    }

    @Override
    public void insertShow(Show show, DatabaseCallback<Void> callback){
        cancel();
        this.task = executor.submit(new InsertShowRunnable(context, show, callback));
    }

    private void cancel() {
        if (task != null && !task.isDone()) {
            task.cancel(true);
        }
    }
}
