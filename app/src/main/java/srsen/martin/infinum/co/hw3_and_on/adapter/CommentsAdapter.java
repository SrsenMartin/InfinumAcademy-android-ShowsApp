package srsen.martin.infinum.co.hw3_and_on.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import srsen.martin.infinum.co.hw3_and_on.R;
import srsen.martin.infinum.co.hw3_and_on.models.Comment;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private List<Comment> commentList;

    public CommentsAdapter(List<Comment> commentList){
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView emailText = holder.itemView.findViewById(R.id.emailText);
        TextView commentText = holder.itemView.findViewById(R.id.commentText);

        Comment comment = commentList.get(position);
        emailText.setText(comment.getEmail());
        commentText.setText(comment.getText());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public void setCommentsList(List<Comment> commentsList){
        this.commentList = commentsList;
        notifyDataSetChanged();
    }

    public void addComment(Comment comment){
        commentList.add(comment);
        notifyItemChanged(commentList.size() - 1);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View view){
            super(view);
        }
    }
}
