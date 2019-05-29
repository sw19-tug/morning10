package at.tugraz.ist.swe.cheat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements ChatHistoryAdapter.ItemClickListener {

    //private BluetoothDeviceManager bluetoothDeviceManager;
    AlertDialog.Builder devicesDialogBuilder;
    AlertDialog devicesDialog;

    ArrayAdapter<String> deviceListAdapter;

    ChatHistoryAdapter adapter;

    boolean messageColor = true;
    final int RESULT_IMAGE_SELECTED = 42;

    private static final int LOCATION_PERMISSION_RESPONSE = 2;
    private static final int READ_PERMISSION_RESPONSE = 3;

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
                myToolbar.setBackgroundColor(0xff66bb6a);
                dialog.dismiss();
            }
        });

        // Set up the RecyclerView to display messages (history)
        ArrayList<ChatMessage> messages = null;
        final RecyclerView recyclerView = findViewById(R.id.rv_chat_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatHistoryAdapter(messages, "00:00:00:00:00:00");
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
}
