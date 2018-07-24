package srsen.martin.infinum.co.hw3_and_on;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

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
        TextView textView = holder.itemView.findViewById(R.id.showName);
        ImageView imageView = holder.itemView.findViewById(R.id.showImage);

        Show show = showsList.get(position);
        textView.setText(show.getTitle());
        Uri imageUri = Uri.parse(Util.BASE_URL + show.getImageUrl().substring(1));

        Glide.with(holder.itemView).load(imageUri).into(imageView);
        textView.setOnClickListener(v -> action.onItemClicked(holder.itemView.getContext(), show));
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
