package at.tugraz.ist.swe.cheat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ChatHistoryAdapter extends RecyclerView.Adapter<ChatHistoryAdapter.ViewHolder> {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private String currentDevice;

    private List<ChatMessage> messageData;
    private ItemClickListener clickListener;

    private RecyclerView.LayoutManager manager;

    // Constructor takes context and data
    ChatHistoryAdapter(List<ChatMessage> data, String device, RecyclerView.LayoutManager lm) {
        this.messageData = new ArrayList<>();
        this.currentDevice = device;
        this.manager = lm;
    }

    // Determine the appropriate ViewType according to the sender
    @Override
    public int getItemViewType(int position) {
        ChatMessage message = messageData.get(position);

        if (message.getSenderAddress().equals(this.currentDevice)) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    // Inflate the appropriate layout when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        switch (viewType)
        {
            case VIEW_TYPE_MESSAGE_SENT:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.rv_message_sent, parent, false);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.rv_message_recieved, parent, false);
                break;
            default:
                return null;
        }

        return new ViewHolder(view);
    }

    // Bind data to fields -> tvMessage (TextView field)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChatMessage message = messageData.get(position);
        String message_text = message.getMessage();
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");
        String message_timestamp = formatter.format(message.getTimeStamp());
        holder.tv_message.setText(message_text);
        holder.tv_timestamp.setText(message_timestamp);
    }

    // Return total number of data sets
    @Override
    public int getItemCount() {
        return messageData.size();
    }

    // Store and recycle views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_message;
        TextView tv_timestamp;

        ViewHolder(View itemView) {
            super(itemView);
            tv_message = itemView.findViewById(R.id.tv_message);
            tv_timestamp = itemView.findViewById(R.id.tv_timestamp);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // Get data at click position (for convenience)
    String getItem(int id) {
        return messageData.get(id).getMessage();
    }

    // Allow click events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    // Interface for parent activity to respond to click events (inner function)
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    // Add message to recycler view
    public void addMessage(ChatMessage message)
    {
        messageData.add(message);
        notifyItemInserted(messageData.size());
        manager.scrollToPosition(getItemCount() - 1);
    }
}