package srsen.martin.infinum.co.hw3_and_on;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ShowsAdapter extends RecyclerView.Adapter<ShowsAdapter.ViewHolder> {

    private List<Show> showsList;
    private OnItemClickAction action;

    public ShowsAdapter(List<Show> showsList, OnItemClickAction action){
        this.showsList = showsList;
        this.action = action;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView textView = holder.itemView.findViewById(R.id.showName);

        Show show = showsList.get(position);
        textView.setText(show.getName());
        textView.setOnClickListener(v -> action.onItemClicked(holder.itemView.getContext(), show));
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

    public interface OnItemClickAction {
        void onItemClicked(Context context, Show show);
    }
}
