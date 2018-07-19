package srsen.martin.infinum.co.hw3_and_on;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.ViewHolder> {

    private List<Episode> episodesList;
    private OnItemClickAction action;

    public EpisodesAdapter(List<Episode> episodesList, OnItemClickAction action){
        this.episodesList = episodesList;
        this.action = action;
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodesAdapter.ViewHolder holder, int position) {
        TextView textView = holder.itemView.findViewById(R.id.episodeName);
        ImageView episodeImage = holder.itemView.findViewById(R.id.episodeImage);

        Episode episode = episodesList.get(position);
        textView.setText(episode.getName());
        Glide.with(holder.itemView).load(episode.getImageUri()).into(episodeImage);

        holder.itemView.setOnClickListener(v -> action.onItemClicked(holder.itemView.getContext(), episode));
    }

    @NonNull
    @Override
    public EpisodesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_episode, parent, false);
        return new EpisodesAdapter.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return episodesList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View view){
            super(view);
        }
    }

    public interface OnItemClickAction {
        void onItemClicked(Context context, Episode episode);
    }
}
