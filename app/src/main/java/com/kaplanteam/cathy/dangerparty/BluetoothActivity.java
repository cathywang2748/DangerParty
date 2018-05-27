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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

/**
 * Created by per6 on 5/7/18.
 */

public class BluetoothActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener {
    private BluetoothAdapter mBluetoothAdapter;

    private static final int REQUEST_DISCOVERABLE = 10;
    private static final String APP_NAME = "DangerParty";
    private static final UUID MY_UUID = UUID.fromString("e141d643-1f16-443f-9da7-c5fbc7081397");

    private static final int STATE_LISTENING = 1;
    private static final int STATE_CONNECTING = 2;
    private static final int STATE_CONNECTED = 3;
    private static final int STATE_CONNECTION_FAILED = 4;
    private static final int STATE_MESSAGE_RECIEVED = 5;

    private Button discoverable;
    private Button list;
    private ListView bt_listView;
    private ArrayAdapter adapter;
    private ArrayList<String> deviceNames;
    private Set<BluetoothDevice> pairedDevices;
    private ArrayList<BluetoothDevice> btDevices;
    private TextView btStatus;
    private TextView msgReceived;
    private EditText msg;
    private Button send;
    public SendReceive sendReceive;
    private ToggleButton btOn;

    private Fragment currentFragment;

    private String localNum;
    public boolean layoutA;

    public String commandForeign;
    public int successes;
    public int failures;
    public int swapString;
    public int swapCurrent;
    public boolean domesticSuccess;
    public boolean swapReady;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Device does not support Bluetooth :(", Toast.LENGTH_LONG).show();
            finish();
        }

        successes = 0;
        failures = 0;

        wireWidgets();
        setOnClickListeners();

