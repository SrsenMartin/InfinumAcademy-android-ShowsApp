package srsen.martin.infinum.co.hw3_and_on;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ShowsAdapter extends RecyclerView.Adapter<ShowsAdapter.ViewHolder> {

    private List<Show> showsList;

    public ShowsAdapter(List<Show> showsList){
        this.showsList = showsList;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView textView = holder.itemView.findViewById(R.id.showName);

        String showName = showsList.get(position).getName();
        textView.setText(showName);
        textView.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();

            Intent intent = EpisodesActivity.newIntentInstance(context, showName);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_show, parent, false);
        return new ViewHolder(view);
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
}
