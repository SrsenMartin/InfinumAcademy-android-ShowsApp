package srsen.martin.infinum.co.hw3_and_on;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.ViewHolder> {

    private List<Episode> episodesList;

    public EpisodesAdapter(List<Episode> episodesList){
        this.episodesList = episodesList;
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodesAdapter.ViewHolder holder, int position) {
        TextView textView = holder.itemView.findViewById(R.id.showName);

        Episode episode = episodesList.get(position);
        textView.setText(episode.getName());
    }

    @NonNull
    @Override
    public EpisodesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_show, parent, false);
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
}
