package srsen.martin.infinum.co.hw3_and_on.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import srsen.martin.infinum.co.hw3_and_on.Provider;
import srsen.martin.infinum.co.hw3_and_on.R;
import srsen.martin.infinum.co.hw3_and_on.Util;
import srsen.martin.infinum.co.hw3_and_on.adapter.CommentsAdapter;
import srsen.martin.infinum.co.hw3_and_on.database.DatabaseCallback;
import srsen.martin.infinum.co.hw3_and_on.models.Comment;
import srsen.martin.infinum.co.hw3_and_on.models.Data;

public class CommentsActivity extends AppCompatActivity {

    public static final String EXTRA_EPISODE_ID = "extra_episode_id";

    @BindView(R.id.commentsToolbar)
    Toolbar toolbar;

    @BindView(R.id.noCommentsPlaceholder)
    ViewGroup noCommentsPlaceholder;

    @BindView(R.id.commentEditText)
    EditText commentEditText;

    @BindView(R.id.commentsRecyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    private String episodeId;
    private CommentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ButterKnife.bind(this);

        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        episodeId = getEpisodeId();
        initRecycler();

        loadComments();
        setOnRefreshListener();
    }

    private void loadComments(){
        if(Util.isInternetAvailable(this)){
            loadCommentsFromInternet();
        }else {
            loadCommentsFromDatabase();
        }
    }

    private void loadCommentsFromDatabase(){
        Provider.getEpisodesRepository(this).getEpisodeComments(episodeId, new DatabaseCallback<List<Comment>>() {
            @Override
            public void onSuccess(List<Comment> data) {
                if (data.isEmpty()) {
                    Toast.makeText(CommentsActivity.this, R.string.database_empty, Toast.LENGTH_SHORT).show();
                } else {
                    setComments(data);
                }
            }

            @Override
            public void onError(Throwable t) {
                Util.showError(CommentsActivity.this, getString(R.string.comments));
            }
        });
    }

    private void insertCommentsDatabase(List<Comment> commentList){
        Provider.getEpisodesRepository(this).insertComments(commentList, new DatabaseCallback<Void>() {
            @Override
            public void onSuccess(Void data) {}

            @Override
            public void onError(Throwable t) {
                Util.showError(CommentsActivity.this, getString(R.string.comments));
            }
        });
    }

    private void loadCommentsFromInternet(){
        Util.showProgress(this, getString(R.string.comments), getString(R.string.loading), true, false);

        Provider.getApiService().getComments(episodeId).enqueue(new Callback<Data<List<Comment>>>() {
            @Override
            public void onResponse(Call<Data<List<Comment>>> call, Response<Data<List<Comment>>> response) {
                Util.hideProgress();

                if(response.isSuccessful()){
                    List<Comment> commentList = response.body().getData();
                    setComments(commentList);
                    insertCommentsDatabase(commentList);
                }else{
                    Toast.makeText(CommentsActivity.this, R.string.unable_comments, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Data<List<Comment>>> call, Throwable t) {
                Util.hideProgress();
                Util.showError(CommentsActivity.this, getString(R.string.comments));
            }
        });
    }

    private void setComments(List<Comment> commentList){
        adapter.setCommentsList(commentList);
        checkEmptyPlaceholder();
    }

    private void checkEmptyPlaceholder(){
        if(adapter == null || adapter.getItemCount() == 0){
            noCommentsPlaceholder.setVisibility(View.VISIBLE);
        }else{
            noCommentsPlaceholder.setVisibility(View.INVISIBLE);
        }
    }

    private void initRecycler(){
        if(adapter == null) initAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void initAdapter(){
        adapter = new CommentsAdapter(new ArrayList<>());
    }

    private String getEpisodeId(){
        checkExtras();

        episodeId = getIntent().getStringExtra(EXTRA_EPISODE_ID);
        return episodeId;
    }

    @OnClick(R.id.postButton)
    public void postComment(){
        if(!Util.isInternetAvailable(this)){
            Toast.makeText(this, getString(R.string.connection_comment_post), Toast.LENGTH_LONG).show();
            return;
        }

        String commentText = commentEditText.getText().toString();

        if(commentText.isEmpty()){
            Toast.makeText(this, getString(R.string.invalid_comment), Toast.LENGTH_SHORT).show();
            return;
        }

        Comment comment = new Comment();
        comment.setText(commentText);
        comment.setEpisodeId(episodeId);

        postCommentInternet(comment);
    }

    private void postCommentInternet(Comment comment){
        Util.showProgress(this, getString(R.string.comments), getString(R.string.loading), true, false);

        Provider.getApiService().postComment(Provider.getToken(this), comment).enqueue(new Callback<Data<Comment>>() {
            @Override
            public void onResponse(Call<Data<Comment>> call, Response<Data<Comment>> response) {
                Util.hideProgress();

                if(response.isSuccessful()){
                    Comment comment = response.body().getData();
                    addComment(comment);
                    commentEditText.setText("");

                    Toast.makeText(CommentsActivity.this, getString(R.string.comment_posted), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(CommentsActivity.this, R.string.unable_post_comment, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Data<Comment>> call, Throwable t) {
                Util.hideProgress();
                Util.showError(CommentsActivity.this, getString(R.string.comments));
            }
        });
    }

    private void addComment(Comment comment){
        if(adapter == null) initAdapter();

        adapter.addComment(comment);
        checkEmptyPlaceholder();

        List<Comment> commentCont = new ArrayList<>();
        commentCont.add(comment);
        insertCommentsDatabase(commentCont);
    }

    private void checkExtras() {
        if(getIntent() == null || getIntent().getStringExtra(EXTRA_EPISODE_ID) == null){
            Toast.makeText(this, getString(R.string.id_extra_episode), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setOnRefreshListener(){
        swipeRefresh.setOnRefreshListener(() -> {
            swipeRefresh.setRefreshing(true);

            if(!Util.isInternetAvailable(CommentsActivity.this)){
                Toast.makeText(CommentsActivity.this, R.string.no_connection, Toast.LENGTH_SHORT).show();
            }else{
                loadCommentsFromInternet();
            }

            swipeRefresh.setRefreshing(false);
        });
    }

    public static Intent newIntentInstance(Context context, String episodeId){
        Intent intent = new Intent(context, CommentsActivity.class);
        intent.putExtra(EXTRA_EPISODE_ID, episodeId);

        return intent;
    }
}