        btOn.setChecked(mBluetoothAdapter.isEnabled());

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);

        deviceNames = new ArrayList<>();
        btDevices = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, deviceNames);
        bt_listView.setAdapter(adapter);
    }

    private void wireWidgets() {
        discoverable = findViewById(R.id.button_discoverable);
        list = findViewById(R.id.button_list);
        bt_listView = findViewById(R.id.listView);
        btStatus = findViewById(R.id.textView_status);
        msgReceived = findViewById(R.id.textView_received_msg);
        msg = findViewById(R.id.editText_msg);
        send = findViewById(R.id.button_send);
        btOn = findViewById(R.id.toggleButton_bluetooth);
    }

    private void setOnClickListeners(){
        discoverable.setOnClickListener(this);
        list.setOnClickListener(this);
        bt_listView.setOnItemClickListener(this);
        send.setOnClickListener(this);
        btOn.setOnCheckedChangeListener(this);
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) { // Discovery has found a device. Get the BluetoothDevice object and its info from the Intent.

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                pairedDevices = mBluetoothAdapter.getBondedDevices();

                //String deviceHardwareAddress = device.getAddress(); // MAC address

                if(device != null){
                    btDevices.add(device);
                    if(device.getName() != null){
                        deviceNames.add(device.getName() + ": " + device.getAddress());
                    }
                    else{
                        deviceNames.add("Unknown: " + device.getAddress());
                    }
                }

                adapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_discoverable: //make device discoverable
                Log.d("DISCOVERABLE", "clicked");
                if(!mBluetoothAdapter.isDiscovering()){
                    Intent intentDisc = new Intent(mBluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult(intentDisc, REQUEST_DISCOVERABLE);
                    Log.d("isDISCOVERABLE", "true");
                    ServerClass serverClass = new ServerClass(); //used to be for listen button
                    serverClass.start();
                }
                break;
            case R.id.button_list: //list paired devices
                Log.d("HOST", "clicked");
                mBluetoothAdapter.cancelDiscovery();//NEW------------------------------------------------------------
                mBluetoothAdapter.startDiscovery();///PROBLEM HERE - restart tablet
                Log.d("BLUETOOTH", "" + mBluetoothAdapter.isDiscovering());
                break;
            case R.id.button_send:
                String string = String.valueOf(msg.getText().toString());
                sendReceive.write(string.getBytes());
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
                    btStatus.setText("Listening");
                    break;
                case STATE_CONNECTING:
                    Toast.makeText(BluetoothActivity.this, "Connecting", Toast.LENGTH_SHORT).show();
                    btStatus.setText("Connecting");
                    break;
                case STATE_CONNECTED:
                    Toast.makeText(BluetoothActivity.this, "Connected", Toast.LENGTH_SHORT).show();
                    btStatus.setText("Connected");
                    mBluetoothAdapter.cancelDiscovery();
                    currentFragment = new FragmentBackground2P();
                    switchToNewScreen();
                    list.setVisibility(View.GONE);
                    btOn.setVisibility(View.GONE);
                    discoverable.setVisibility(View.GONE);
                    btStatus.setVisibility(View.GONE);
                    bt_listView.setVisibility(View.GONE);

                    localNum = "" + Math.random();
                    sendReceive.write(localNum.getBytes());
                    break;
                case STATE_CONNECTION_FAILED:
                    Toast.makeText(BluetoothActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
                    btStatus.setText("Connection Failed");
                    break;
                case STATE_MESSAGE_RECIEVED: //-----------------------------------------------OOOOOOOOOOOOOOOO
                    byte[] readBuffer = (byte[])message.obj;
                    String tempMessage = new String(readBuffer, 0, message.arg1);
                    msgReceived.setText("" + tempMessage);
                    Toast.makeText(BluetoothActivity.this, "" + tempMessage, Toast.LENGTH_SHORT).show();

                    //check swap, success, fail
                    if(tempMessage.substring(0,2).equals("0.")) {
                        layoutA = Double.parseDouble(tempMessage) > Double.parseDouble(localNum);
                    }
                    else if(tempMessage.equals("success")){ //listener of some sort?-------------------------------
                        domesticSuccess = false;
                        successes++;
                    }
                    else if(tempMessage.equals("domestic success")){
                        domesticSuccess = true;
                        successes++;
                    }
                    else if(tempMessage.equals("fail")){ //listener of some sort?-------------------------------
                        failures++;
                    }
                    else if(tempMessage.substring(0,4).equals("swap")){
                        swapString = Integer.parseInt(tempMessage.substring(4, 5));
                        swapCurrent = Integer.parseInt(tempMessage.substring(5, 6));
                        swapReady = true;
                    }
                    else{
                        commandForeign = tempMessage;
                    }
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
        btStatus.setText("Connecting");

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch(compoundButton.getId()) {
            case R.id.toggleButton_bluetooth:
                if (b) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, 1);
                } else {
                    if (mBluetoothAdapter.isEnabled()) {
                        mBluetoothAdapter.disable();
                    }
                }
                break;
        }
    }

    private class ServerClass extends Thread
    {
        private BluetoothServerSocket serverSocket;

        public ServerClass()
        {
            Log.d("ServerClass", "created");
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
                try { //PROBLEM COULD BE HERE-------------------------------------------------------------------------------
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

                    //code for send / receive
                    sendReceive = new SendReceive(socket);
                    sendReceive.start();
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
            Log.d("ClientClass", "created");
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
            try { //PROBLEM COULD BE HERE-------------------------------------------------------------------------------
                socket.connect();
                Message msg = Message.obtain();
                msg.what = STATE_CONNECTED;
                handler.sendMessage(msg);

                //code for send/receive
                sendReceive = new SendReceive(socket);
                sendReceive.start();

            } catch (IOException e) {
                e.printStackTrace();
                Message msg = Message.obtain();
                msg.what = STATE_CONNECTION_FAILED;
                handler.sendMessage(msg);
            }
        }
    }

    public class SendReceive extends Thread
    {
        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;

        private SendReceive(BluetoothSocket socket)
        {
            bluetoothSocket = socket;
            InputStream tempIn = null;
            OutputStream tempOut = null;

            try {
                tempIn = bluetoothSocket.getInputStream();
                tempOut = bluetoothSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            inputStream = tempIn;
            outputStream =tempOut;
        }

        public void run()
        {
            byte[] buffer = new byte[1024];
            int bytes;

            while (true)
            {
                try {
                    bytes = inputStream.read(buffer);
                    handler.obtainMessage(STATE_MESSAGE_RECIEVED, bytes, -1, buffer).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void write(byte[] bytes) //-----------------------------------------------OOOOOOOOOOOOOOOO
        {
            try {
                outputStream.write(bytes);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void switchToNewScreen() {
        //tell the fragment manager that if our current fragment isn't null, to replace whatever is there with it
        FragmentManager fm = getSupportFragmentManager();
        if (currentFragment != null) {
            fm.beginTransaction()
                    .replace(R.id.fragment_container, currentFragment)
                    .commitAllowingStateLoss();
        }
    }

    public void resetSandF(){
        successes = 0;
        failures = 0;
    }
}



