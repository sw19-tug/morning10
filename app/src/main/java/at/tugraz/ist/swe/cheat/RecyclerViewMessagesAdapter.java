package at.tugraz.ist.swe.cheat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewMessagesAdapter extends RecyclerView.Adapter<RecyclerViewMessagesAdapter.ViewHolder> {

    private List<String> messageData; // to be replaced by List<Message>
    private LayoutInflater inflater;
    private ItemClickListener clickListener;

    // Constructor takes context and data
    RecyclerViewMessagesAdapter(Context context, List<String> data) {
        this.inflater = LayoutInflater.from(context);
        this.messageData = data;
    }

    // Inflate rv_message layout when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_message, parent, false);
        return new ViewHolder(view);
    }

    // Bind data to fields -> tvMessage (TextView field)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String message = messageData.get(position);
        holder.tvMessage.setText(message);
    }

    // Return total number of data sets
    @Override
    public int getItemCount() {
        return messageData.size();
    }


    // Store and recycle views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvMessage;

        ViewHolder(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_message);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // Get data at click position (for convenience)
    String getItem(int id) {
        return messageData.get(id);
    }

    // Allow click events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    // Interface for parent activity to respond to click events (inner function)
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
