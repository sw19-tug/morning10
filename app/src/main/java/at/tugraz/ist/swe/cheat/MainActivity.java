package at.tugraz.ist.swe.cheat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements ChatHistoryAdapter.ItemClickListener {

    //private BluetoothDeviceManager bluetoothDeviceManager;
    AlertDialog.Builder devicesDialogBuilder;
    AlertDialog devicesDialog;
    ArrayAdapter<String> deviceListAdapter;
    ChatHistoryAdapter adapter;

    boolean messageColor = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        devicesDialogBuilder = new AlertDialog.Builder(this)
            .setTitle("Choose your cheating partner")
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            })
            .setNegativeButton(android.R.string.no, null);

        devicesDialogBuilder.create();

        deviceListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        final Toolbar myToolbar = (Toolbar)findViewById(R.id.menu);

        setSupportActionBar(myToolbar);

        devicesDialogBuilder.setAdapter(deviceListAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myToolbar.setBackgroundColor(0xff66bb6a);
                dialog.dismiss();
            }
        });

        // Set up the RecyclerView to display messages (history)
        ArrayList<ChatMessage> messages = null;
        final RecyclerView recyclerView = findViewById(R.id.rv_chat_history);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ChatHistoryAdapter(messages, "00:00:00:00:00:00", layoutManager);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setAdapter(adapter);

        // Initialize tfInput and btSend
        final EditText tfInput = findViewById(R.id.tf_input);
        final Button btSend = findViewById(R.id.bt_send);
        btSend.setEnabled(false);

        // TextChangedListener tfInput
        tfInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                btSend.setEnabled(s.toString().isEmpty() ? false : true);
            }
        });

        // OnClickListener sendButton
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address;
                if (messageColor) {
                    messageColor = false;
                    address = "00:00:00:00:00:00";
                }
                else {
                    messageColor = true;
                    address = "11:00:00:00:00:00";
                }

                adapter.addMessage(new ChatMessage(1, address, tfInput.getText().toString(), new Date()));
                tfInput.setText("");
            }
        });
    }
  
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final Toolbar myToolbar = (Toolbar)findViewById(R.id.menu);
        final MenuItem btConnect =  (MenuItem)myToolbar.getMenu().findItem(R.id.bt_connect);

        switch (item.getItemId()) {
            case R.id.bt_connect:
                if(((ColorDrawable)myToolbar.getBackground()).getColor() == 0xff66bb6a) {
                    myToolbar.setBackgroundColor(0xffffffff);
                    btConnect.setIcon(R.drawable.ic_portable_wifi_off_black_24dp);
                } else {
                    deviceListAdapter.clear();
                    deviceListAdapter.add("Davids iPhone");
                    deviceListAdapter.add("Stefans iPhone");
                    deviceListAdapter.add("Matzes GalaxyS7Edge");
                    deviceListAdapter.add("Patricks iPhone");
                    deviceListAdapter.add("Oskars iPhone");

                    devicesDialog = devicesDialogBuilder.show();
                    btConnect.setIcon(R.drawable.ic_wifi_tethering_black_24dp);
                }
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
  
    @Override
    public void onItemClick(View view, int position) {
        view.findViewById(R.id.tv_message).setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_orange));
        view.findViewById(R.id.tv_message).setTag(R.drawable.rounded_rectangle_orange);

    }
}
