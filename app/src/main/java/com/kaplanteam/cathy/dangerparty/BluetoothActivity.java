package com.kaplanteam.cathy.dangerparty;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by per6 on 5/7/18.
 */

public class BluetoothActivity extends AppCompatActivity implements View.OnClickListener{
    private BluetoothAdapter mBluetoothAdapter;

    private static final int REQUEST_ENABLE = 0;
    private static final int REQUEST_DISCOVERABLE = 1;

//    private static final int STATE_LISTENING = 1;
//    private static final int STATE_CONNECTING = 2;
//    private static final int STATE_CONNECTED = 3;
//    private static final int STATE_CONNECTION_FAILED = 4;
//    private static final int STATE_MESSAGE_RECIEVED = 5;
//
//    int REQUEST_ENABLE_BLUETOOTH = 1;

    private Button host;
    private Button join;
    private ListView bt_listView;
    private ArrayAdapter adapter;
    private ArrayList<String> deviceNames;
    private Set<BluetoothDevice> pairedDevices;

    //UUID uuid = UUID.fromString("e141d643-1f16-443f-9da7-c5fbc7081397");


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Device does not support Bluetooth :(", Toast.LENGTH_LONG).show();
            finish();
        }

        wireWidgets();
        setOnClickListeners();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);

        deviceNames = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, deviceNames);
        bt_listView.setAdapter(adapter);
    }

    private void wireWidgets() {
        host = findViewById(R.id.button_host);
        join = findViewById(R.id.button_join);
        bt_listView = findViewById(R.id.listView);
    }

    private void setOnClickListeners(){
        host.setOnClickListener(this);
        join.setOnClickListener(this);
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) { // Discovery has found a device. Get the BluetoothDevice object and its info from the Intent.

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                pairedDevices = mBluetoothAdapter.getBondedDevices();

                Log.d("DEVICES", "" + pairedDevices); //String deviceHardwareAddress = device.getAddress(); // MAC address

                if(device.getName() != null){
                    deviceNames.add(device.getName());
                }
                else{
                    deviceNames.add("Unknown");
                }

                adapter.notifyDataSetChanged();
            }
        }
    };

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        // Don't forget to unregister the ACTION_FOUND receiver.
//        unregisterReceiver(mReceiver);
//    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_host: //make device discoverable
                if(!mBluetoothAdapter.isDiscovering()){
                    Intent intentDisc = new Intent(mBluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult(intentDisc, REQUEST_DISCOVERABLE);
                }
                break;
            case R.id.button_join: //list paired devices
                mBluetoothAdapter.startDiscovery();
                break;
        }
    }
}
