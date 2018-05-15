package com.kaplanteam.cathy.dangerparty;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

/**
 * Created by per6 on 5/7/18.
 */

public class BluetoothActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener {
    private BluetoothAdapter mBluetoothAdapter;

    private static final int REQUEST_ENABLE = 0;
    private static final int REQUEST_DISCOVERABLE = 10;
    private static final String APP_NAME = "DangerParty";
    private static final UUID MY_UUID = UUID.fromString("e141d643-1f16-443f-9da7-c5fbc7081397");

    private static final int STATE_LISTENING = 1;
    private static final int STATE_CONNECTING = 2;
    private static final int STATE_CONNECTED = 3;
    private static final int STATE_CONNECTION_FAILED = 4;
    private static final int STATE_MESSAGE_RECIEVED = 5;

    int REQUEST_ENABLE_BLUETOOTH = 1;

    private Button host;
    private Button join;
    private Button listen;
    private ListView bt_listView;
    private ArrayAdapter adapter;
    private ArrayList<String> deviceNames;
    private Set<BluetoothDevice> pairedDevices;
    private ArrayList<BluetoothDevice> btDevices;

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
        btDevices = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, deviceNames);
        bt_listView.setAdapter(adapter);
    }

    private void wireWidgets() {
        host = findViewById(R.id.button_host);
        join = findViewById(R.id.button_join);
        listen = findViewById(R.id.button_listen);
        bt_listView = findViewById(R.id.listView);
    }

    private void setOnClickListeners(){
        host.setOnClickListener(this);
        join.setOnClickListener(this);
        listen.setOnClickListener(this);
        bt_listView.setOnItemClickListener(this);
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) { // Discovery has found a device. Get the BluetoothDevice object and its info from the Intent.

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                pairedDevices = mBluetoothAdapter.getBondedDevices();

                Log.d("DEVICES", "" + pairedDevices); //String deviceHardwareAddress = device.getAddress(); // MAC address

                if(device != null){
                    btDevices.add(device);
                    if(device.getName() != null){
                        deviceNames.add(device.getName());
                    }
                    else{
                        deviceNames.add("Unknown");
                    }
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
            case R.id.button_listen: //list paired devices
                ServerClass serverClass = new ServerClass();
                serverClass.start();
                break;


        }
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {

            switch (message.what)
            {
                case STATE_LISTENING:
                    Toast.makeText(BluetoothActivity.this, "Listening", Toast.LENGTH_SHORT).show();
                    break;
                case STATE_CONNECTING:
                    Toast.makeText(BluetoothActivity.this, "Connecting", Toast.LENGTH_SHORT).show();
                    break;
                case STATE_CONNECTED:
                    Toast.makeText(BluetoothActivity.this, "Connected", Toast.LENGTH_SHORT).show();
                    break;
                case STATE_CONNECTION_FAILED:
                    Toast.makeText(BluetoothActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
                    break;
                case STATE_MESSAGE_RECIEVED:
                    //saving for later
                    break;
            }
            return false;
        }
    });

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
       ClientClass clientClass = new ClientClass(btDevices.get(i));
       clientClass.start();

        Toast.makeText(this, "Connecting", Toast.LENGTH_SHORT).show();
    }

    private class ServerClass extends Thread
    {
        private BluetoothServerSocket serverSocket;

        public ServerClass()
        {
            try {
                serverSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(APP_NAME, MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run()
        {
            BluetoothSocket socket = null;

            while(socket == null)
            {
                try {
                    Message msg = Message.obtain();
                    msg.what = STATE_CONNECTING;
                    handler.sendMessage(msg);

                    socket = serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    Message msg = Message.obtain();
                    msg.what = STATE_CONNECTION_FAILED;
                    handler.sendMessage(msg);
                }

                if(socket != null)
                {
                    Message msg = Message.obtain();
                    msg.what = STATE_CONNECTED;
                    handler.sendMessage(msg);

                    //write some code for send / receive
                    break;
                }

            }
        }

    }

    private class ClientClass extends Thread
    {
        private BluetoothDevice device;
        private BluetoothSocket socket;

        public ClientClass(BluetoothDevice device1)
        {
            device = device1;

            try {
                socket = device.createRfcommSocketToServiceRecord(MY_UUID);
            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }

        public void run()
        {
            try {
                socket.connect();
                Message msg = Message.obtain();
                msg.what = STATE_CONNECTED;
                handler.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
                Message msg = Message.obtain();
                msg.what = STATE_CONNECTION_FAILED;
                handler.sendMessage(msg);
            }
        }
    }

}



