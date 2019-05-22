package at.tugraz.ist.swe.cheat;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import java.util.Observable;
import java.util.Observer;

import at.tugraz.ist.swe.cheat.btobservable.DeviceObservable;
import at.tugraz.ist.swe.cheat.serviceimpl.DummyBluetoothDeviceProvider;
import at.tugraz.ist.swe.cheat.serviceimpl.RealBluetoothDeviceProvider;
import at.tugraz.ist.swe.cheat.viewfragments.DeviceListFragment;
import at.tugraz.ist.swe.cheat.viewfragments.ToastFragment;

public class MainActivity extends AppCompatActivity implements RecyclerViewMessagesAdapter.ItemClickListener {

    BluetoothDeviceManager bluetoothDeviceManager;
    AlertDialog.Builder devicesDialogBuilder;
    AlertDialog devicesDialog;

    ArrayAdapter<String> deviceListAdapter;

    RecyclerViewMessagesAdapter adapter;
    DeviceObservable deviceObservable = new DeviceObservable();
    BluetoothDiscover bluetoothDiscover = new BluetoothDiscover(deviceObservable);
    ToastFragment toastFragment = new ToastFragment();


    public static final int REQUEST_ENABLE_BLUETOOTH = 1;
    private static final int MY_PERMISSION_RESPONSE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //We have to add this
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSION_RESPONSE);
            }
        }

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
                    bluetoothDeviceManager.stopScanning();
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
                String strName = deviceListAdapter.getItem(which);
                if(strName != null)
                {
                    bluetoothDeviceManager.stopScanning();
                    String info = strName;
                    String address = info.substring(info.length() - 17);

                    bluetoothDeviceManager.connectToDevice(address);
                    Log.d("#######","Show name "+ strName);
                    Log.d("#######","Show address "+ address);
                }

                myToolbar.setBackgroundColor(0xff66bb6a);
                bluetoothDeviceManager.stopScanning();
                unregisterReceiver(bluetoothDiscover);

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

        toastFragment.setMainActivity(this);

        bluetoothDeviceManager.getBluetoothDeviceProvider().addObserver(toastFragment);
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
                    //bluetoothDeviceManager.startScanning();
                    //TODO STOP Connection
                    myToolbar.setBackgroundColor(0xffffffff);
                    btConnect.setIcon(R.drawable.ic_portable_wifi_off_black_24dp);
                } else {
                    bluetoothDeviceManager.startScanning();

                    //TODO start scanning
                    DeviceListFragment deviceListFragment = new DeviceListFragment(deviceListAdapter);
                    deviceObservable.addObserver(deviceListFragment);


                    // Register for broadcasts when a device is discovered
                    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    registerReceiver(bluetoothDiscover, filter);

                    // Register for broadcasts when discovery has finished
                    filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                    registerReceiver(bluetoothDiscover, filter);


                    //Show Devices
                    //listen to observer to
                    //deviceListAdapter.add("TEST");

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


    @Override
    public void onStart() {
        super.onStart();
        if (!bluetoothDeviceManager.isOn()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);
            Intent disoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            disoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
            startActivity(disoverableIntent);

        } else {
            Intent disoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            disoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
            startActivity(disoverableIntent);
            //TODO Start Chat Controller
        }
    }


}
