package at.tugraz.ist.swe.cheat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.content.IntentFilter;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;


import at.tugraz.ist.swe.cheat.btobservable.DeviceObservable;
import at.tugraz.ist.swe.cheat.dto.Provider;
import at.tugraz.ist.swe.cheat.serviceimpl.DummyBluetoothDeviceProvider;
import at.tugraz.ist.swe.cheat.serviceimpl.RealBluetoothDeviceProvider;
import at.tugraz.ist.swe.cheat.viewfragments.DeviceListFragment;
import at.tugraz.ist.swe.cheat.viewfragments.ToastFragment;

public class MainActivity extends AppCompatActivity implements ChatHistoryAdapter.ItemClickListener {


    BluetoothDeviceManager bluetoothDeviceManager;
    AlertDialog.Builder devicesDialogBuilder;
    AlertDialog devicesDialog;
    ArrayAdapter<String> deviceListAdapter;
    ChatHistoryAdapter adapter;

    DeviceObservable deviceObservable = new DeviceObservable();
    BluetoothDiscover bluetoothDiscover = new BluetoothDiscover(deviceObservable);
    ToastFragment toastFragment = new ToastFragment();

    ChatController chatController;


    public static final int REQUEST_ENABLE_BLUETOOTH = 1;
    private static final int MY_PERMISSION_RESPONSE = 2;
    boolean messageColor = true;
    final int RESULT_IMAGE_SELECTED = 42;

    private static final int LOCATION_PERMISSION_RESPONSE = 2;
    private static final int READ_PERMISSION_RESPONSE = 3;

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

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        LOCATION_PERMISSION_RESPONSE);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        READ_PERMISSION_RESPONSE);
            }
        }

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

        final Button btSendImage = findViewById(R.id.bt_sendImage);
        btSendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.setType("image/*");
                startActivityForResult(i, RESULT_IMAGE_SELECTED);
            }
        });
        toastFragment.setMainActivity(this);

        bluetoothDeviceManager.getBluetoothDeviceProvider().addObserver(toastFragment);
        chatController = new ChatController(bluetoothDeviceManager.getBluetoothDeviceProvider());
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

        Log.d("#######","Show Current State "+ bluetoothDeviceManager.getBluetoothDeviceProvider().getCurrentState());

        switch (item.getItemId()) {

            case R.id.bt_connect:
                //TODO
                if(bluetoothDeviceManager.getBluetoothDeviceProvider().getCurrentState() == Provider.STATE_CONNECTED || bluetoothDeviceManager.getBluetoothDeviceProvider().getCurrentState() == Provider.STATE_CONNECTING) {
                    //bluetoothDeviceManager.startScanning();
                    //TODO STOP Connection
                    try {
                        bluetoothDeviceManager.getBluetoothDeviceProvider().disconnected();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    myToolbar.setBackgroundColor(0xffffffff);
                    btConnect.setIcon(R.drawable.ic_portable_wifi_off_black_24dp);
                } else if(bluetoothDeviceManager.getBluetoothDeviceProvider().getCurrentState() == Provider.STATE_LISTEN ) {
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
        if(view.findViewById(R.id.tv_message).getBackground().getConstantState() == getResources().getDrawable(R.drawable.rounded_rectangle_orange).getConstantState())
        {
            if(view.findViewById(R.id.rv_message_sent) == null)
            {
                view.findViewById(R.id.tv_message).setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_white));
                view.findViewById(R.id.tv_message).setTag(R.drawable.rounded_rectangle_white);
            }
            else
            {
                view.findViewById(R.id.tv_message).setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_blue));
                view.findViewById(R.id.tv_message).setTag(R.drawable.rounded_rectangle_blue);
            }
        }
        else{
            view.findViewById(R.id.tv_message).setBackground(getResources().getDrawable(R.drawable.rounded_rectangle_orange));
            view.findViewById(R.id.tv_message).setTag(R.drawable.rounded_rectangle_orange);
        }

    }

    public void onActivityResult(int requestCode,int resultCode,Intent data){

        if (resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case RESULT_IMAGE_SELECTED:
                    Uri selectedImage = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        String address;
                        if (messageColor) {
                            messageColor = false;
                            address = "00:00:00:00:00:00";
                        }
                        else {
                            messageColor = true;
                            address = "11:00:00:00:00:00";
                        }
                        adapter.addMessage(new ChatMessage(1, address, bitmap, new Date()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }

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
