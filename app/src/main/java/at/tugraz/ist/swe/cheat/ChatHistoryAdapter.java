package at.tugraz.ist.swe.cheat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import at.tugraz.ist.swe.cheat.dto.CustomMessage;
import at.tugraz.ist.swe.cheat.dto.Provider;

import static at.tugraz.ist.swe.cheat.dto.Provider.STATE_CONNECTED;
import static at.tugraz.ist.swe.cheat.dto.Provider.STATE_CONNECTING;
import static at.tugraz.ist.swe.cheat.dto.Provider.STATE_LISTEN;


//TODO ADD implements Observer
public class ChatHistoryAdapter extends RecyclerView.Adapter<ChatHistoryAdapter.ViewHolder> implements Observer {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private String currentDevice;

    private List<ChatMessage> messageData;
    private ItemClickListener clickListener;
    private ItemLongClickListener longClickListener;

    private RecyclerView.LayoutManager manager;
    private CustomMessage message;

    private Provider bluetoothDeviceProvider;

    private MainActivity mainActivity;
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
        if(message_text.isEmpty()) {
            holder.tv_message_img.setImageBitmap(message.getImage());
            holder.tv_message_img.setVisibility(View.VISIBLE);
            holder.tv_message.setVisibility(View.GONE);
        }
        else {
            holder.tv_message_img.setVisibility(View.GONE);
            holder.tv_message.setVisibility(View.VISIBLE);
            holder.tv_message.setText(message_text);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");
        String message_timestamp = formatter.format(message.getTimeStamp());
        holder.tv_timestamp.setText(message_timestamp);
    }

    // Return total number of data sets
    @Override
    public int getItemCount() {
        return messageData.size();
    }


    //TODO ADD THIS
    //
    @Override
    public void update(Observable o, final Object message) {

        this.message = (CustomMessage) message;


        if(((CustomMessage) message).getDevice() != null)
        {
            mainActivity.runOnUiThread(new Runnable()
            {
                public void run() {
                    if(((CustomMessage) message).getMessage() != null)
                        addMessageFromPartner(((CustomMessage) message).getMessage());
                }
            });
        }


    }

    private void addMessageFromPartner(ChatMessage message) {

        message.setAddress("partner");
        messageData.add(message);
        //notifyItemInserted(messageData.size());
        manager.scrollToPosition(getItemCount() - 1);

    }

    // Store and recycle views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView tv_message;
        ImageView tv_message_img;
        TextView tv_timestamp;

        ViewHolder(View itemView) {
            super(itemView);
            tv_message = itemView.findViewById(R.id.tv_message);
            tv_message_img = itemView.findViewById(R.id.tv_message_img);
            tv_timestamp = itemView.findViewById(R.id.tv_timestamp);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        // Handle simple click
        @Override
        public void onClick(View view) {
            System.out.println("# Message " + getAdapterPosition() + " pressed normally.");
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }

        // Handle long click
        @Override
        public boolean onLongClick(View view) {
            System.out.println("# Message " + getAdapterPosition() + " pressed long.");
            if (longClickListener != null) longClickListener.onItemLongClick(view, getAdapterPosition());
            // Return true to indicate the click was handled
            return true;
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

    // Allow long click events to be caught
    void setLongClickListener(ItemLongClickListener itemLongClickListener) {
        this.longClickListener = itemLongClickListener;
    }

    // Interface for parent activity to respond to click events (inner function)
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface ItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    // Add message to recycler view
    public void addMessage(ChatMessage message)
    {
        messageData.add(message);

        try {
            bluetoothDeviceProvider.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //notifyItemInserted(messageData.size());
        manager.scrollToPosition(getItemCount() - 1);
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public Provider getBluetoothDeviceProvider() {
        return bluetoothDeviceProvider;
    }

    public void setBluetoothDeviceProvider(Provider bluetoothDeviceProvider) {
        this.bluetoothDeviceProvider = bluetoothDeviceProvider;
    }

    public String getCurrentDevice() {
        return currentDevice;
    }

    public void setCurrentDevice(String currentDevice) {
        System.out.println("Current Device " + currentDevice);
        this.currentDevice = currentDevice;
    }

    // Delete message from recycler view
    public void deleteMessage(int id) {
        messageData.remove(messageData.get(id));
        notifyItemRemoved(messageData.size());
    }


}