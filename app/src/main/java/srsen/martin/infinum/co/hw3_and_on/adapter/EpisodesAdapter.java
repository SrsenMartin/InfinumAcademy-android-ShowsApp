package srsen.martin.infinum.co.hw3_and_on.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import srsen.martin.infinum.co.hw3_and_on.R;
import srsen.martin.infinum.co.hw3_and_on.models.Episode;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.ViewHolder> {

    private List<Episode> episodesList;
    private OnItemClickAction action;

    public EpisodesAdapter(List<Episode> episodesList, OnItemClickAction action){
        this.episodesList = episodesList;
        this.action = action;
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodesAdapter.ViewHolder holder, int position) {
        TextView seasonEpisodeString = holder.itemView.findViewById(R.id.seasonEpisodeString);
        TextView episodeTitle = holder.itemView.findViewById(R.id.episodeTitle);

        Episode episode = episodesList.get(position);
        seasonEpisodeString.setText(seasonEpisodeString(holder.itemView.getContext(), episode.getSeason(), episode.getEpisode()));
        episodeTitle.setText(episode.getTitle());

        holder.itemView.setOnClickListener(v -> action.onItemClicked(episode));
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

    private String seasonEpisodeString(Context context, String season, String episode){
        return String.format(context.getString(R.string.newEpisodeSeason), season, episode);
    }

    public void addEpisode(Episode episode){
        episodesList.add(episode);
        notifyItemChanged(episodesList.size() - 1);
    }

    public void setEpisodesList(List<Episode> episodesList){
        this.episodesList = episodesList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View view){
            super(view);
        }
    }

    public interface OnItemClickAction {
        void onItemClicked(Episode episode);
    }
}
