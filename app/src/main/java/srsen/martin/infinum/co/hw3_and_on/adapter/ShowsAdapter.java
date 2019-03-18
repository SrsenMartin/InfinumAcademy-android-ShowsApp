package srsen.martin.infinum.co.hw3_and_on.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import srsen.martin.infinum.co.hw3_and_on.R;
import srsen.martin.infinum.co.hw3_and_on.Util;
import srsen.martin.infinum.co.hw3_and_on.models.Show;

public class ShowsAdapter extends RecyclerView.Adapter<ShowsAdapter.ViewHolder> {

    private List<Show> showsList;
    private OnItemClickAction action;

    private int chosenLayout;

    public ShowsAdapter(List<Show> showsList, OnItemClickAction action, int chosenLayout){
        this.showsList = showsList;
        this.action = action;
        this.chosenLayout = chosenLayout;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        View view = holder.itemView;

        TextView textView = view.findViewById(R.id.showName);
        ImageView imageView = view.findViewById(R.id.showImage);

        Show show = showsList.get(position);
        textView.setText(show.getTitle());
        Uri imageUri = Util.getImageUri(show.getImageUrl());

        Util.setImage(view.getContext(), imageUri, imageView, view.findViewById(R.id.emptyPlaceholder));

        holder.itemView.setOnClickListener(v -> action.onItemClicked(show.getID(), imageView));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(chosenLayout, parent, false);
        return new ViewHolder(view);
    }

    public void setShows(List<Show> showsList){
        this.showsList = showsList;
        notifyDataSetChanged();
    }

    public void setLayout(int layoutId){
        this.chosenLayout = layoutId;
    }

    @Override
    public int getItemCount() {
        return showsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View view){
            super(view);
        }
    }

    public interface OnItemClickAction {
        void onItemClicked(String showID, View sharedView);
    }
}
