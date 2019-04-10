package at.tugraz.ist.swe.cheat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
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

import at.tugraz.ist.swe.cheat.serviceimpl.DummyBluetoothDeviceProvider;
import at.tugraz.ist.swe.cheat.serviceimpl.RealBluetoothDeviceProvider;

public class MainActivity extends AppCompatActivity implements RecyclerViewMessagesAdapter.ItemClickListener {

    BluetoothDeviceManager bluetoothDeviceManager;
    AlertDialog.Builder devicesDialogBuilder;
    AlertDialog devicesDialog;

    ArrayAdapter<String> deviceListAdapter;

    RecyclerViewMessagesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.FINGERPRINT.contains("generic")) {
            bluetoothDeviceManager = new BluetoothDeviceManager(new DummyBluetoothDeviceProvider());
        } else {
            bluetoothDeviceManager = new BluetoothDeviceManager(new RealBluetoothDeviceProvider());
        }

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

        // Dummy dataset for messages
        ArrayList<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage(1, "00-00-00-00-00-00", "Bonjour", new Date() ));
        messages.add(new ChatMessage(2, "00-00-00-00-00-00", "Hola", new Date()));
        messages.add(new ChatMessage(3, "00-00-00-00-00-00", "Nǐn hǎo", new Date() ));
        messages.add(new ChatMessage(4, "00-00-00-00-00-00", "Anyoung haseyo", new Date() ));

        // Set up the RecyclerView to display messages (history)
        RecyclerView recyclerView = findViewById(R.id.rv_messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewMessagesAdapter(this, messages);
        adapter.setClickListener(this);
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
        final EditText tfInput = findViewById(R.id.tf_input);
        tfInput.setText(adapter.getItem(position));
    }
}
