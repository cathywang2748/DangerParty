package com.kaplanteam.cathy.dangerparty;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    private Button bt_on;
    private Button bt_off;
    private Button bt_discoverable;
    private Button bt_list;
    private ListView bt_listView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        wireWidgets();
        setOnClickListeners();

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Device does not support Bluetooth :(", Toast.LENGTH_LONG).show();
            finish();
        }
//        if (!mBluetoothAdapter.isEnabled()) {
//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableBtIntent, REQUEST_ENABLE);
//        }
//        // Register for broadcasts when a device is discovered.
//        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        registerReceiver(mReceiver, filter);
//
//        boolean d = mBluetoothAdapter.startDiscovery();
//        Log.d("discovery starts", "" + d);

    }

    private void wireWidgets() {
        bt_on = findViewById(R.id.button_on);
        bt_off = findViewById(R.id.button_off);
        bt_discoverable = findViewById(R.id.button_discoverable);
        bt_list = findViewById(R.id.button_list);
        bt_listView = findViewById(R.id.listView);
    }

    private void setOnClickListeners(){
        bt_on.setOnClickListener(this);
        bt_off.setOnClickListener(this);
        bt_discoverable.setOnClickListener(this);
        bt_list.setOnClickListener(this);
    }

//    // Create a BroadcastReceiver for ACTION_FOUND.
//    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                // Discovery has found a device. Get the BluetoothDevice
//                // object and its info from the Intent.
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                String deviceName = device.getName();
//                String deviceHardwareAddress = device.getAddress(); // MAC address
//
//                Toast.makeText(context, "Device found: " + deviceName, Toast.LENGTH_SHORT).show();
//            }
//            else
//            {
//                Toast.makeText(context, "no device found", Toast.LENGTH_SHORT).show();
//            }
//        }
//    };



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
            case R.id.button_on: //turn on bluetooth
                Intent intent = new Intent(mBluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, REQUEST_ENABLE);
                Toast.makeText(this, "Bluetooth On", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_off: //turn off bluetooth
                mBluetoothAdapter.disable();
                Toast.makeText(this, "Bluetooth Off", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_discoverable: //make device discoverable
                if(!mBluetoothAdapter.isDiscovering()){
                    Intent intentDisc = new Intent(mBluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult(intentDisc, REQUEST_DISCOVERABLE);
                }
                break;
            case R.id.button_list: //list paired devices
                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                Toast.makeText(this, "" + pairedDevices.size(), Toast.LENGTH_SHORT).show();
                ArrayList<String> devices = new ArrayList<>();
                for(BluetoothDevice bt : pairedDevices){
                    devices.add(bt.getName());
                }
                ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, devices);
                bt_listView.setAdapter(adapter);
                break;
        }
    }
}
